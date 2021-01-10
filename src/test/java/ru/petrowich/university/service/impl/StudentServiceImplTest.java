package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.CourseRepository;
import ru.petrowich.university.repository.GroupRepository;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.repository.StudentRepository;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Course;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

class StudentServiceImplTest {
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer PERSON_ID_50003 = 50003;
    private static final Integer PERSON_ID_50005 = 50005;
    private static final String PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String PERSON_EMAIL_50002 = "obval.zaboev@university.edu";
    private static final String PERSON_EMAIL_50003 = "record.nadoev@university.edu";
    private static final String PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final String GROUP_NAME_501 = "AA-01";
    private static final String GROUP_NAME_502 = "BB-02";
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final String COURSE_NAME_51 = "math";
    private static final String COURSE_NAME_52 = "biology";
    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long LESSON_ID_5000002 = 5000002L;
    private static final Long LESSON_ID_5000003 = 5000003L;

    private final Lesson firstLesson = new Lesson().setId(LESSON_ID_5000001);
    private final Lesson secondLesson = new Lesson().setId(LESSON_ID_5000002);
    private final Lesson thirdLesson = new Lesson().setId(LESSON_ID_5000003);

    private final Lecturer lecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(lecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(lecturer).setActive(true);

    private final Group firstGroup = new Group().setId(GROUP_ID_501).setName(GROUP_NAME_501).setActive(true);
    private final Group secondGroup = new Group().setId(GROUP_ID_502).setName(GROUP_NAME_502).setActive(true);

    private final Student firstStudent = new Student().setId(PERSON_ID_50001).setGroup(firstGroup).setEmail(PERSON_EMAIL_50001).setActive(true);
    private final Student secondStudent = new Student().setId(PERSON_ID_50002).setGroup(firstGroup).setEmail(PERSON_EMAIL_50002).setActive(true);
    private final Student thirdStudent = new Student().setId(PERSON_ID_50003).setGroup(secondGroup).setEmail(PERSON_EMAIL_50003).setActive(false);

    private final List<Course> firstStudentCourses = new ArrayList<>();
    private final List<Course> secondStudentCourses = new ArrayList<>();

    private final List<Lesson> firstStudentLessons = new ArrayList<>();
    private final List<Lesson> secondStudentLessons = new ArrayList<>();

    private AutoCloseable autoCloseable;

    {
        firstStudentCourses.add(firstCourse);
        firstStudentCourses.add(secondCourse);
        firstStudent.setCourses(firstStudentCourses);

        firstStudentLessons.add(firstLesson);
        firstStudentLessons.add(secondLesson);
        secondStudentLessons.add(thirdLesson);
        firstStudent.setLessons(firstStudentLessons);

        secondStudentCourses.add(secondCourse);
        secondStudent.setCourses(secondStudentCourses);

        secondStudentLessons.add(thirdLesson);
        firstStudent.setLessons(firstStudentLessons);
    }

    @Mock
    private StudentRepository mockStudentRepository;

    @Mock
    private GroupRepository mockGroupRepository;

    @Mock
    private CourseRepository mockCourseRepository;

    @Mock
    private LessonRepository mockLessonRepository;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @BeforeEach
    private void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnStudentWhenStudentIdPassed() {
        when(mockStudentRepository.findById(PERSON_ID_50001)).thenReturn(firstStudent);
        when(mockGroupRepository.findById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockLessonRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);

        Student actual = studentServiceImpl.getById(PERSON_ID_50001);

        verify(mockStudentRepository, times(1)).findById(PERSON_ID_50001);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50001);

        assertThatObject(actual).isEqualToComparingFieldByField(firstStudent);
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentIdPassed() {
        when(mockStudentRepository.findById(-1)).thenReturn(null);
        Student actual = studentServiceImpl.getById(-1);

        verify(mockStudentRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNullPassed() {
        when(mockStudentRepository.findById(null)).thenReturn(null);
        Student actual = studentServiceImpl.getById(null);

        verify(mockStudentRepository, times(1)).findById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedStudent() {
        doNothing().when(mockStudentRepository).save(firstStudent);
        studentServiceImpl.add(firstStudent);
        verify(mockStudentRepository, times(1)).save(firstStudent);
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockStudentRepository).save(null);
        studentServiceImpl.add(null);
        verify(mockStudentRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedStudent() {
        doNothing().when(mockStudentRepository).update(firstStudent);
        studentServiceImpl.update(firstStudent);
        verify(mockStudentRepository, times(1)).update(firstStudent);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockStudentRepository).update(null);
        studentServiceImpl.update(null);
        verify(mockStudentRepository, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedStudent() {
        doNothing().when(mockStudentRepository).delete(firstStudent);
        studentServiceImpl.delete(firstStudent);
        verify(mockStudentRepository, times(1)).delete(firstStudent);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockStudentRepository).delete(null);
        studentServiceImpl.delete(null);
        verify(mockStudentRepository, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnStudentsList() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentRepository.findAll()).thenReturn(expected);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonRepository.findByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getAll();

        verify(mockStudentRepository, times(1)).findAll();
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50003);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testSetGetByGroupIdShouldReturnStudentsListWhenGroupIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentRepository.findByGroupId(GROUP_ID_502)).thenReturn(expected);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonRepository.findByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getByGroupId(GROUP_ID_502);

        verify(mockStudentRepository, times(1)).findByGroupId(GROUP_ID_502);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50003);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByGroupIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        when(mockStudentRepository.findByGroupId(null)).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getByGroupId(null);

        verify(mockStudentRepository, times(1)).findByGroupId(null);
        assertEquals(expected, actual, "empty student list should be returned");
    }

    @Test
    void testGetByCourseIdShouldReturnStudentsListWhenCourseIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentRepository.findByGroupId(COURSE_ID_51)).thenReturn(expected);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonRepository.findByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getByGroupId(COURSE_ID_51);

        verify(mockStudentRepository, times(1)).findByGroupId(COURSE_ID_51);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50003);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByCourseIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        when(mockStudentRepository.findByCourseId(null)).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getByCourseId(null);

        verify(mockStudentRepository, times(1)).findByCourseId(null);
        assertEquals(expected, actual, "empty student list should be returned");
    }

    @Test
    void testGetByLessonIdShouldReturnStudentsListWhenLessonIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentRepository.findByLessonId(LESSON_ID_5000001)).thenReturn(expected);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseRepository.findByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonRepository.findByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonRepository.findByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonRepository.findByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getByLessonId(LESSON_ID_5000001);

        verify(mockStudentRepository, times(1)).findByLessonId(LESSON_ID_5000001);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50003);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50001);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50002);
        verify(mockLessonRepository, times(1)).findByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByLessonIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        when(mockStudentRepository.findByLessonId(null)).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getByLessonId(null);

        verify(mockStudentRepository, times(1)).findByLessonId(null);
        assertEquals(expected, actual, "empty student list should be returned");
    }
}
