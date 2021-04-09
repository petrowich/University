package ru.petrowich.university.controller.lessons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.petrowich.university.dto.lessons.TimeSlotDTO;
import ru.petrowich.university.mapper.lesson.TimeSlotMapper;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TimeSlotRestControllerTest {    
    private static final Integer NONEXISTENT_TIME_SLOT_ID = 9;
    private static final Integer EXISTENT_TIME_SLOT_ID_1 = 1;
    private static final Integer NEW_TIME_SLOT_ID = 9;
    private static final String ANOTHER_TIME_SLOT_NAME = "another name";

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final TimeSlotMapper timeSlotMapper = new TimeSlotMapper(modelMapper);

    @Mock
    TimeSlotService mockTimeSlotService;

    @Mock
    TimeSlotMapper mockTimeSlotMapper;

    @InjectMocks
    TimeSlotRestController timeSlotRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetTimeSlotShouldReturnOK() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(timeSlot);
        when(mockTimeSlotMapper.toDto(timeSlot)).thenReturn(timeSlotDTO);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.getTimeSlot(EXISTENT_TIME_SLOT_ID_1);

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(timeSlotDTO);
    }

    @Test
    void testGetTimeSlotShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.getTimeSlot(null);

        verify(mockTimeSlotService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.getTimeSlot(NONEXISTENT_TIME_SLOT_ID);

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddTimeSlotShouldReturnCreated() {
        TimeSlot newTimeSlot = new TimeSlot();
        TimeSlotDTO newTimeSlotDTO = timeSlotMapper.toDto(newTimeSlot);
        TimeSlot expectedTimeSlot = timeSlotMapper.toEntity(newTimeSlotDTO).setId(NEW_TIME_SLOT_ID);

        when(mockTimeSlotMapper.toEntity(newTimeSlotDTO)).thenReturn(newTimeSlot);
        when(mockTimeSlotService.add(newTimeSlot)).thenReturn(expectedTimeSlot);

        TimeSlotDTO expectedTimeSlotDTO = timeSlotMapper.toDto(expectedTimeSlot);
        when(mockTimeSlotMapper.toDto(expectedTimeSlot)).thenReturn(expectedTimeSlotDTO);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.addTimeSlot(newTimeSlotDTO);

        verify(mockTimeSlotMapper, times(1)).toEntity(newTimeSlotDTO);
        verify(mockTimeSlotService, times(1)).add(newTimeSlot);
        verify(mockTimeSlotMapper, times(1)).toDto(expectedTimeSlot);
        assertThat(responseEntity.getBody()).isEqualTo(expectedTimeSlotDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateTimeSlotShouldReturnOK() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);
        TimeSlot expectedTimeSlot = timeSlotMapper.toEntity(timeSlotDTO).setName(ANOTHER_TIME_SLOT_NAME);

        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(timeSlot);
        when(mockTimeSlotMapper.toEntity(timeSlotDTO)).thenReturn(timeSlot);
        when(mockTimeSlotService.update(timeSlot)).thenReturn(expectedTimeSlot);

        TimeSlotDTO expectedTimeSlotDTO = timeSlotMapper.toDto(expectedTimeSlot);
        when(mockTimeSlotMapper.toDto(expectedTimeSlot)).thenReturn(expectedTimeSlotDTO);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.updateTimeSlot(timeSlotDTO, EXISTENT_TIME_SLOT_ID_1);

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotMapper, times(1)).toEntity(timeSlotDTO);
        verify(mockTimeSlotService, times(1)).update(timeSlot);
        verify(mockTimeSlotMapper, times(1)).toDto(expectedTimeSlot);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedTimeSlotDTO);
    }

    @Test
    void testUpdateTimeSlotShouldReturnBadRequestWhenNullIdPassed() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.updateTimeSlot(timeSlotDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.updateTimeSlot(timeSlotDTO, NONEXISTENT_TIME_SLOT_ID);

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteTimeSlotShouldReturnOK() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(timeSlot);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.deleteTimeSlot(NONEXISTENT_TIME_SLOT_ID);

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
        verify(mockTimeSlotService, times(1)).delete(timeSlot);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteTimeSlotShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.deleteTimeSlot(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);
        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.deleteTimeSlot(NONEXISTENT_TIME_SLOT_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllTimeSlotsShouldReturnOK() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO1 = timeSlotMapper.toDto(timeSlot);

        List<TimeSlot> timeSlots = new ArrayList<>(Collections.singletonList(timeSlot));

        when(mockTimeSlotService.getAll()).thenReturn(timeSlots);
        when(mockTimeSlotMapper.toDto(timeSlot)).thenReturn(timeSlotDTO1);

        ResponseEntity<List<TimeSlotDTO>> responseEntity = timeSlotRestController.getAllTimeSlots();

        verify(mockTimeSlotService, times(1)).getAll();
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<TimeSlotDTO> expectedTimeSlotDTOList = new ArrayList<>(singletonList(timeSlotDTO1));
        assertThat(expectedTimeSlotDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
