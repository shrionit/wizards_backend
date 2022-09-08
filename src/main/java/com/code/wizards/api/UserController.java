package com.code.wizards.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.wizards.models.User;
import com.code.wizards.services.UserService;
import com.code.wizards.assemblers.UserModelAssembler;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler userModelAssembler){
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> models = userService.getAllUsers()
                .stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(models, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<User> one(@PathVariable Object id) {
        User user = null;
        try {
            UUID _id = UUID.fromString(id.toString());
            user = userService.getUserById(_id);
        } catch (Exception e) {
            user = userService.getUserById(id.toString());
        }        
        return EntityModel.of(user);
    }

    @PostMapping
    public EntityModel<?> addUser(@RequestBody User user) {
        return EntityModel.of(userService.addUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable UUID id) {
        User updatedUser = userService.updateUser(id, user);
        EntityModel<User> model = userModelAssembler.toModel(updatedUser);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable UUID id, @RequestBody Map<String, ?> json){
        User updatedUser = userService.patchUser(id, json);
        EntityModel<User> model = userModelAssembler.toModel(updatedUser);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id){
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
