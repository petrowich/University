package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.LecturerRepository;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

class LecturerServiceImplTest {
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;
    private static final String PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final String PERSON_EMAIL_50006 = "ushat.pomoev@university.edu";
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final String COURSE_NAME_51 = "math";
    private static final String COURSE_NAME_52 = "biology";
    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long LESSON_ID_5000002 = 5000002L;
    private static final Long LESSON_ID_5000003 = 5000003L;

    private final Lesson fistLesson = new Lesson().setId(LESSON_ID_5000001);
    private final Lesson secondLesson = new Lesson().setId(LESSON_ID_5000002);
    private final Lesson thirdLesson = new Lesson().setId(LESSON_ID_5000003);

    private final Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);
    private final Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50006).setEmail(PERSON_EMAIL_50006).setActive(false);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(firstLecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(firstLecturer).setActive(true);

    private AutoCloseable autoCloseable;

    {
        List<Course> firstLecturerCourses = new ArrayList<>();
        firstLecturerCourses.add(firstCourse);
        firstLecturerCourses.add(secondCourse);
        firstLecturer.setCourses(firstLecturerCourses);

        List<Lesson> firstLecturerLessons = new ArrayList<>();
        firstLecturerLessons.add(fistLesson);
        firstLecturerLessons.add(secondLesson);
        firstLecturerLessons.add(thirdLesson);
        firstLecturer.setLessons(firstLecturerLessons);
    }

    @Mock
    private LecturerRepository mockLecturerRepository;

    @InjectMocks
    private LecturerServiceImpl lecturerServiceImpl;

    @BeforeEach
    private void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnLecturerWhenLecturerIdPassed() {
        when(mockLecturerRepository.findById(PERSON_ID_50005)).thenReturn(firstLecturer);

        Lecturer actual = lecturerServiceImpl.getById(PERSON_ID_50005);

        verify(mockLecturerRepository, times(1)).findById(PERSON_ID_50005);

        assertThat(actual).usingRecursiveComparison().isEqualTo(firstLecturer);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockLecturerRepository.findById(-1)).thenReturn(null);
        Lecturer actual = lecturerServiceImpl.getById(-1);

        verify(mockLecturerRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        when(mockLecturerRepository.findById(null)).thenReturn(null);
        Lecturer actual = lecturerServiceImpl.getById(null);

        verify(mockLecturerRepository, times(1)).findById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedLecturer() {
        doNothing().when(mockLecturerRepository).save(firstLecturer);
        lecturerServiceImpl.add(firstLecturer);
        verify(mockLecturerRepository, times(1)).save(firstLecturer);
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockLecturerRepository).save(null);
        lecturerServiceImpl.add(null);
        verify(mockLecturerRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedLecturer() {
        doNothing().when(mockLecturerRepository).update(firstLecturer);
        lecturerServiceImpl.update(firstLecturer);
        verify(mockLecturerRepository, times(1)).update(firstLecturer);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockLecturerRepository).update(null);
        lecturerServiceImpl.update(null);
        verify(mockLecturerRepository, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedLecturer() {
        doNothing().when(mockLecturerRepository).delete(firstLecturer);
        lecturerServiceImpl.delete(firstLecturer);
        verify(mockLecturerRepository, times(1)).delete(firstLecturer);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockLecturerRepository).delete(null);
        lecturerServiceImpl.delete(null);
        verify(mockLecturerRepository, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnLecturerList() {
        List<Lecturer> expected = new ArrayList<>();
        expected.add(firstLecturer);
        expected.add(secondLecturer);

        when(mockLecturerRepository.findAll()).thenReturn(expected);

        List<Lecturer> actual = lecturerServiceImpl.getAll();

        verify(mockLecturerRepository, times(1)).findAll();

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
