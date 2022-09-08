package com.code.wizards.services;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.code.wizards.exceptions.UserNotFoundException;
import com.code.wizards.models.User;
import com.code.wizards.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository repo;

    @Autowired
    public UserService(@Qualifier("mysql") UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUserById(final UUID id) {
        return repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserById(final String id) {
        Optional<User> user = repo.findByEmail(id);
        if (user.isEmpty())
            user = repo.findByUsername(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    public User addUser(User user) {
        return repo.save(user);
    }

    public User updateUser(final UUID id, User user) {
        User u = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setCreatedAt(null);
        user.setUpdatedAt(null);
        user.setId(u.getId());
        user.setCreatedAt(u.getCreatedAt());
        return repo.save(user);
    }

    public User patchUser(final UUID id, Map<String, ?> patch) {
        User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        invokeUserSetters(user, patch);
        return repo.save(user);
    }

    public User deleteUser(final UUID id) {
        User user = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repo.deleteById(id);
        return user;
    }

    private void invokeUserSetters(User user, Map<String, ?> json) {
        List.of(User.class.getDeclaredMethods())
            .stream()
            .filter(m -> m.getName().startsWith("set"))
            .forEach(m -> {
                json
                    .entrySet()
                    .stream()
                    .filter(pair -> m.getName().toLowerCase().endsWith(pair.getKey()))
                    .forEach(pair -> invokeMethod(m, user, pair.getValue()));
            });
    }

    private void invokeMethod(Method method, User user, Object... args) {
        try {
            method.invoke(user, args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
