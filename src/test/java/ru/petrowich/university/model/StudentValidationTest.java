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

class StudentValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testStudentFirstNameSizeValidation(){
        String longFirstName = new RandomString(300).nextString();
        Student student = new Student().setFirstName(longFirstName);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        List<ConstraintViolation<Student>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("firstName"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person first name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testStudentLastNameSizeValidation(){
        String longLastName = new RandomString(300).nextString();
        Student student = new Student().setLastName(longLastName);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        List<ConstraintViolation<Student>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("lastName"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person last name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testStudentEmailValidation(){
        String email = new RandomString(10).nextString();
        Student student = new Student().setEmail(email);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        List<ConstraintViolation<Student>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("email"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person email is not valid", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testStudentEmailNullValidation(){
        Student student = new Student().setEmail(null);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        List<ConstraintViolation<Student>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("email"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person email is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCourseCommentSizeValidation(){
        String longComment = new RandomString(3000).nextString();
        Student student = new Student().setComment(longComment);
        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        List<ConstraintViolation<Student>> descriptionViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("comment"))
                .collect(Collectors.toList());

        assertFalse(descriptionViolations.isEmpty());
        assertEquals("person comment is more than 2048 characters", descriptionViolations.iterator().next().getMessage());
    }
}
