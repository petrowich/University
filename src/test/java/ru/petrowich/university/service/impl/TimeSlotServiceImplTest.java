package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.petrowich.university.dao.TimeSlotDAO;
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
import static org.mockito.MockitoAnnotations.initMocks;

class TimeSlotServiceImplTest {
    private static final Integer TIME_SLOT_ID = 1;
    private static final String TIME_SLOT_NAME = "first lesson";
    private static final LocalTime TIME_SLOT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime TIME_SLOT_END_TIME = LocalTime.of(9, 30);

    private final TimeSlot timeSlot = new TimeSlot().setId(TIME_SLOT_ID).setName(TIME_SLOT_NAME).setStartTime(TIME_SLOT_START_TIME).setEndTime(TIME_SLOT_END_TIME);

    @Mock
    private TimeSlotDAO mockTimeSlotDAO;

    @InjectMocks
    TimeSlotServiceImpl timeSlotServiceImpl;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testGetByIdShouldReturnTimeSlotWhenTimeSlotIdPassed() {
        when(mockTimeSlotDAO.getById(TIME_SLOT_ID)).thenReturn(timeSlot);

        TimeSlot actual = timeSlotServiceImpl.getById(TIME_SLOT_ID);

        verify(mockTimeSlotDAO, times(1)).getById(TIME_SLOT_ID);
        assertEquals(timeSlot, actual, "expected timeslot should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentTimeSlotIdPassed() {
        when(mockTimeSlotDAO.getById(-1)).thenReturn(null);
        TimeSlot actual = timeSlotServiceImpl.getById(-1);

        verify(mockTimeSlotDAO, times(1)).getById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNullPassed() {
        when(mockTimeSlotDAO.getById(null)).thenReturn(null);
        TimeSlot actual = timeSlotServiceImpl.getById(null);
        verify(mockTimeSlotDAO, times(1)).getById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeDaoAddWithPassedTimeSlot() {
        doNothing().when(mockTimeSlotDAO).add(timeSlot);
        timeSlotServiceImpl.add(timeSlot);
        verify(mockTimeSlotDAO, times(1)).add(timeSlot);
    }

    @Test
    void testAddShouldInvokeDaoAddWithPassedNull() {
        doNothing().when(mockTimeSlotDAO).add(null);
        timeSlotServiceImpl.add(null);
        verify(mockTimeSlotDAO, times(1)).add(null);
    }

    @Test
    void testUpdateShouldInvokeDaoAddWithPassedTimeSlot() {
        doNothing().when(mockTimeSlotDAO).update(timeSlot);
        timeSlotServiceImpl.update(timeSlot);
        verify(mockTimeSlotDAO, times(1)).update(timeSlot);
    }

    @Test
    void testUpdateShouldInvokeDaoAddWithPassedNull() {
        doNothing().when(mockTimeSlotDAO).update(null);
        timeSlotServiceImpl.update(null);
        verify(mockTimeSlotDAO, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeDaoAddWithPassedTimeSlot() {
        doNothing().when(mockTimeSlotDAO).delete(timeSlot);
        timeSlotServiceImpl.delete(timeSlot);
        verify(mockTimeSlotDAO, times(1)).delete(timeSlot);
    }

    @Test
    void testDeleteShouldInvokeDaoAddWithPassedNull() {
        doNothing().when(mockTimeSlotDAO).delete(null);
        timeSlotServiceImpl.delete(null);
        verify(mockTimeSlotDAO, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnTimeSlotList() {
        List<TimeSlot> expected = new ArrayList<>();
        expected.add(timeSlot);
        when(mockTimeSlotDAO.getAll()).thenReturn(expected);

        List<TimeSlot> actual = timeSlotServiceImpl.getAll();

        verify(mockTimeSlotDAO, times(1)).getAll();
        assertEquals(expected, actual, "expected timeslot list should be returned");
    }
}
