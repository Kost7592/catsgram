package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final UserService userService;
    private final List<Post> posts = new ArrayList<>();
    private int id = 0;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }
    public List<Post> findAll() {
        return posts;
    }

    public Post findPostById(Long postId) {
        return posts.stream()
                .filter(p ->p.getId.equals(id))
    }

    public Post create(Post post) {
        User postAuthor = userService.findUserById(post.getAuthorId());



        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        Optional<User> user = userService.findUserById(post.getAuthorId());
        if (user.isEmpty()) {
            throw new ConditionsNotMetException("Автор с id " + post.getAuthorId() + " не найден!");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.add(post);
        return post;
    }

    /*public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }*/

    private long getNextId() {
        return id++;
    }
}