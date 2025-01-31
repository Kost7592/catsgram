package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@ToString(includeFieldNames=true)
@EqualsAndHashCode(of = {"email"})
public class User {
    private Long id;
    private String username;
    private String email;
    private  String password;
    private Instant registrationDate;
}
