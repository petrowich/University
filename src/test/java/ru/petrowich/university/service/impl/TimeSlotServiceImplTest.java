package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.TimeSlotRepository;
import ru.petrowich.university.model.TimeSlot;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

class TimeSlotServiceImplTest {
    private static final Integer TIME_SLOT_ID = 1;
    private static final String TIME_SLOT_NAME = "first lesson";
    private static final LocalTime TIME_SLOT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime TIME_SLOT_END_TIME = LocalTime.of(9, 30);

    private static final Set<ConstraintViolation<TimeSlot>> violations = new HashSet<>();

    private final TimeSlot timeSlot = new TimeSlot().setId(TIME_SLOT_ID).setName(TIME_SLOT_NAME).setStartTime(TIME_SLOT_START_TIME).setEndTime(TIME_SLOT_END_TIME);

    private AutoCloseable autoCloseable;

    @Mock
    private TimeSlotRepository mockTimeSlotRepository;

    @Mock
    private Validator mockValidator;

    @InjectMocks
    TimeSlotServiceImpl timeSlotServiceImpl;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnTimeSlotWhenTimeSlotIdPassed() {
        Optional<TimeSlot> optionalTimeSlot = Optional.of(timeSlot);
        when(mockTimeSlotRepository.findById(TIME_SLOT_ID)).thenReturn(optionalTimeSlot);

        TimeSlot actual = timeSlotServiceImpl.getById(TIME_SLOT_ID);

        verify(mockTimeSlotRepository, times(1)).findById(TIME_SLOT_ID);
        assertEquals(timeSlot, actual, "expected timeslot should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentTimeSlotIdPassed() {
        when(mockTimeSlotRepository.findById(-1)).thenReturn(Optional.empty());
        TimeSlot actual = timeSlotServiceImpl.getById(-1);

        verify(mockTimeSlotRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> timeSlotServiceImpl.getById(null), "GetById(null) should throw IllegalArgumentException");
        verify(mockTimeSlotRepository, times(0)).findById(null);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedTimeSlot() {
        when(mockValidator.validate(timeSlot)).thenReturn(violations);
        timeSlotServiceImpl.add(timeSlot);

        verify(mockValidator, times(1)).validate(timeSlot);
        verify(mockTimeSlotRepository, times(1)).save(timeSlot);
    }

    @Test
    void testAddShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        when(mockValidator.validate(timeSlot)).thenReturn(violations);
        assertThrows(IllegalArgumentException.class, () -> timeSlotServiceImpl.add(null), "add(null) should throw IllegalArgumentException");

        verify(mockValidator, times(0)).validate(timeSlot);
        verify(mockTimeSlotRepository, times(0)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedTimeSlot() {
        when(mockValidator.validate(timeSlot)).thenReturn(violations);
        timeSlotServiceImpl.update(timeSlot);

        verify(mockValidator, times(1)).validate(timeSlot);
        verify(mockTimeSlotRepository, times(1)).save(timeSlot);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        when(mockValidator.validate(timeSlot)).thenReturn(violations);
        assertThrows(NullPointerException.class, () -> timeSlotServiceImpl.update(null), "update(null) should throw NullPointerException");

        verify(mockValidator, times(0)).validate(timeSlot);
        verify(mockTimeSlotRepository, times(0)).save(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryDeleteWithPassedTimeSlot() {
        timeSlotServiceImpl.delete(timeSlot);
        verify(mockTimeSlotRepository, times(1)).delete(timeSlot);
    }

    @Test
    void testDeleteShouldInvokeRepositoryDeleteWithPassedNull() {
        doNothing().when(mockTimeSlotRepository).delete(null);
        timeSlotServiceImpl.delete(null);
        verify(mockTimeSlotRepository, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnTimeSlotList() {
        List<TimeSlot> expected = new ArrayList<>();
        expected.add(timeSlot);
        when(mockTimeSlotRepository.findAll()).thenReturn(expected);

        List<TimeSlot> actual = timeSlotServiceImpl.getAll();

        verify(mockTimeSlotRepository, times(1)).findAll();
        assertEquals(expected, actual, "expected timeslot list should be returned");
    }
}
