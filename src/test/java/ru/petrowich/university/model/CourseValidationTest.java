package ru.petrowich.university.model;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testCourseNameSizeValidation(){
        String longName = new RandomString(300).nextString();
        Course course = new Course().setName(longName);
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        List<ConstraintViolation<Course>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("course name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCourseNameNotBlankValidation() {
        Course course = new Course().setName("");
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        List<ConstraintViolation<Course>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("course name is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCourseNameNullValidation() {
        Course course = new Course().setName(null);
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        List<ConstraintViolation<Course>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("course name is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCourseDescriptionSizeValidation(){
        String longDescription = new RandomString(3000).nextString();
        Course course = new Course().setDescription(longDescription);
        Set<ConstraintViolation<Course>> violations = validator.validate(course);

        List<ConstraintViolation<Course>> descriptionViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("description"))
                .collect(Collectors.toList());

        assertFalse(descriptionViolations.isEmpty());
        assertEquals("description is more than 2048 characters", descriptionViolations.iterator().next().getMessage());
    }
}
