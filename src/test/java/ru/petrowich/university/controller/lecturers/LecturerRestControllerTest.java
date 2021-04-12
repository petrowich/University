package ru.petrowich.university.controller.lecturers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.dto.lecturers.LecturerDTO;
import ru.petrowich.university.mapper.lecturers.LecturerMapper;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
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

class LecturerRestControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer NEW_PERSON_ID = 50007;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String ANOTHER_PERSON_EMAIL = "another@mail.com";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();
    private final LecturerMapper lecturerMapper = new LecturerMapper(modelMapper);

    @Mock
    private LecturerService mockLecturerService;

    @Mock
    private LecturerMapper mockLecturerMapper;

    @InjectMocks
    private LecturerRestController lecturerRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
        mockMvc = standaloneSetup(lecturerRestController).build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetLecturerShouldReturnOK() throws Exception {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);

        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer);
        when(mockLecturerMapper.toDto(lecturer)).thenReturn(lecturerDTO);

        mockMvc.perform(get("/api/lecturers/{id}", EXISTENT_PERSON_ID_50005)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_PERSON_ID_50005));

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerMapper, times(1)).toDto(lecturer);
    }

    @Test
    void testGetLecturerShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        mockMvc.perform(get("/api/lecturers/{id}", NONEXISTENT_PERSON_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockLecturerService, times(1)).getById(NONEXISTENT_PERSON_ID);
    }

    @Test
    void testAddLecturerShouldReturnCreated() throws Exception {
        Lecturer newLecturer = new Lecturer().setActive(true);
        LecturerDTO newLecturerDTO = lecturerMapper.toDto(newLecturer);
        String newLecturerJSON = objectMapper.writeValueAsString(newLecturerDTO);

        Lecturer expectedLecturer = lecturerMapper.toEntity(newLecturerDTO).setId(NEW_PERSON_ID);

        when(mockLecturerMapper.toEntity(newLecturerDTO)).thenReturn(newLecturer);
        when(mockLecturerService.add(newLecturer)).thenReturn(expectedLecturer);

        LecturerDTO expectedLecturerDTO = lecturerMapper.toDto(expectedLecturer);
        when(mockLecturerMapper.toDto(expectedLecturer)).thenReturn(expectedLecturerDTO);

        mockMvc.perform(post("/api/lecturers/add")
                .content(newLecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_PERSON_ID));

        verify(mockLecturerMapper, times(1)).toEntity(newLecturerDTO);
        verify(mockLecturerService, times(1)).add(newLecturer);
        verify(mockLecturerMapper, times(1)).toDto(expectedLecturer);
    }

    @Test
    void testUpdateLecturerShouldReturnOK() throws Exception {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005).setEmail(ANOTHER_PERSON_EMAIL);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);

        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(new Lecturer().setId(EXISTENT_PERSON_ID_50005));
        when(mockLecturerMapper.toEntity(lecturerDTO)).thenReturn(lecturer);
        when(mockLecturerService.update(lecturer)).thenReturn(lecturer);
        when(mockLecturerMapper.toDto(lecturer)).thenReturn(lecturerDTO);

        String lecturerJSON = objectMapper.writeValueAsString(lecturerDTO);

        mockMvc.perform(put("/api/lecturers/update/{id}", EXISTENT_PERSON_ID_50005)
                .content(lecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_PERSON_ID_50005))
                .andExpect(jsonPath("$.email").value(ANOTHER_PERSON_EMAIL));

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerMapper, times(1)).toEntity(lecturerDTO);
        verify(mockLecturerService, times(1)).update(lecturer);
        verify(mockLecturerMapper, times(1)).toDto(lecturer);
    }

    @Test
    void testUpdateLecturerShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        Lecturer lecturer = new Lecturer().setId(NONEXISTENT_PERSON_ID);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);
        String lecturerJSON = objectMapper.writeValueAsString(lecturerDTO);

        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        mockMvc.perform(put("/api/lecturers/update/{id}", NONEXISTENT_PERSON_ID)
                .content(lecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockLecturerService, times(1)).getById(NONEXISTENT_PERSON_ID);
    }

    @Test
    void testDeleteLecturerShouldReturnOK() throws Exception {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer);

        mockMvc.perform(delete("/api/lecturers/delete/{id}", EXISTENT_PERSON_ID_50005))
                .andExpect(status().isOk());

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerService, times(1)).delete(lecturer);
    }

    @Test
    void testDeleteLecturerShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/lecturers/delete/{id}", NONEXISTENT_PERSON_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllLecturersShouldReturnOK() throws Exception {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);
        List<Lecturer> lecturers = new ArrayList<>(singletonList(lecturer));

        when(mockLecturerService.getAll()).thenReturn(lecturers);
        when(mockLecturerMapper.toDto(lecturer)).thenReturn(lecturerDTO);

        mockMvc.perform(get("/api/lecturers/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_PERSON_ID_50005));

        verify(mockLecturerService, times(1)).getAll();
        verify(mockLecturerMapper, times(1)).toDto(lecturer);
    }
}
