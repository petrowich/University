package ru.petrowich.university.controller.lessons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.dto.lessons.TimeSlotDTO;
import ru.petrowich.university.mapper.lesson.TimeSlotMapper;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class TimeSlotRestControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer NONEXISTENT_TIME_SLOT_ID = 9;
    private static final Integer EXISTENT_TIME_SLOT_ID_1 = 1;
    private static final Integer NEW_TIME_SLOT_ID = 9;
    private static final String ANOTHER_TIME_SLOT_NAME = "another name";

    private final ObjectMapper objectMapper = new ObjectMapper();
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
        mockMvc = standaloneSetup(timeSlotRestController).build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetTimeSlotShouldReturnOK() throws Exception {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(timeSlot);
        when(mockTimeSlotMapper.toDto(timeSlot)).thenReturn(timeSlotDTO);

        mockMvc.perform(get("/api/lessons/timeslots/{id}", EXISTENT_TIME_SLOT_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_TIME_SLOT_ID_1));

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot);
    }

    @Test
    void testGetTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);

        mockMvc.perform(get("/api/lessons/timeslots/{id}", NONEXISTENT_TIME_SLOT_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
    }

    @Test
    void testAddTimeSlotShouldReturnCreated() throws Exception {
        TimeSlot newTimeSlot = new TimeSlot();
        TimeSlotDTO newTimeSlotDTO = timeSlotMapper.toDto(newTimeSlot);
        String newTimeSlotJSON = objectMapper.writeValueAsString(newTimeSlotDTO);
        TimeSlot expectedTimeSlot = timeSlotMapper.toEntity(newTimeSlotDTO).setId(NEW_TIME_SLOT_ID);

        when(mockTimeSlotMapper.toEntity(newTimeSlotDTO)).thenReturn(newTimeSlot);
        when(mockTimeSlotService.add(newTimeSlot)).thenReturn(expectedTimeSlot);

        TimeSlotDTO expectedTimeSlotDTO = timeSlotMapper.toDto(expectedTimeSlot);
        when(mockTimeSlotMapper.toDto(expectedTimeSlot)).thenReturn(expectedTimeSlotDTO);

        mockMvc.perform(post("/api/lessons/timeslots/add")
                .content(newTimeSlotJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_TIME_SLOT_ID));

        verify(mockTimeSlotMapper, times(1)).toEntity(newTimeSlotDTO);
        verify(mockTimeSlotService, times(1)).add(newTimeSlot);
        verify(mockTimeSlotMapper, times(1)).toDto(expectedTimeSlot);
    }

    @Test
    void testUpdateTimeSlotShouldReturnOK() throws Exception {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1).setName(ANOTHER_TIME_SLOT_NAME);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1));
        when(mockTimeSlotMapper.toEntity(timeSlotDTO)).thenReturn(timeSlot);
        when(mockTimeSlotService.update(timeSlot)).thenReturn(timeSlot);
        when(mockTimeSlotMapper.toDto(timeSlot)).thenReturn(timeSlotDTO);

        String timeSlotJSON = objectMapper.writeValueAsString(timeSlotDTO);

        mockMvc.perform(put("/api/lessons/timeslots/update/{id}", EXISTENT_TIME_SLOT_ID_1)
                .content(timeSlotJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_TIME_SLOT_ID_1))
                .andExpect(jsonPath("$.name").value(ANOTHER_TIME_SLOT_NAME));

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotMapper, times(1)).toEntity(timeSlotDTO);
        verify(mockTimeSlotService, times(1)).update(timeSlot);
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot);
    }

    @Test
    void testUpdateTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        TimeSlot timeSlot = new TimeSlot().setId(NONEXISTENT_TIME_SLOT_ID);
        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);
        String timeSlotJSON = objectMapper.writeValueAsString(timeSlotDTO);

        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);

        mockMvc.perform(put("/api/lessons/timeslots/update/{id}", NONEXISTENT_TIME_SLOT_ID)
                .content(timeSlotJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockTimeSlotService, times(1)).getById(NONEXISTENT_TIME_SLOT_ID);
    }

    @Test
    void testDeleteTimeSlotShouldReturnOK() throws Exception {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        when(mockTimeSlotService.getById(EXISTENT_TIME_SLOT_ID_1)).thenReturn(timeSlot);

        mockMvc.perform(delete("/api/lessons/timeslots/delete/{id}", EXISTENT_TIME_SLOT_ID_1))
                .andExpect(status().isOk());

        verify(mockTimeSlotService, times(1)).getById(EXISTENT_TIME_SLOT_ID_1);
        verify(mockTimeSlotService, times(1)).delete(timeSlot);
    }

    @Test
    void testDeleteTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockTimeSlotService.getById(NONEXISTENT_TIME_SLOT_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/lessons/timeslots/delete/{id}", NONEXISTENT_TIME_SLOT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllTimeSlotsShouldReturnOK() throws Exception {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        TimeSlotDTO timeSlotDTO1 = timeSlotMapper.toDto(timeSlot);

        List<TimeSlot> timeSlots = new ArrayList<>(Collections.singletonList(timeSlot));

        when(mockTimeSlotService.getAll()).thenReturn(timeSlots);
        when(mockTimeSlotMapper.toDto(timeSlot)).thenReturn(timeSlotDTO1);

        mockMvc.perform(get("/api/lessons/timeslots/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_TIME_SLOT_ID_1));

        verify(mockTimeSlotService, times(1)).getAll();
        verify(mockTimeSlotMapper, times(1)).toDto(timeSlot);
    }
}
