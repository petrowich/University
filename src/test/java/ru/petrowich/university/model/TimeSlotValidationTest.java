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

class TimeSlotValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testTimeSlotNameSizeValidation(){
        String longName = new RandomString(300).nextString();
        TimeSlot timeSlot = new TimeSlot().setName(longName);
        Set<ConstraintViolation<TimeSlot>> violations = validator.validate(timeSlot);

        List<ConstraintViolation<TimeSlot>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("timeslot name length is more than 255 characters", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testTimeSlotNameNotBlankValidation(){
        TimeSlot timeSlot = new TimeSlot().setName("");
        Set<ConstraintViolation<TimeSlot>> violations = validator.validate(timeSlot);

        List<ConstraintViolation<TimeSlot>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("timeslot name is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testTimeSlotNameNullValidation(){
        TimeSlot timeSlot = new TimeSlot().setName(null);
        Set<ConstraintViolation<TimeSlot>> violations = validator.validate(timeSlot);

        List<ConstraintViolation<TimeSlot>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("name"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("timeslot name is empty", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testTimeSlotStartTimeNullValidation(){
        TimeSlot timeSlot = new TimeSlot().setStartTime(null);
        Set<ConstraintViolation<TimeSlot>> violations = validator.validate(timeSlot);

        List<ConstraintViolation<TimeSlot>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("startTime"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("timeslot start time is null", nameViolations.iterator().next().getMessage());
    }

    @Test
    void testTimeSlotEndTimeNullValidation(){
        TimeSlot timeSlot = new TimeSlot().setEndTime(null);
        Set<ConstraintViolation<TimeSlot>> violations = validator.validate(timeSlot);

        List<ConstraintViolation<TimeSlot>> nameViolations = violations.stream()
                .filter(violation -> violation.getPropertyPath().toString().equals("endTime"))
                .collect(Collectors.toList());

        assertFalse(nameViolations.isEmpty());
        assertEquals("timeslot end time is null", nameViolations.iterator().next().getMessage());
    }
}
