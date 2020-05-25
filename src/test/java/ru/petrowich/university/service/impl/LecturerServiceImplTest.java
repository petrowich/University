package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.LessonDAO;
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
import static org.mockito.MockitoAnnotations.initMocks;

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

    private final List<Course> firstLecturerCourses = new ArrayList<>();

    private final List<Lesson> firstLecturerLessons = new ArrayList<>();

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
    private LecturerDAO mockLecturerDAO;

    @Mock
    private CourseDAO mockCourseDAO;

    @Mock
    private LessonDAO mockLessonDAO;

    @InjectMocks
    private LecturerServiceImpl lecturerServiceImpl;

    @BeforeEach
    private void setUp() {
        initMocks(this);
    }

    @Test
    void testGetByIdShouldReturnLecturerWhenLecturerIdPassed() {
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(firstLecturer);
        when(mockCourseDAO.getByAuthorId(PERSON_ID_50005)).thenReturn(firstLecturerCourses);
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50005)).thenReturn(firstLecturerLessons);

        Lecturer actual = lecturerServiceImpl.getById(PERSON_ID_50005);

        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockCourseDAO, times(1)).getByAuthorId(PERSON_ID_50005);
        verify(mockLessonDAO, times(1)).getByLecturerId(PERSON_ID_50005);

        assertThatObject(actual).isEqualToComparingFieldByField(firstLecturer);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockLecturerDAO.getById(-1)).thenReturn(null);
        Lecturer actual = lecturerServiceImpl.getById(-1);

        verify(mockLecturerDAO, times(1)).getById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        when(mockLecturerDAO.getById(null)).thenReturn(null);
        Lecturer actual = lecturerServiceImpl.getById(null);

        verify(mockLecturerDAO, times(1)).getById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedLecturer() {
        doNothing().when(mockLecturerDAO).add(firstLecturer);
        lecturerServiceImpl.add(firstLecturer);
        verify(mockLecturerDAO, times(1)).add(firstLecturer);
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockLecturerDAO).add(null);
        lecturerServiceImpl.add(null);
        verify(mockLecturerDAO, times(1)).add(null);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedLecturer() {
        doNothing().when(mockLecturerDAO).update(firstLecturer);
        lecturerServiceImpl.update(firstLecturer);
        verify(mockLecturerDAO, times(1)).update(firstLecturer);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockLecturerDAO).update(null);
        lecturerServiceImpl.update(null);
        verify(mockLecturerDAO, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedLecturer() {
        doNothing().when(mockLecturerDAO).delete(firstLecturer);
        lecturerServiceImpl.delete(firstLecturer);
        verify(mockLecturerDAO, times(1)).delete(firstLecturer);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockLecturerDAO).delete(null);
        lecturerServiceImpl.delete(null);
        verify(mockLecturerDAO, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnLecturerList() {
        List<Lecturer> expected = new ArrayList<>();
        expected.add(firstLecturer);
        expected.add(secondLecturer);

        when(mockLecturerDAO.getAll()).thenReturn(expected);
        when(mockCourseDAO.getByAuthorId(PERSON_ID_50005)).thenReturn(firstLecturerCourses);
        when(mockCourseDAO.getByAuthorId(PERSON_ID_50006)).thenReturn(new ArrayList<>());
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50005)).thenReturn(firstLecturerLessons);
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50006)).thenReturn(new ArrayList<>());

        List<Lecturer> actual = lecturerServiceImpl.getAll();

        verify(mockLecturerDAO, times(1)).getAll();
        verify(mockCourseDAO, times(1)).getByAuthorId(PERSON_ID_50005);
        verify(mockCourseDAO, times(1)).getByAuthorId(PERSON_ID_50006);
        verify(mockLessonDAO, times(1)).getByLecturerId(PERSON_ID_50005);
        verify(mockLessonDAO, times(1)).getByLecturerId(PERSON_ID_50006);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
