package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.dao.TimeSlotDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.TimeSlot;

import java.time.LocalTime;
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

class LessonServiceImplTest {
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer PERSON_ID_50005 = 50005;
    private static final String PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String PERSON_EMAIL_50002 = "obval.zaboev@university.edu";
    private static final String PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final String GROUP_NAME_501 = "AA-01";
    private static final String GROUP_NAME_502 = "BB-02";
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final String COURSE_NAME_51 = "math";
    private static final String COURSE_NAME_52 = "biology";
    private static final Integer TIME_SLOT_ID = 1;
    private static final String TIME_SLOT_NAME = "first lesson";
    private static final LocalTime TIME_SLOT_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime TIME_SLOT_END_TIME = LocalTime.of(9, 30);
    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long LESSON_ID_5000002 = 5000002L;
    private static final Long LESSON_ID_5000003 = 5000003L;

    private final Group firstGroup = new Group().setId(GROUP_ID_501).setName(GROUP_NAME_501).setActive(true);
    private final Group secondGroup = new Group().setId(GROUP_ID_502).setName(GROUP_NAME_502).setActive(true);

    private final List<Group> firstLessonGroups = new ArrayList<>();
    private final List<Group> secondLessonGroups = new ArrayList<>();

    private final Student firstStudent = new Student().setId(PERSON_ID_50001).setGroup(firstGroup).setEmail(PERSON_EMAIL_50001).setActive(true);
    private final Student secondStudent = new Student().setId(PERSON_ID_50002).setGroup(firstGroup).setEmail(PERSON_EMAIL_50002).setActive(true);

    private final List<Student> firstLessonStudents = new ArrayList<>();
    private final List<Student> secondLessonStudents = new ArrayList<>();

