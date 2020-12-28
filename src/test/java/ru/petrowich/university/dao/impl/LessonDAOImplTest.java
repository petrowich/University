package ru.petrowich.university.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.dao.DaoException;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
class LessonDAOImplTest {
    private static final Long NONEXISTENT_LESSON_ID = 9999999L;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;
    private static final Long EXISTENT_LESSON_ID_5000002 = 5000002L;
    private static final Long EXISTENT_LESSON_ID_5000003 = 5000003L;
    private static final Long EXISTENT_LESSON_ID_5000004 = 5000004L;
    private static final Long EXISTENT_LESSON_ID_5000005 = 5000005L;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final Integer EXISTENT_COURSE_ID_54 = 54;
    private static final Integer EXISTENT_COURSE_ID_55 = 55;
    private static final Integer EXISTENT_COURSE_ID_56 = 56;
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_PERSON_ID_50006 = 50006;
    private static final Integer TIME_SLOT_ID_1 = 1;
    private static final Integer TIME_SLOT_ID_2 = 2;
    private static final Integer TIME_SLOT_ID_3 = 3;
    private static final Integer TIME_SLOT_ID_4 = 4;
    private static final LocalDate NEW_LESSON_DATE = LocalDate.of(2019, 7, 25);
    private static final LocalTime NEW_LESSON_START_TIME = LocalTime.of(13, 20);
    private static final LocalTime NEW_LESSON_END_TIME = LocalTime.of(14, 50);
    private static final LocalDate EXISTENT_LESSON_DATE_5000001 = LocalDate.of(2020, 6, 1);
    private static final LocalDate EXISTENT_LESSON_DATE_5000002 = LocalDate.of(2020, 6, 1);
    private static final LocalDate EXISTENT_LESSON_DATE_5000003 = LocalDate.of(2020, 6, 1);
    private static final LocalDate EXISTENT_LESSON_DATE_5000004 = LocalDate.of(2020, 7, 1);
    private static final LocalDate EXISTENT_LESSON_DATE_5000005 = LocalDate.of(2020, 6, 1);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000001 = LocalTime.of(8, 0);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000002 = LocalTime.of(9, 40);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000003 = LocalTime.of(11, 20);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000004 = LocalTime.of(8, 0);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000005 = LocalTime.of(9, 40);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000001 = LocalTime.of(9, 30);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000002 = LocalTime.of(11, 10);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000003 = LocalTime.of(12, 50);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000004 = LocalTime.of(21, 30);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000005 = LocalTime.of(11, 10);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LessonDAO lessonDAOImpl;

