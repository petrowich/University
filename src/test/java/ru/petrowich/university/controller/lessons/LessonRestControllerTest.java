package ru.petrowich.university.controller.lessons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.dto.lessons.LessonDTO;
import ru.petrowich.university.mapper.lesson.LessonMapper;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.service.LessonService;

import java.time.LocalDate;
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

class LessonRestControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Long NEW_LESSON_ID = 9999999L;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;
    private static final Long NONEXISTENT_LESSON_ID = 9999999L;
    private static final LocalDate ANOTHER_LESSON_DATE = LocalDate.of(2020, 7, 31);
    private static final Integer EXISTENT_COURSE_ID_51 = 51;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();
    private final LessonMapper lessonMapper = new LessonMapper(modelMapper);

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Mock
    LessonService mockLessonService;

    @Mock
    LessonMapper mockLessonMapper;

    @InjectMocks
    LessonRestController lessonRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
        mockMvc = standaloneSetup(lessonRestController).build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetLessonShouldReturnOK() throws Exception {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson);
        when(mockLessonMapper.toDto(lesson)).thenReturn(lessonDTO);

        mockMvc.perform(get("/api/lessons/{id}", EXISTENT_LESSON_ID_5000001)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_LESSON_ID_5000001));

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonMapper, times(1)).toDto(lesson);
    }

    @Test
    void testGetLessonShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);

        mockMvc.perform(get("/api/lessons/{id}", NONEXISTENT_LESSON_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockLessonService, times(1)).getById(NONEXISTENT_LESSON_ID);
    }

    @Test
    void testAddLessonShouldReturnCreated() throws Exception {
        Lesson newLesson = new Lesson();
        LessonDTO newLessonDTO = lessonMapper.toDto(newLesson);
        String newLecturerJSON = objectMapper.writeValueAsString(newLessonDTO);

        Lesson expectedLesson = lessonMapper.toEntity(newLessonDTO).setId(NEW_LESSON_ID);

        when(mockLessonMapper.toEntity(newLessonDTO)).thenReturn(newLesson);
        when(mockLessonService.add(newLesson)).thenReturn(expectedLesson);

        LessonDTO expectedLessonDTO = lessonMapper.toDto(expectedLesson);
        when(mockLessonMapper.toDto(expectedLesson)).thenReturn(expectedLessonDTO);

        mockMvc.perform(post("/api/lessons/add")
                .content(newLecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_LESSON_ID));

        verify(mockLessonMapper, times(1)).toEntity(newLessonDTO);
        verify(mockLessonService, times(1)).add(newLesson);
        verify(mockLessonMapper, times(1)).toDto(expectedLesson);
    }

    @Test
    void testUpdateLessonShouldReturnOK() throws Exception {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001).setCourse(new Course().setId(EXISTENT_COURSE_ID_51));
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(new Lesson().setId(EXISTENT_LESSON_ID_5000001));
        when(mockLessonMapper.toEntity(lessonDTO)).thenReturn(lesson);
        when(mockLessonService.update(lesson)).thenReturn(lesson);
        when(mockLessonMapper.toDto(lesson)).thenReturn(lessonDTO);

        String lessonJSON = objectMapper.writeValueAsString(lessonDTO);

        mockMvc.perform(put("/api/lessons/update/{id}", EXISTENT_LESSON_ID_5000001)
                .content(lessonJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_LESSON_ID_5000001))
                .andExpect(jsonPath("$.courseId").value(EXISTENT_COURSE_ID_51));

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonMapper, times(1)).toEntity(lessonDTO);
        verify(mockLessonService, times(1)).update(lesson);
        verify(mockLessonMapper, times(1)).toDto(lesson);
    }

    @Test
    void testUpdateLessonShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        Lesson lesson = new Lesson().setId(NONEXISTENT_LESSON_ID);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);
        String lessonJSON = objectMapper.writeValueAsString(lessonDTO);

        mockMvc.perform(put("/api/lessons/update/{id}", NONEXISTENT_LESSON_ID)
                .content(lessonJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockLessonService, times(1)).getById(NONEXISTENT_LESSON_ID);
    }

    @Test
    void testDeleteLessonShouldReturnOK() throws Exception {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson);

        mockMvc.perform(delete("/api/lessons/delete/{id}", EXISTENT_LESSON_ID_5000001))
                .andExpect(status().isOk());

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonService, times(1)).delete(lesson);
    }

    @Test
    void testDeleteLessonShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/lessons/delete/{id}", NONEXISTENT_LESSON_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllLessonsShouldReturnOK() throws Exception {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        LessonDTO lessonDTO1 = lessonMapper.toDto(lesson);

        List<Lesson> lessons = new ArrayList<>(singletonList(lesson));

        when(mockLessonService.getAll()).thenReturn(lessons);
        when(mockLessonMapper.toDto(lesson)).thenReturn(lessonDTO1);

        mockMvc.perform(get("/api/lessons/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_LESSON_ID_5000001));

        verify(mockLessonService, times(1)).getAll();
        verify(mockLessonMapper, times(1)).toDto(lesson);
    }
}
