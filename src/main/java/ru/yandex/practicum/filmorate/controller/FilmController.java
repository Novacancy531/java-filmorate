package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        log.info("Добавление фильма.");
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.trace("Добавлен фильм.");
        return film;
    }

    @GetMapping
    public Collection<Film> get() {
        log.info("Отправка коллекции фильмов.");
        return films.values();
    }

    @PutMapping
    public ResponseEntity<Film> put(@Valid @RequestBody Film film) {
        log.info("Обновление данных фильма.");

        if (film.getId() == null) {
            throw new ConditionsNotMetException("Не указан id фильма.");
        } else if (!films.containsKey(film.getId())) {
            throw new ConditionsNotMetException("Фильм не найден.");
        }

        films.replace(film.getId(), film);
        log.trace("Данные фильма обновлены.");
        return ResponseEntity.ok(film);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
