package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

class LessonServiceImplTest {
    private static final Integer PERSON_ID_50005 = 50005;
    private static final String PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final String COURSE_NAME_51 = "math";
    private static final String COURSE_NAME_52 = "biology";
    private static final Integer TIME_SLOT_ID = 1;
    private static final String TIME_SLOT_NAME = "first lesson";
    private static final LocalDate TIME_SLOT_DATE = LocalDate.of(2025, 12, 31);
    private static final LocalTime TIME_SLOT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime TIME_SLOT_END_TIME = LocalTime.of(9, 30);
    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long LESSON_ID_5000002 = 5000002L;
    private static final Long LESSON_ID_5000003 = 5000003L;

    private static final Set<ConstraintViolation<Lesson>> violations = new HashSet<>();

    private final Lecturer lecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(lecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(lecturer).setActive(true);

    private final TimeSlot timeSlot = new TimeSlot().setId(TIME_SLOT_ID).setName(TIME_SLOT_NAME).setStartTime(TIME_SLOT_START_TIME).setEndTime(TIME_SLOT_END_TIME);

    private final Lesson firstLesson = new Lesson().setId(LESSON_ID_5000001).setTimeSlot(timeSlot).setLecturer(lecturer).setCourse(firstCourse).setDate(TIME_SLOT_DATE).setStartTime(TIME_SLOT_START_TIME).setEndTime(TIME_SLOT_END_TIME);
    private final Lesson secondLesson = new Lesson().setId(LESSON_ID_5000002).setTimeSlot(timeSlot).setLecturer(lecturer).setCourse(secondCourse);
    private final Lesson thirdLesson = new Lesson().setId(LESSON_ID_5000003).setTimeSlot(timeSlot).setLecturer(lecturer).setCourse(new Course());

    private AutoCloseable autoCloseable;

    @Mock
    private LessonRepository mockLessonRepository;

    @Mock
    private Validator mockValidator;

    @InjectMocks
    private LessonServiceImpl lessonServiceImpl;

    @BeforeEach
    private void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnLessonWhenLessonIdPassed() {
        Optional<Lesson> optionalFirstLesson = Optional.of(firstLesson);
        when(mockLessonRepository.findById(LESSON_ID_5000001)).thenReturn(optionalFirstLesson);

        Lesson actual = lessonServiceImpl.getById(LESSON_ID_5000001);

        verify(mockLessonRepository, times(1)).findById(LESSON_ID_5000001);

        assertThat(actual).usingRecursiveComparison().isEqualTo(firstLesson);
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentIdPassed() {
        when(mockLessonRepository.findById(-1L)).thenReturn(Optional.empty());
        Lesson actual = lessonServiceImpl.getById(-1L);

        verify(mockLessonRepository, times(1)).findById(-1L);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> lessonServiceImpl.getById(null), "GetById(null) should throw IllegalArgumentException");
        verify(mockLessonRepository, times(0)).findById(null);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedLesson() {
        when(mockValidator.validate(firstLesson)).thenReturn(violations);
        lessonServiceImpl.add(firstLesson);

        verify(mockValidator, times(1)).validate(firstLesson);
        verify(mockLessonRepository, times(1)).save(firstLesson);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        when(mockValidator.validate(firstLesson)).thenReturn(violations);
        assertThrows(NullPointerException.class, () -> lessonServiceImpl.add(null), "add(null) should throw NullPointerException");

        verify(mockValidator, times(0)).validate(firstLesson);
        verify(mockLessonRepository, times(0)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedLesson() {
        when(mockValidator.validate(firstLesson)).thenReturn(violations);
        lessonServiceImpl.update(firstLesson);

        verify(mockValidator, times(1)).validate(firstLesson);
        verify(mockLessonRepository, times(1)).save(firstLesson);
    }

    @Test
    void testUpdateShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        when(mockValidator.validate(firstLesson)).thenReturn(violations);
        assertThrows(IllegalArgumentException.class, () -> lessonServiceImpl.update(null), "update(null) should throw IllegalArgumentException");

        verify(mockValidator, times(0)).validate(firstLesson);
        verify(mockLessonRepository, times(0)).save(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryDeleteWithPassedLesson() {
        doNothing().when(mockLessonRepository).delete(firstLesson);
        lessonServiceImpl.delete(firstLesson);
        verify(mockLessonRepository, times(1)).delete(firstLesson);
    }

    @Test
    void testDeleteShouldInvokeRepositoryDeleteWithPassedNull() {
        doNothing().when(mockLessonRepository).delete(null);
        lessonServiceImpl.delete(null);
        verify(mockLessonRepository, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnLessonsList() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(firstLesson);
        expected.add(secondLesson);
        expected.add(thirdLesson);

        when(mockLessonRepository.findAll()).thenReturn(expected);
        List<Lesson> actual = lessonServiceImpl.getAll();

        verify(mockLessonRepository, times(1)).findAll();
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
