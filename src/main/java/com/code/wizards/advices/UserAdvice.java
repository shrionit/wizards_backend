package com.code.wizards.advices;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.code.wizards.exceptions.UserNotFoundException;
import com.code.wizards.tools.ErrorResponse;
import com.code.wizards.tools.FormError;

@ControllerAdvice
public class UserAdvice {
    
    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<?> userNotFoundHandler(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<?> validationError(ConstraintViolationException ex) {
        List<FormError> errors = ex
                .getConstraintViolations()
                .stream()
                .map(e -> new FormError(e.getPropertyPath().toString(), e.getMessage()))
                .collect(Collectors.toList());
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> validationError(DataIntegrityViolationException ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unique Field Error", List.of(ex.getRootCause().getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
