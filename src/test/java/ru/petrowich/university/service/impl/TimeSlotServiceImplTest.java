package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.TimeSlotRepository;
import ru.petrowich.university.model.TimeSlot;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

class TimeSlotServiceImplTest {
    private static final Integer TIME_SLOT_ID = 1;
    private static final String TIME_SLOT_NAME = "first lesson";
    private static final LocalTime TIME_SLOT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime TIME_SLOT_END_TIME = LocalTime.of(9, 30);

    private final TimeSlot timeSlot = new TimeSlot().setId(TIME_SLOT_ID).setName(TIME_SLOT_NAME).setStartTime(TIME_SLOT_START_TIME).setEndTime(TIME_SLOT_END_TIME);

    private AutoCloseable autoCloseable;

    @Mock
    private TimeSlotRepository mockTimeSlotRepository;

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
        when(mockTimeSlotRepository.findById(TIME_SLOT_ID)).thenReturn(timeSlot);

        TimeSlot actual = timeSlotServiceImpl.getById(TIME_SLOT_ID);

        verify(mockTimeSlotRepository, times(1)).findById(TIME_SLOT_ID);
        assertEquals(timeSlot, actual, "expected timeslot should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentTimeSlotIdPassed() {
        when(mockTimeSlotRepository.findById(-1)).thenReturn(null);
        TimeSlot actual = timeSlotServiceImpl.getById(-1);

        verify(mockTimeSlotRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNullPassed() {
        when(mockTimeSlotRepository.findById(null)).thenReturn(null);
        TimeSlot actual = timeSlotServiceImpl.getById(null);
        verify(mockTimeSlotRepository, times(1)).findById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeRepositoryAddWithPassedTimeSlot() {
        doNothing().when(mockTimeSlotRepository).save(timeSlot);
        timeSlotServiceImpl.add(timeSlot);
        verify(mockTimeSlotRepository, times(1)).save(timeSlot);
    }

    @Test
    void testAddShouldInvokeRepositoryAddWithPassedNull() {
        doNothing().when(mockTimeSlotRepository).save(null);
        timeSlotServiceImpl.add(null);
        verify(mockTimeSlotRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositoryAddWithPassedTimeSlot() {
        doNothing().when(mockTimeSlotRepository).update(timeSlot);
        timeSlotServiceImpl.update(timeSlot);
        verify(mockTimeSlotRepository, times(1)).update(timeSlot);
    }

    @Test
    void testUpdateShouldInvokeRepositoryAddWithPassedNull() {
        doNothing().when(mockTimeSlotRepository).update(null);
        timeSlotServiceImpl.update(null);
        verify(mockTimeSlotRepository, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryAddWithPassedTimeSlot() {
        doNothing().when(mockTimeSlotRepository).delete(timeSlot);
        timeSlotServiceImpl.delete(timeSlot);
        verify(mockTimeSlotRepository, times(1)).delete(timeSlot);
    }

    @Test
    void testDeleteShouldInvokeRepositoryAddWithPassedNull() {
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
