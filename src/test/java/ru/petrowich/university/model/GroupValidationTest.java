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
class GroupValidationTest {
    @Autowired
    private Validator validator;

    @Test
    void testGroupNameSizeValidation(){
        String longName = new RandomString(300).nextString();
        Group group = new Group().setName(longName);
        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        List<ConstraintViolation<Group>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("group name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testGroupNameNotBlankValidation() {
        Group group = new Group().setName("");
        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        List<ConstraintViolation<Group>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("group name is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testGroupNameNullValidation() {
        Group group = new Group().setName(null);
        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        List<ConstraintViolation<Group>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("group name is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCapacityMinValidation() {
        Group group = new Group().setCapacity(0);
        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        List<ConstraintViolation<Group>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("capacity"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("group capacity is less than 1", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testCapacityMaxValidation() {
        Group group = new Group().setCapacity(1000);
        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        List<ConstraintViolation<Group>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("capacity"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("group capacity exceed 100", nameViolations.iterator().next().getMessage());
    }
}