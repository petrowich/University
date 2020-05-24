package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.dao.StudentDAO;
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
import static org.mockito.MockitoAnnotations.initMocks;

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
    private StudentDAO mockStudentDAO;

    @Mock
    private GroupDAO mockGroupDAO;

    @Mock
    private CourseDAO mockCourseDAO;

    @Mock
    private LessonDAO mockLessonDAO;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @BeforeEach
    private void setUp() {
        initMocks(this);
    }

    @Test
    void testGetByIdShouldReturnStudentWhenStudentIdPassed() {
        when(mockStudentDAO.getById(PERSON_ID_50001)).thenReturn(firstStudent);
        when(mockGroupDAO.getById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockLessonDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);

        Student actual = studentServiceImpl.getById(PERSON_ID_50001);

        verify(mockStudentDAO, times(1)).getById(PERSON_ID_50001);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50001);

        assertThatObject(actual).isEqualToComparingFieldByField(firstStudent);
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentIdPassed() {
        when(mockStudentDAO.getById(-1)).thenReturn(null);
        Student actual = studentServiceImpl.getById(-1);

        verify(mockStudentDAO, times(1)).getById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNullPassed() {
        when(mockStudentDAO.getById(null)).thenReturn(null);
        Student actual = studentServiceImpl.getById(null);

        verify(mockStudentDAO, times(1)).getById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedStudent() {
        doNothing().when(mockStudentDAO).add(firstStudent);
        studentServiceImpl.add(firstStudent);
        verify(mockStudentDAO, times(1)).add(firstStudent);
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockStudentDAO).add(null);
        studentServiceImpl.add(null);
        verify(mockStudentDAO, times(1)).add(null);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedStudent() {
        doNothing().when(mockStudentDAO).update(firstStudent);
        studentServiceImpl.update(firstStudent);
        verify(mockStudentDAO, times(1)).update(firstStudent);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockStudentDAO).update(null);
        studentServiceImpl.update(null);
        verify(mockStudentDAO, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedStudent() {
        doNothing().when(mockStudentDAO).delete(firstStudent);
        studentServiceImpl.delete(firstStudent);
        verify(mockStudentDAO, times(1)).delete(firstStudent);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockStudentDAO).delete(null);
        studentServiceImpl.delete(null);
        verify(mockStudentDAO, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnStudentsList() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentDAO.getAll()).thenReturn(expected);
        when(mockGroupDAO.getById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockGroupDAO.getById(GROUP_ID_502)).thenReturn(secondGroup);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getAll();

        verify(mockStudentDAO, times(1)).getAll();
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_501);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_502);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50003);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testSetGetByGroupIdShouldReturnStudentsListWhenGroupIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentDAO.getByGroupId(GROUP_ID_502)).thenReturn(expected);
        when(mockGroupDAO.getById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockGroupDAO.getById(GROUP_ID_502)).thenReturn(secondGroup);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getByGroupId(GROUP_ID_502);

        verify(mockStudentDAO, times(1)).getByGroupId(GROUP_ID_502);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_501);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_502);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50003);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByGroupIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        when(mockStudentDAO.getByGroupId(null)).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getByGroupId(null);

        verify(mockStudentDAO, times(1)).getByGroupId(null);
        assertEquals(expected, actual, "empty student list should be returned");
    }

    @Test
    void testGetByCourseIdShouldReturnStudentsListWhenCourseIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentDAO.getByGroupId(COURSE_ID_51)).thenReturn(expected);
        when(mockGroupDAO.getById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockGroupDAO.getById(GROUP_ID_502)).thenReturn(secondGroup);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getByGroupId(COURSE_ID_51);

        verify(mockStudentDAO, times(1)).getByGroupId(COURSE_ID_51);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_501);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_502);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50003);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByCourseIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        when(mockStudentDAO.getByCourseId(null)).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getByCourseId(null);

        verify(mockStudentDAO, times(1)).getByCourseId(null);
        assertEquals(expected, actual, "empty student list should be returned");
    }

    @Test
    void testGetByLessonIdShouldReturnStudentsListWhenLessonIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(expected);
        when(mockGroupDAO.getById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockGroupDAO.getById(GROUP_ID_502)).thenReturn(secondGroup);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentCourses);
        when(mockCourseDAO.getByStudentId(PERSON_ID_50003)).thenReturn(new ArrayList<>());
        when(mockLessonDAO.getByStudentId(PERSON_ID_50001)).thenReturn(firstStudentLessons);
        when(mockLessonDAO.getByStudentId(PERSON_ID_50002)).thenReturn(secondStudentLessons);
        when(mockLessonDAO.getByLecturerId(PERSON_ID_50003)).thenReturn(new ArrayList<>());

        List<Student> actual = studentServiceImpl.getByLessonId(LESSON_ID_5000001);

        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_501);
        verify(mockGroupDAO, times(1)).getById(GROUP_ID_502);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50003);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50002);
        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50003);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByLessonIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        when(mockStudentDAO.getByLessonId(null)).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getByLessonId(null);

        verify(mockStudentDAO, times(1)).getByLessonId(null);
        assertEquals(expected, actual, "empty student list should be returned");
    }
}
