package ru.yandex.practicum.catsgram.model;

import lombok.*;

@Data
@Getter
@Setter
@ToString(includeFieldNames=true)
@EqualsAndHashCode(of = {"id"})
public class Image {
    private long id;
    private long authorId;
    private String originalFileName;
    private String filePath;

}
