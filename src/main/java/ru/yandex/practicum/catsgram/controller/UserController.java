package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
       return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
       return userService.updateUser(newUser);
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.orElse(null);
    }
}
