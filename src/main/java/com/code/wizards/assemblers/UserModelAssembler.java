package com.code.wizards.assemblers;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.code.wizards.api.UserController;
import com.code.wizards.models.User;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
            linkTo(methodOn(UserController.class).one(user.getId())).withSelfRel(),
            linkTo((methodOn(UserController.class)).all()).withRel("users"));
    }
}