package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.model.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final Map<Long, Image> images = new HashMap<>();

    public List<Image> getPostImages(Long postId) {
        return images.values().stream()
                .filter(image -> image.getPostId() == postId)
                .collect(Collectors.toList());
    }
}
