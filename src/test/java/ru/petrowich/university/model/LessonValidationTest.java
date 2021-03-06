package ru.petrowich.university.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class LessonValidationTest {
    @Autowired
    private Validator validator;

    @Test
    void testLessonDatePastValidation() {
        LocalDate pastDate = LocalDate.of(2000, 12, 31);
        Lesson Lesson = new Lesson().setDate(pastDate);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(Lesson);

        List<ConstraintViolation<Lesson>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("date"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("lesson date is in the past", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testLessonDateNullValidation() {
        Lesson Lesson = new Lesson().setDate(null);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(Lesson);

        List<ConstraintViolation<Lesson>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("date"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("lesson date is null", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testLessonStartTimeNullValidation() {
        Lesson Lesson = new Lesson().setStartTime(null);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(Lesson);

        List<ConstraintViolation<Lesson>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("startTime"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("lesson start time is null", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testLessonEndTimeNullValidation() {
        Lesson Lesson = new Lesson().setEndTime(null);
        Set<ConstraintViolation<Lesson>> violations = validator.validate(Lesson);

        List<ConstraintViolation<Lesson>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("endTime"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("lesson end time is null", nameViolations.iterator().next().getMessage());
    }
}
