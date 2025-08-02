package com.post_hub.utils_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum PostioService {

    IAM_SERVICE("Iam-service"),
    // Поле показывает, из какого микросервиса пришёл лог

    UNDEFINED_SERVICE("Undefined-service");

    private final String value;

    public static PostioService fromValue(String searchValue) {
        return Arrays.stream(PostioService.values())
                .filter(v -> Objects.equals(v.value, searchValue))
                .findFirst()
                .orElse(UNDEFINED_SERVICE);
    }
    // метод нужен, чтобы по строковому значению, который идёт из базы найти соответствующий enum
}