package ru.petrowich.university.model;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class LecturerValidationTest {
    @Autowired
    private Validator validator;

    @Test
    void testLecturerFirstNameSizeValidation(){
        String longFirstName = new RandomString(300).nextString();
        Lecturer lecturer = new Lecturer().setFirstName(longFirstName);
        Set<ConstraintViolation<Lecturer>> violations = validator.validate(lecturer);

        List<ConstraintViolation<Lecturer>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("firstName"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person first name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testLecturerLastNameSizeValidation(){
        String longLastName = new RandomString(300).nextString();
        Lecturer lecturer = new Lecturer().setLastName(longLastName);
        Set<ConstraintViolation<Lecturer>> violations = validator.validate(lecturer);

        List<ConstraintViolation<Lecturer>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("lastName"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person last name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testLecturerEmailValidation(){
        String email = new RandomString(10).nextString();
        Lecturer lecturer = new Lecturer().setEmail(email);
        Set<ConstraintViolation<Lecturer>> violations = validator.validate(lecturer);

        List<ConstraintViolation<Lecturer>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("email"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person email is not valid", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testLecturerEmailNullValidation(){
        Lecturer lecturer = new Lecturer().setEmail(null);
        Set<ConstraintViolation<Lecturer>> violations = validator.validate(lecturer);

        List<ConstraintViolation<Lecturer>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("email"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("person email is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCourseCommentSizeValidation(){
        String longComment = new RandomString(3000).nextString();
        Lecturer lecturer = new Lecturer().setComment(longComment);
        Set<ConstraintViolation<Lecturer>> violations = validator.validate(lecturer);

        List<ConstraintViolation<Lecturer>> descriptionViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("comment"))
                .collect(Collectors.toList());

        assertFalse(descriptionViolations.isEmpty());
        assertEquals("person comment is more than 2048 characters", descriptionViolations.iterator().next().getMessage());
    }
}
