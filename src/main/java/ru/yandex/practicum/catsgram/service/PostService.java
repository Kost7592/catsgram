package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

// Указываем, что класс PostService - является бином и его
// нужно добавить в контекст приложения
@Service
public class PostService {
    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<Post> findAll(int size, String sort, int from) {
        Collection<Post> postsCollection = posts.values();
        List<Post> postsList = new ArrayList<>(postsCollection);
        if (sort.equals("asc")) {
            return postsList.stream()
                    .sorted(Comparator.comparing(Post::getPostDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else if (sort.equals("desc")) {
            return postsList .stream()
                    .sorted(Comparator.comparing(Post::getPostDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
        return postsList.subList(from, Math.min(size + from - 1, postsList.size()));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        Optional<User> user = userService.getUserById(post.getAuthorId());
        if (user.isEmpty()) {
            throw new ConditionsNotMetException("Автор с id " + post.getAuthorId() + " не найден!");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
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
    }

    public Optional<Post> getPostById(Long postId) {
        return Optional.ofNullable(posts.getOrDefault(postId, null));
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}