package ru.petrowich.university.controller.lessons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.petrowich.university.dto.lessons.LessonDTO;
import ru.petrowich.university.mapper.lesson.LessonMapper;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.service.LessonService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class LessonRestControllerTest {
    private static final Long NEW_LESSON_ID = 9999999L;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;
    private static final Long NONEXISTENT_LESSON_ID = 9999999L;
    private static final LocalDate ANOTHER_LESSON_DATE = LocalDate.of(2020, 7, 1);

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final LessonMapper lessonMapper = new LessonMapper(modelMapper);

    @Mock
    LessonService mockLessonService;

    @Mock
    LessonMapper mockLessonMapper;

    @InjectMocks
    LessonRestController lessonRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetLessonShouldReturnOK() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson);
        when(mockLessonMapper.toDto(lesson)).thenReturn(lessonDTO);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.getLesson(EXISTENT_LESSON_ID_5000001);

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonMapper, times(1)).toDto(lesson);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(lessonDTO);
    }

    @Test
    void testGetLessonShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<LessonDTO> responseEntity = lessonRestController.getLesson(null);

        verify(mockLessonService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetLessonShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.getLesson(NONEXISTENT_LESSON_ID);

        verify(mockLessonService, times(1)).getById(NONEXISTENT_LESSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddLessonShouldReturnCreated() {
        Lesson newLesson = new Lesson();
        LessonDTO newLessonDTO = lessonMapper.toDto(newLesson);
        Lesson expectedLesson = lessonMapper.toEntity(newLessonDTO).setId(NEW_LESSON_ID);

        when(mockLessonMapper.toEntity(newLessonDTO)).thenReturn(newLesson);
        when(mockLessonService.add(newLesson)).thenReturn(expectedLesson);

        LessonDTO expectedLessonDTO = lessonMapper.toDto(expectedLesson);
        when(mockLessonMapper.toDto(expectedLesson)).thenReturn(expectedLessonDTO);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.addLesson(newLessonDTO);

        verify(mockLessonMapper, times(1)).toEntity(newLessonDTO);
        verify(mockLessonService, times(1)).add(newLesson);
        verify(mockLessonMapper, times(1)).toDto(expectedLesson);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLessonDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateLessonShouldReturnOK() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);

        LessonDTO lessonDTO = lessonMapper.toDto(lesson);
        Lesson expectedLesson = lessonMapper.toEntity(lessonDTO).setDate(ANOTHER_LESSON_DATE);

        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson);
        when(mockLessonMapper.toEntity(lessonDTO)).thenReturn(lesson);
        when(mockLessonService.update(lesson)).thenReturn(expectedLesson);

        LessonDTO expectedLessonDTO = lessonMapper.toDto(expectedLesson);
        when(mockLessonMapper.toDto(expectedLesson)).thenReturn(expectedLessonDTO);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.updateLesson(lessonDTO, EXISTENT_LESSON_ID_5000001);

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonMapper, times(1)).toEntity(lessonDTO);
        verify(mockLessonService, times(1)).update(lesson);
        verify(mockLessonMapper, times(1)).toDto(expectedLesson);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLessonDTO);
    }

    @Test
    void testUpdateLessonShouldReturnBadRequestWhenNullIdPassed() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.updateLesson(lessonDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateLessonShouldReturnNotFoundWhenNonexistentIdPassed() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.updateLesson(lessonDTO, NONEXISTENT_LESSON_ID);

        verify(mockLessonService, times(1)).getById(NONEXISTENT_LESSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteLessonShouldReturnOK() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.deleteLesson(EXISTENT_LESSON_ID_5000001);

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonService, times(1)).delete(lesson);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteLessonShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<LessonDTO> responseEntity = lessonRestController.deleteLesson(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteLessonShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);
        ResponseEntity<LessonDTO> responseEntity = lessonRestController.deleteLesson(NONEXISTENT_LESSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllLessonsShouldReturnOK() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        LessonDTO lessonDTO1 = lessonMapper.toDto(lesson);

        List<Lesson> lessons = new ArrayList<>(singletonList(lesson));

        when(mockLessonService.getAll()).thenReturn(lessons);
        when(mockLessonMapper.toDto(lesson)).thenReturn(lessonDTO1);

        ResponseEntity<List<LessonDTO>> responseEntity = lessonRestController.getAllLessons();

        verify(mockLessonService, times(1)).getAll();
        verify(mockLessonMapper, times(1)).toDto(lesson);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<LessonDTO> expectedLessonDTOList = new ArrayList<>(singletonList(lessonDTO1));
        assertThat(expectedLessonDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
