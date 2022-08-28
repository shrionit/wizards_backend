package com.code.wizards.student;

public class StudentNotFoundException extends RuntimeException {
    StudentNotFoundException(Long id){
        super("Student not found with id : "+id);
    }
}
