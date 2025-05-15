package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

@SpringBootTest
class UserTests {
    private static Validator validator;
    private static Set<ConstraintViolation<User>> violations;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private User createUser() {
        var user = new User();

        user.setEmail("dolsa.broadstaff@gmail.com");
        user.setLogin("Novac");
        user.setName("Aleksandr");
        user.setBirthday(LocalDate.of(1996, 1, 22));

        return user;
    }

    @Test
    void validUser() {
        var user = createUser();

        violations = validator.validate(user);
        Assertions.assertTrue(violations.isEmpty(), "Валидация не пройдена.");
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    void validEmail() {
        var user = createUser();

        user.setEmail(" ");
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty(), "Валидация не сработала.");
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation
                .getPropertyPath().toString().equals("email")), "Ожидалось поле email.");

        user.setEmail("novac.comDolsa");
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty(), "Валидация не сработала.");
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation
                .getPropertyPath().toString().equals("email")), "Ожидалось поле email.");
    }

    @Test
    void validLogin() {
        var user = createUser();

        user.setLogin(" ");
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty(), "Валидация не сработала.");
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation
                .getPropertyPath().toString().equals("login")), "Ожидалось поле login.");
    }

    @Test
    void validBirthday() {
        var user = createUser();

        user.setBirthday(null);
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty(), "Валидация не сработала.");
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation
                .getPropertyPath().toString().equals("birthday")), "Ожидалось поле birthday.");

        user.setBirthday(LocalDate.now().plusDays(1));
        violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty(), "Валидация не сработала.");
        Assertions.assertTrue(violations.stream().anyMatch(violation -> violation
                .getPropertyPath().toString().equals("birthday")), "Ожидалось поле birthday.");
    }
}