    private final Lecturer lecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(lecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(lecturer).setActive(true);

    private final TimeSlot timeSlot = new TimeSlot().setId(TIME_SLOT_ID).setName(TIME_SLOT_NAME).setStartTime(TIME_SLOT_START_TIME).setEndTime(TIME_SLOT_END_TIME);

    private final Lesson firstLesson = new Lesson().setId(LESSON_ID_5000001).setTimeSlot(timeSlot).setLecturer(lecturer).setCourse(firstCourse);
    private final Lesson secondLesson = new Lesson().setId(LESSON_ID_5000002).setTimeSlot(timeSlot).setLecturer(lecturer).setCourse(secondCourse);
    private final Lesson thirdLesson = new Lesson().setId(LESSON_ID_5000003).setTimeSlot(timeSlot).setLecturer(lecturer).setCourse(new Course());

    private AutoCloseable autoCloseable;

    {
        firstLessonGroups.add(firstGroup);
        firstLessonGroups.add(secondGroup);
        secondLessonGroups.add(secondGroup);

        firstLessonStudents.add(firstStudent);
        firstLessonStudents.add(secondStudent);

        secondLessonStudents.add(secondStudent);
    }

    @Mock
    private LessonDAO mockLessonDAO;

    @Mock
    private CourseDAO mockCourseDAO;

    @Mock
    private LecturerDAO mockLecturerDAO;

    @Mock
    private TimeSlotDAO mockTimeSlotDAO;

    @Mock
    private GroupDAO mockGroupDAO;

    @Mock
    private StudentDAO mockStudentDAO;

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
        when(mockLessonDAO.getById(LESSON_ID_5000001)).thenReturn(firstLesson);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonGroups);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonStudents);
        when(mockTimeSlotDAO.getById(TIME_SLOT_ID)).thenReturn(timeSlot);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(lecturer);
        when(mockCourseDAO.getById(COURSE_ID_51)).thenReturn(firstCourse);

        Lesson actual = lessonServiceImpl.getById(LESSON_ID_5000001);

        verify(mockLessonDAO, times(1)).getById(LESSON_ID_5000001);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockTimeSlotDAO, times(1)).getById(TIME_SLOT_ID);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_51);

        assertThatObject(actual).isEqualToComparingFieldByField(firstLesson);
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentIdPassed() {
        when(mockLessonDAO.getById(-1L)).thenReturn(null);
        Lesson actual = lessonServiceImpl.getById(-1L);

        verify(mockLessonDAO, times(1)).getById(-1L);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenNullPassed() {
        when(mockLessonDAO.getById(null)).thenReturn(null);
        Lesson actual = lessonServiceImpl.getById(null);

        verify(mockLessonDAO, times(1)).getById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedLesson() {
        doNothing().when(mockLessonDAO).add(firstLesson);
        lessonServiceImpl.add(firstLesson);
        verify(mockLessonDAO, times(1)).add(firstLesson);
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockLessonDAO).add(null);
        lessonServiceImpl.add(null);
        verify(mockLessonDAO, times(1)).add(null);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedLesson() {
        doNothing().when(mockLessonDAO).update(firstLesson);
        lessonServiceImpl.update(firstLesson);
        verify(mockLessonDAO, times(1)).update(firstLesson);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockLessonDAO).update(null);
        lessonServiceImpl.update(null);
        verify(mockLessonDAO, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedLesson() {
        doNothing().when(mockLessonDAO).delete(firstLesson);
        lessonServiceImpl.delete(firstLesson);
        verify(mockLessonDAO, times(1)).delete(firstLesson);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockLessonDAO).delete(null);
        lessonServiceImpl.delete(null);
        verify(mockLessonDAO, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnLessonsList() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(firstLesson);
        expected.add(secondLesson);
        expected.add(thirdLesson);

        when(mockLessonDAO.getAll()).thenReturn(expected);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonGroups);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000002)).thenReturn(secondLessonGroups);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000003)).thenReturn(new ArrayList<>());
        when(mockGroupDAO.getByLessonId(null)).thenReturn(null);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonStudents);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000002)).thenReturn(secondLessonStudents);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000003)).thenReturn(new ArrayList<>());
        when(mockStudentDAO.getByLessonId(null)).thenReturn(null);
        when(mockCourseDAO.getById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockCourseDAO.getById(COURSE_ID_52)).thenReturn(secondCourse);
        when(mockCourseDAO.getById(null)).thenReturn(null);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(lecturer);
        when(mockLecturerDAO.getById(null)).thenReturn(null);
        when(mockTimeSlotDAO.getById(TIME_SLOT_ID)).thenReturn(timeSlot);
        when(mockTimeSlotDAO.getById(null)).thenReturn(null);

        List<Lesson> actual = lessonServiceImpl.getAll();

        verify(mockLessonDAO, times(1)).getAll();
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000002);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000003);
        verify(mockGroupDAO, times(0)).getByLessonId(null);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000002);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000003);
        verify(mockStudentDAO, times(0)).getByLessonId(null);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_51);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_52);
        verify(mockCourseDAO, times(0)).getById(null);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockLecturerDAO, times(0)).getById(null);
        verify(mockTimeSlotDAO, times(1)).getById(TIME_SLOT_ID);
        verify(mockTimeSlotDAO, times(0)).getById(null);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByLecturerIdShouldReturnLessonsListWhenLessonIdPassed() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(firstLesson);
        expected.add(secondLesson);
        expected.add(thirdLesson);

        when(mockLessonDAO.getByLecturerId(PERSON_ID_50005)).thenReturn(expected);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonGroups);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000002)).thenReturn(secondLessonGroups);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000003)).thenReturn(new ArrayList<>());
        when(mockGroupDAO.getByLessonId(null)).thenReturn(null);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonStudents);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000002)).thenReturn(secondLessonStudents);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000003)).thenReturn(new ArrayList<>());
        when(mockStudentDAO.getByLessonId(null)).thenReturn(null);
        when(mockCourseDAO.getById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockCourseDAO.getById(COURSE_ID_52)).thenReturn(secondCourse);
        when(mockCourseDAO.getById(null)).thenReturn(null);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(lecturer);
        when(mockLecturerDAO.getById(null)).thenReturn(null);
        when(mockTimeSlotDAO.getById(TIME_SLOT_ID)).thenReturn(timeSlot);
        when(mockTimeSlotDAO.getById(null)).thenReturn(null);

        List<Lesson> actual = lessonServiceImpl.getByLecturerId(PERSON_ID_50005);

        verify(mockLessonDAO, times(1)).getByLecturerId(PERSON_ID_50005);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000002);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000003);
        verify(mockGroupDAO, times(0)).getByLessonId(null);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000002);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000003);
        verify(mockStudentDAO, times(0)).getByLessonId(null);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_51);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_52);
        verify(mockCourseDAO, times(0)).getById(null);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockLecturerDAO, times(0)).getById(null);
        verify(mockTimeSlotDAO, times(1)).getById(TIME_SLOT_ID);
        verify(mockTimeSlotDAO, times(0)).getById(null);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByLecturerIdShouldReturnLessonsListWhenNullPassed() {
        List<Lesson> expected = new ArrayList<>();
        when(mockLessonDAO.getByLecturerId(null)).thenReturn(expected);

        List<Lesson> actual = lessonServiceImpl.getByLecturerId(null);

        verify(mockLessonDAO, times(1)).getByLecturerId(null);
        assertEquals(expected, actual, "empty lesson list should be returned");
    }

    @Test
    void testGetByStudentIdShouldReturnLessonsListWhenStudentIdPassed() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(firstLesson);
        expected.add(secondLesson);
        expected.add(thirdLesson);

        when(mockLessonDAO.getByStudentId(PERSON_ID_50001)).thenReturn(expected);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonGroups);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000002)).thenReturn(secondLessonGroups);
        when(mockGroupDAO.getByLessonId(LESSON_ID_5000003)).thenReturn(new ArrayList<>());
        when(mockGroupDAO.getByLessonId(null)).thenReturn(null);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000001)).thenReturn(firstLessonStudents);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000002)).thenReturn(secondLessonStudents);
        when(mockStudentDAO.getByLessonId(LESSON_ID_5000003)).thenReturn(new ArrayList<>());
        when(mockStudentDAO.getByLessonId(null)).thenReturn(null);
        when(mockCourseDAO.getById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockCourseDAO.getById(COURSE_ID_52)).thenReturn(secondCourse);
        when(mockCourseDAO.getById(null)).thenReturn(null);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(lecturer);
        when(mockLecturerDAO.getById(null)).thenReturn(null);
        when(mockTimeSlotDAO.getById(TIME_SLOT_ID)).thenReturn(timeSlot);
        when(mockTimeSlotDAO.getById(null)).thenReturn(null);

        List<Lesson> actual = lessonServiceImpl.getByStudentId(PERSON_ID_50001);

        verify(mockLessonDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000002);
        verify(mockGroupDAO, times(1)).getByLessonId(LESSON_ID_5000003);
        verify(mockGroupDAO, times(0)).getByLessonId(null);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000001);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000002);
        verify(mockStudentDAO, times(1)).getByLessonId(LESSON_ID_5000003);
        verify(mockStudentDAO, times(0)).getByLessonId(null);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_51);
        verify(mockCourseDAO, times(1)).getById(COURSE_ID_52);
        verify(mockCourseDAO, times(0)).getById(null);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockLecturerDAO, times(0)).getById(null);
        verify(mockTimeSlotDAO, times(1)).getById(TIME_SLOT_ID);
        verify(mockTimeSlotDAO, times(0)).getById(null);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByStudentIdShouldReturnLessonsListWhenNullPassed() {
        List<Lesson> expected = new ArrayList<>();
        when(mockLessonDAO.getByStudentId(null)).thenReturn(expected);

        List<Lesson> actual = lessonServiceImpl.getByStudentId(null);

        verify(mockLessonDAO, times(1)).getByStudentId(null);
        assertEquals(expected, actual, "empty lesson list should be returned");
    }
}