    @Autowired
    private String populateDbSql;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(populateDbSql);
    }

    @Test
    void testGetByIdShouldReturnExistentLesson() {
        Lesson expected = new Lesson()
                .setId(EXISTENT_LESSON_ID_5000001)
                .setCourse(new Course().setId(EXISTENT_COURSE_ID_51))
                .setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005))
                .setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_1))
                .setDate(EXISTENT_LESSON_DATE_5000001)
                .setStartTime(EXISTENT_LESSON_START_TIME_5000001)
                .setEndTime(EXISTENT_LESSON_END_TIME_5000001);

        Lesson actual = lessonDAOImpl.getById(EXISTENT_LESSON_ID_5000001);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNonexistentIdPassed() {
        assertThrows(DaoException.class, () -> lessonDAOImpl.getById(NONEXISTENT_LESSON_ID), "DaoException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNullPassed() {
        assertThrows(DaoException.class, () -> lessonDAOImpl.getById(null), "DaoException throw is expected");
    }

    @Test
    void testAddShouldAddNewLessonAndSetItsNewId() {
        Lesson expected = new Lesson()
                .setCourse(new Course().setId(EXISTENT_COURSE_ID_54))
                .setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50006))
                .setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_4))
                .setDate(NEW_LESSON_DATE)
                .setStartTime(NEW_LESSON_START_TIME)
                .setEndTime(NEW_LESSON_END_TIME);

        lessonDAOImpl.add(expected);
        assertNotNull(expected.getId(), "add() should set new id to the timeslot, new id cannot be null");

        Lesson actual = lessonDAOImpl.getById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lessonDAOImpl.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    void testUpdateShouldUpdateExistentLesson() {
        Lesson actual = lessonDAOImpl.getById(EXISTENT_LESSON_ID_5000001);

        actual.setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50006))
                .setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_4))
                .setDate(NEW_LESSON_DATE)
                .setStartTime(NEW_LESSON_START_TIME)
                .setEndTime(NEW_LESSON_END_TIME);

        lessonDAOImpl.update(actual);

        Lesson expected = lessonDAOImpl.getById(EXISTENT_LESSON_ID_5000001);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lessonDAOImpl.update(null), "update(null) should throw NullPointerException");
    }

    @Test
    void testDeleteShouldDeactivateExistentCourse() {
        Lesson lesson = new Lesson().setId(EXISTENT_LESSON_ID_5000001);
        lessonDAOImpl.delete(lesson);

        assertThrows(DaoException.class, () -> lessonDAOImpl.getById(EXISTENT_LESSON_ID_5000001), "DaoException throw is expected");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lessonDAOImpl.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    void testGetAllShouldReturnAllCoursesList() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000001).setCourse(new Course().setId(EXISTENT_COURSE_ID_51)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_1)).setDate(EXISTENT_LESSON_DATE_5000001).setStartTime(EXISTENT_LESSON_START_TIME_5000001).setEndTime(EXISTENT_LESSON_END_TIME_5000001));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000002).setCourse(new Course().setId(EXISTENT_COURSE_ID_52)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_2)).setDate(EXISTENT_LESSON_DATE_5000002).setStartTime(EXISTENT_LESSON_START_TIME_5000002).setEndTime(EXISTENT_LESSON_END_TIME_5000002));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000003).setCourse(new Course().setId(EXISTENT_COURSE_ID_53)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_3)).setDate(EXISTENT_LESSON_DATE_5000003).setStartTime(EXISTENT_LESSON_START_TIME_5000003).setEndTime(EXISTENT_LESSON_END_TIME_5000003));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000004).setCourse(new Course().setId(EXISTENT_COURSE_ID_56)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(null)).setDate(EXISTENT_LESSON_DATE_5000004).setStartTime(EXISTENT_LESSON_START_TIME_5000004).setEndTime(EXISTENT_LESSON_END_TIME_5000004));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000005).setCourse(new Course().setId(EXISTENT_COURSE_ID_55)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50006)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_2)).setDate(EXISTENT_LESSON_DATE_5000005).setStartTime(EXISTENT_LESSON_START_TIME_5000005).setEndTime(EXISTENT_LESSON_END_TIME_5000005));

        List<Lesson> actual = lessonDAOImpl.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Lesson> expectedSet = new HashSet<>(expected);
        Set<Lesson> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByLecturerIdShouldReturnLecturerLessonsListWhenLecturerIdPassed() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000001).setCourse(new Course().setId(EXISTENT_COURSE_ID_51)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_1)).setDate(EXISTENT_LESSON_DATE_5000001).setStartTime(EXISTENT_LESSON_START_TIME_5000001).setEndTime(EXISTENT_LESSON_END_TIME_5000001));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000002).setCourse(new Course().setId(EXISTENT_COURSE_ID_52)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_2)).setDate(EXISTENT_LESSON_DATE_5000002).setStartTime(EXISTENT_LESSON_START_TIME_5000002).setEndTime(EXISTENT_LESSON_END_TIME_5000002));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000003).setCourse(new Course().setId(EXISTENT_COURSE_ID_53)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_3)).setDate(EXISTENT_LESSON_DATE_5000003).setStartTime(EXISTENT_LESSON_START_TIME_5000003).setEndTime(EXISTENT_LESSON_END_TIME_5000003));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000004).setCourse(new Course().setId(EXISTENT_COURSE_ID_56)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(null)).setDate(EXISTENT_LESSON_DATE_5000004).setStartTime(EXISTENT_LESSON_START_TIME_5000004).setEndTime(EXISTENT_LESSON_END_TIME_5000004));

        List<Lesson> actual = lessonDAOImpl.getByLecturerId(EXISTENT_PERSON_ID_50005);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Lesson> expectedSet = new HashSet<>(expected);
        Set<Lesson> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByLecturerIdShouldReturnEmptyLessonsListWhenNonexistentPersonIdPassed() {
        List<Lesson> expected = new ArrayList<>();
        List<Lesson> actual = lessonDAOImpl.getByLecturerId(EXISTENT_PERSON_ID_50001);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByLecturerIdShouldReturnEmptyLessonsListWhenNullPassed() {
        List<Lesson> expected = new ArrayList<>();
        List<Lesson> actual = lessonDAOImpl.getByLecturerId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByStudentIdShouldReturnStudentLessonsListWhenStudentIdPassed() {
        List<Lesson> expected = new ArrayList<>();
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000001).setCourse(new Course().setId(EXISTENT_COURSE_ID_51)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_1)).setDate(EXISTENT_LESSON_DATE_5000001).setStartTime(EXISTENT_LESSON_START_TIME_5000001).setEndTime(EXISTENT_LESSON_END_TIME_5000001));
        expected.add(new Lesson().setId(EXISTENT_LESSON_ID_5000002).setCourse(new Course().setId(EXISTENT_COURSE_ID_52)).setLecturer(new Lecturer().setId(EXISTENT_PERSON_ID_50005)).setTimeSlot(new TimeSlot().setId(TIME_SLOT_ID_2)).setDate(EXISTENT_LESSON_DATE_5000002).setStartTime(EXISTENT_LESSON_START_TIME_5000002).setEndTime(EXISTENT_LESSON_END_TIME_5000002));

        List<Lesson> actual = lessonDAOImpl.getByStudentId(EXISTENT_PERSON_ID_50001);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Lesson> expectedSet = new HashSet<>(expected);
        Set<Lesson> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByStudentIdShouldReturnEmptyLessonsListWhenNullPassed() {
        List<Lesson> expected = new ArrayList<>();
        List<Lesson> actual = lessonDAOImpl.getByStudentId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }
}