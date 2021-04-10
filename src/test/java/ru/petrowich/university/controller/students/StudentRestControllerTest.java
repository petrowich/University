package ru.petrowich.university.controller.students;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.dto.students.StudentDTO;
import ru.petrowich.university.mapper.students.StudentMapper;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

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

class StudentRestControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer NEW_PERSON_ID = 50007;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final String ANOTHER_PERSON_EMAIL = "another@mail.com";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();
    private final StudentMapper studentMapper = new StudentMapper(modelMapper);

    @Mock
    StudentService mockStudentService;

    @Mock
    StudentMapper mockStudentMapper;

    @InjectMocks
    StudentRestController studentRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
        mockMvc = standaloneSetup(studentRestController).build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetStudentShouldReturnOK() throws Exception {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO = studentMapper.toDto(student);

        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student);
        when(mockStudentMapper.toDto(student)).thenReturn(studentDTO);

        mockMvc.perform(get("/api/students/{id}", EXISTENT_PERSON_ID_50001)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_PERSON_ID_50001));

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentMapper, times(1)).toDto(student);
    }

    @Test
    void testGetStudentShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        mockMvc.perform(get("/api/students/{id}", NONEXISTENT_PERSON_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockStudentService, times(1)).getById(NONEXISTENT_PERSON_ID);
    }

    @Test
    void testAddStudentShouldReturnCreated() throws Exception {
        Student newStudent = new Student();
        StudentDTO newStudentDTO = studentMapper.toDto(newStudent);
        String newStudentJSON = objectMapper.writeValueAsString(newStudentDTO);

        Student expectedStudent = studentMapper.toEntity(newStudentDTO).setId(NEW_PERSON_ID);

        when(mockStudentMapper.toEntity(newStudentDTO)).thenReturn(newStudent);
        when(mockStudentService.add(newStudent)).thenReturn(expectedStudent);

        StudentDTO expectedStudentDTO = studentMapper.toDto(expectedStudent);
        when(mockStudentMapper.toDto(expectedStudent)).thenReturn(expectedStudentDTO);

        mockMvc.perform(post("/api/students/add")
                .content(newStudentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_PERSON_ID));

        verify(mockStudentMapper, times(1)).toEntity(newStudentDTO);
        verify(mockStudentService, times(1)).add(newStudent);
        verify(mockStudentMapper, times(1)).toDto(expectedStudent);
    }

    @Test
    void testUpdateStudentShouldReturnOK() throws Exception {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001).setEmail(ANOTHER_PERSON_EMAIL);
        StudentDTO studentDTO = studentMapper.toDto(student);

        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student);
        when(mockStudentMapper.toEntity(studentDTO)).thenReturn(student);
        when(mockStudentService.update(student)).thenReturn(student);
        when(mockStudentMapper.toDto(student)).thenReturn(studentDTO);

        String studentJSON = objectMapper.writeValueAsString(studentDTO);

        mockMvc.perform(put("/api/students/update/{id}", EXISTENT_PERSON_ID_50001)
                .content(studentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_PERSON_ID_50001))
                .andExpect(jsonPath("$.email").value(ANOTHER_PERSON_EMAIL));

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentMapper, times(1)).toEntity(studentDTO);
        verify(mockStudentService, times(1)).update(student);
        verify(mockStudentMapper, times(1)).toDto(student);
    }

    @Test
    void testUpdateStudentShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        Student student = new Student().setId(NONEXISTENT_PERSON_ID);
        StudentDTO studentDTO = studentMapper.toDto(student);
        String studentJSON = objectMapper.writeValueAsString(studentDTO);

        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        mockMvc.perform(put("/api/students/update/{id}", NONEXISTENT_PERSON_ID)
                .content(studentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockStudentService, times(1)).getById(NONEXISTENT_PERSON_ID);
    }

    @Test
    void testDeleteStudentShouldReturnOK() throws Exception {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student);

        mockMvc.perform(delete("/api/students/delete/{id}", EXISTENT_PERSON_ID_50001))
                .andExpect(status().isOk());

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentService, times(1)).delete(student);
    }

    @Test
    void testDeleteStudentShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/students/delete/{id}", NONEXISTENT_PERSON_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllStudentsShouldReturnOK() throws Exception {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO = studentMapper.toDto(student);

        List<Student> students = new ArrayList<>(singletonList(student));

        when(mockStudentService.getAll()).thenReturn(students);
        when(mockStudentMapper.toDto(student)).thenReturn(studentDTO);

        mockMvc.perform(get("/api/students/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_PERSON_ID_50001));

        verify(mockStudentService, times(1)).getAll();
        verify(mockStudentMapper, times(1)).toDto(student);
    }
}
