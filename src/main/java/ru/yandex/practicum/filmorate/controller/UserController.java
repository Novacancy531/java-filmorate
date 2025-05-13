package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User post(@Valid @RequestBody User user) {
        log.info("Добавление пользователя.");
        if (user.getName() == null || user.getName().isBlank()) {
            log.trace("В поле имя установлен логин, т.к. поле имя пустое.");
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.trace("Добавлен пользователь.");
        return user;
    }

    @GetMapping
    public Collection<User> get() {
        log.info("Отправка коллекции пользователей.");
        return users.values();
    }

    @PutMapping
    public ResponseEntity<User> put(@Valid @RequestBody User user) {
        log.info("Обновление данных пользователя.");

        if (user.getId() == null) {
            throw new ConditionsNotMetException("Не указан id пользователя");
        } else if (!users.containsKey(user.getId())) {
            throw new ConditionsNotMetException("Пользователь не найден.");
        }

        users.replace(user.getId(), user);
        log.trace("Данные пользователя обновлены.");
        return ResponseEntity.ok(user);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
