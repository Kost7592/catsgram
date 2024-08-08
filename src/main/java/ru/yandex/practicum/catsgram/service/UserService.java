package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("@mail не может быть пустым!");
        }
        String userEmail = user.getEmail();
        List<String> usersEmail = getUsersEmails();
        if (usersEmail.contains(user.getEmail())) {
            throw new DuplicateFormatFlagsException("Этот @mail уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User updateUser = users.get(newUser.getId());

            if (updateUser.getEmail().equals(newUser.getEmail())) {
                List<String> usersEmails = getUsersEmails();
                if (usersEmails.contains(newUser.getEmail())) {
                    throw new DuplicatedDataException("Этот @mail уже используется");
                }
            } else {
                if (newUser.getEmail() != null) {
                    updateUser.setEmail(newUser.getEmail());
                }
                if (newUser.getUsername() != null) {
                    updateUser.setUsername(newUser.getUsername());
                }
                if (newUser.getPassword() != null) {
                    updateUser.setPassword(newUser.getPassword());
                }
            }
            return updateUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.getOrDefault(id, null));
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private List<String> getUsersEmails() {
        Collection<User> usersList = users.values();
        return usersList.stream()
                .map(User::getEmail)
                .collect(ArrayList::new, List::add, List::addAll);
    }
}