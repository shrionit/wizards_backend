package com.code.wizards.student;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.EntityMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    StudentRepository repo;

    @Autowired
    StudentModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Student>> all(){
        List<EntityModel<Student>> models = repo
            .findAll()
            .stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());
        return CollectionModel.of(models, linkTo(methodOn(StudentController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Student> one(@PathVariable long id){
        Student student = repo.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        return assembler.toModel(student);
    }

    @PostMapping
    public EntityModel<Student> addStudent(@RequestBody Student student){
        return EntityModel.of(repo.save(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody Student student, @PathVariable long id){
        Student updatedStudent = repo.findById(id).map(s -> {
            s.setName(student.getName());
            s.setAge(student.getAge());
            return repo.save(s);
        }).orElseGet(() -> {
            student.setId(id);
            return repo.save(student);
        });
        EntityModel<Student> model = assembler.toModel(updatedStudent);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
}
