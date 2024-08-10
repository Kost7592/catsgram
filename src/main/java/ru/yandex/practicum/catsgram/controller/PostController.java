package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping()
    public List<Post> findAll(@RequestParam(name = "size", required = true, defaultValue = "0") int size,
                              @RequestParam(name ="sort", defaultValue = "asc") String sort,
                              @RequestParam(name = "page", defaultValue = "10") int from) {

        return postService.findAll(size, sort, from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post newPost) {
        return postService.create(newPost);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }

    @GetMapping("/{postId")
    public Post findPost(@PathVariable("postId") Long postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.orElse(null);
    }
}
