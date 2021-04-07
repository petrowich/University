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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TimeSlotRestControllerTest {    
    private static final Integer NONEXISTENT_TIME_SLOT_ID = 9;
    private static final Integer EXISTENT_TIME_SLOT_ID_1 = 1;
    private static final Integer EXISTENT_TIME_SLOT_ID_2 = 2;
    private static final Integer EXISTENT_TIME_SLOT_ID_3 = 3;
    private static final Integer NEW_TIME_SLOT_ID = 9;
    private static final String EXISTENT_TIME_SLOT_NAME_1 = "first";
    private static final String EXISTENT_TIME_SLOT_NAME_2 = "second";
    private static final String EXISTENT_TIME_SLOT_NAME_3 = "third";
    private static final String NEW_TIME_SLOT_NAME = "new time slot";
    private static final String ANOTHER_TIME_SLOT_NAME = "another name";
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_1 = LocalTime.of(8, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_2 = LocalTime.of(9, 40);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_3 = LocalTime.of(11, 20);
    private static final LocalTime NEW_TIME_SLOT_START_TIME = LocalTime.of(13, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_1 = LocalTime.of(9, 30);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_2 = LocalTime.of(11, 10);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_3 = LocalTime.of(12, 50);
    private static final LocalTime NEW_TIME_SLOT_END_TIME = LocalTime.of(14, 50);

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final TimeSlotMapper timeSlotMapper = new TimeSlotMapper(modelMapper);

    private final TimeSlot newTimeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1).setName(NEW_TIME_SLOT_NAME)
            .setStartTime(NEW_TIME_SLOT_START_TIME).setEndTime(NEW_TIME_SLOT_END_TIME);

    private final TimeSlot timeSlot1 = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1).setName(EXISTENT_TIME_SLOT_NAME_1)
            .setStartTime(EXISTENT_TIME_SLOT_START_TIME_1).setEndTime(EXISTENT_TIME_SLOT_END_TIME_1);

    private final TimeSlot timeSlot2 = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_2).setName(EXISTENT_TIME_SLOT_NAME_2)
            .setStartTime(EXISTENT_TIME_SLOT_START_TIME_2).setEndTime(EXISTENT_TIME_SLOT_END_TIME_2);

    private final TimeSlot timeSlot3 = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_3).setName(EXISTENT_TIME_SLOT_NAME_3)
            .setStartTime(EXISTENT_TIME_SLOT_START_TIME_3).setEndTime(EXISTENT_TIME_SLOT_END_TIME_3);

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
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot1);

        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(timeSlot1);
        when(mockTimeSlotMapper.toDto(timeSlot1)).thenReturn(timeSlotDTO);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.getTimeSlot(EXISTENT_TIME_SLOT_ID_1);

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot1);
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
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot1);
        TimeSlot expectedTimeSlot = timeSlotMapper.toEntity(timeSlotDTO).setName(ANOTHER_TIME_SLOT_NAME);

        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(timeSlot1);
        when(mockTimeSlotMapper.toEntity(timeSlotDTO)).thenReturn(timeSlot1);
        when(mockTimeSlotService.update(timeSlot1)).thenReturn(expectedTimeSlot);

        TimeSlotDTO expectedTimeSlotDTO = timeSlotMapper.toDto(expectedTimeSlot);
        when(mockTimeSlotMapper.toDto(expectedTimeSlot)).thenReturn(expectedTimeSlotDTO);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.updateTimeSlot(timeSlotDTO, EXISTENT_TIME_SLOT_ID_1);

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotMapper, times(1)).toEntity(timeSlotDTO);
        verify(mockTimeSlotService, times(1)).update(timeSlot1);
        verify(mockTimeSlotMapper, times(1)).toDto(expectedTimeSlot);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedTimeSlotDTO);
    }

    @Test
    void testUpdateTimeSlotShouldReturnBadRequestWhenNullIdPassed() {
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot1);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.updateTimeSlot(timeSlotDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() {
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot1);

        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.updateTimeSlot(timeSlotDTO, NONEXISTENT_TIME_SLOT_ID);

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteTimeSlotShouldReturnOK() {
        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(timeSlot1);

        ResponseEntity<TimeSlotDTO> responseEntity = timeSlotRestController.deleteTimeSlot(NONEXISTENT_TIME_SLOT_ID);

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
        verify(mockTimeSlotService, times(1)).delete(timeSlot1);
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
        TimeSlotDTO timeSlotDTO1 = timeSlotMapper.toDto(timeSlot1);
        TimeSlotDTO timeSlotDTO2 = timeSlotMapper.toDto(timeSlot2);
        TimeSlotDTO timeSlotDTO3 = timeSlotMapper.toDto(timeSlot3);

        List<TimeSlot> timeSlots = new ArrayList<>(asList(timeSlot1, timeSlot2, timeSlot3));

        when(mockTimeSlotService.getAll()).thenReturn(timeSlots);
        when(mockTimeSlotMapper.toDto(timeSlot1)).thenReturn(timeSlotDTO1);
        when(mockTimeSlotMapper.toDto(timeSlot2)).thenReturn(timeSlotDTO2);
        when(mockTimeSlotMapper.toDto(timeSlot3)).thenReturn(timeSlotDTO3);

        ResponseEntity<List<TimeSlotDTO>> responseEntity = timeSlotRestController.getAllTimeSlots();

        verify(mockTimeSlotService, times(1)).getAll();
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot1);
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot2);
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot3);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<TimeSlotDTO> expectedTimeSlotDTOList = new ArrayList<>(asList(timeSlotDTO1, timeSlotDTO2, timeSlotDTO3));
        assertThat(expectedTimeSlotDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
