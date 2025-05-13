package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.AfterDate;

import java.time.LocalDate;

@Data
public class Film {
    private Long id;

    @NotBlank(message = "Не может быть пустым.")
    private String name;

    @NotBlank(message = "Название не может быть пустым.")
    @Size(max = 200, message = "Не может быть больше 200 символов.")
    private String description;

    @NotNull
    @AfterDate(value = "1895-12-27", message = "Дата не ранее 28.12.1895.")
    private LocalDate releaseDate;

    @NotNull
    @Min(value = 1, message = "Продолжительность должна быть больше 0")
    private int duration;
}
