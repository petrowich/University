package ru.petrowich.university.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.dao.DaoException;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Group;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
class StudentDAOImplTest {
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String NEW_PERSON_FIRST_NAME = "Улов";
    private static final String NEW_PERSON_LAST_NAME = "Налимов";
    private static final String NEW_PERSON_EMAIL = "ulov.nalimov@university.edu";
    private static final String NEW_PERSON_COMMENT = "new user";
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final Integer EXISTENT_PERSON_ID_50002 = 50002;
    private static final Integer EXISTENT_PERSON_ID_50003 = 50003;
    private static final Integer EXISTENT_PERSON_ID_50004 = 50004;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final String EXISTENT_PERSON_FIRST_NAME_50001 = "Рулон";
    private static final String EXISTENT_PERSON_FIRST_NAME_50002 = "Обвал";
    private static final String EXISTENT_PERSON_FIRST_NAME_50003 = "Рекорд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50004 = "Подрыв";
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Отряд";
    private static final String EXISTENT_PERSON_LAST_NAME_50001 = "Обоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50002 = "Забоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50003 = "Надоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50004 = "Устоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Ковбоев";
    private static final String EXISTENT_PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50002 = "obval.zaboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50003 = "record.nadoev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50004 = "podryv.ustoev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final String EXISTENT_PERSON_COMMENT_50001 = "stupid";
    private static final String EXISTENT_PERSON_COMMENT_50003 = "";
    private static final String EXISTENT_PERSON_COMMENT_50004 = "expelled";
    private static final String EXISTENT_PERSON_COMMENT_50005 = "";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LecturerDAO lecturerDAOImpl;

    @Autowired
    private StudentDAO studentDAOImpl;

    @Autowired
    private String populateDbSql;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(populateDbSql);
    }

    @Test
    void testGetByIdShouldReturnExistentStudent() {
        Student expected = new Student()
                .setId(EXISTENT_PERSON_ID_50001)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50001)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50001)
                .setEmail(EXISTENT_PERSON_EMAIL_50001)
                .setComment(EXISTENT_PERSON_COMMENT_50001)
                .setGroup(new Group().setId(501))
                .setActive(true);

        Student actual = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenAnotherRolePersonIdPassed() {
        assertThrows(DaoException.class, () -> studentDAOImpl.getById(EXISTENT_PERSON_ID_50005), "DaoException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNonexistentIdPassed() {
        assertThrows(DaoException.class, () -> studentDAOImpl.getById(NONEXISTENT_PERSON_ID), "DaoException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNullPassed() {
        assertThrows(DaoException.class, () -> studentDAOImpl.getById(null), "DaoException throw is expected");
    }

    @Test
    void testAddShouldAddNewStudentAndSetItsNewId() {
        Student expected = new Student()
                .setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setGroup(new Group().setId(502))
                .setActive(true);

        studentDAOImpl.add(expected);
        assertNotNull(expected.getId(), "add() should set new id to the student, new id cannot be null");

        Student actual = studentDAOImpl.getById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> studentDAOImpl.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    void testUpdateShouldUpdateExistentStudent() {
        Student actual = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);

        actual.setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setGroup(new Group().setId(502))
                .setActive(false);

        studentDAOImpl.update(actual);

        Student expected = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldNotUpdateExistentNonStudent() {
        Student student = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        student.setId(EXISTENT_PERSON_ID_50005);
        studentDAOImpl.update(student);

        Lecturer actual = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        Lecturer expected = new Lecturer()
                .setId(EXISTENT_PERSON_ID_50005)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50005)
                .setEmail(EXISTENT_PERSON_EMAIL_50005)
                .setComment(EXISTENT_PERSON_COMMENT_50005)
                .setActive(true);

        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> studentDAOImpl.add(null), "update(null) should throw NullPointerException");
    }

    @Test
    void testDeleteShouldDeactivateExistentStudent() {
        Student student = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        studentDAOImpl.delete(student);

        Student actual = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldNotDeactivateExistentNonStudent() {
        Student student = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        student.setId(EXISTENT_PERSON_ID_50005);
        studentDAOImpl.delete(student);

        Lecturer actual = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        assertTrue(actual.isActive(), "actual should be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> studentDAOImpl.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    void testGetAllShouldReturnAllStudentsList() {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50001).setFirstName(EXISTENT_PERSON_FIRST_NAME_50001).setLastName(EXISTENT_PERSON_LAST_NAME_50001).setEmail(EXISTENT_PERSON_EMAIL_50001).setComment(EXISTENT_PERSON_COMMENT_50001).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50002).setFirstName(EXISTENT_PERSON_FIRST_NAME_50002).setLastName(EXISTENT_PERSON_LAST_NAME_50002).setEmail(EXISTENT_PERSON_EMAIL_50002).setComment(null).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50003).setFirstName(EXISTENT_PERSON_FIRST_NAME_50003).setLastName(EXISTENT_PERSON_LAST_NAME_50003).setEmail(EXISTENT_PERSON_EMAIL_50003).setComment(EXISTENT_PERSON_COMMENT_50003).setGroup(new Group().setId(502)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50004).setFirstName(EXISTENT_PERSON_FIRST_NAME_50004).setLastName(EXISTENT_PERSON_LAST_NAME_50004).setEmail(EXISTENT_PERSON_EMAIL_50004).setComment(EXISTENT_PERSON_COMMENT_50004).setGroup(new Group()).setActive(false));

        List<Student> actual = studentDAOImpl.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Student> expectedSet = new HashSet<>(expected);
        Set<Student> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByGroupIdShouldReturnGroupStudentsListWhenGroupIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50001).setFirstName(EXISTENT_PERSON_FIRST_NAME_50001).setLastName(EXISTENT_PERSON_LAST_NAME_50001).setEmail(EXISTENT_PERSON_EMAIL_50001).setComment(EXISTENT_PERSON_COMMENT_50001).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50002).setFirstName(EXISTENT_PERSON_FIRST_NAME_50002).setLastName(EXISTENT_PERSON_LAST_NAME_50002).setEmail(EXISTENT_PERSON_EMAIL_50002).setComment(null).setGroup(new Group().setId(501)).setActive(true));

        List<Student> actual = studentDAOImpl.getByGroupId(501);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Student> expectedSet = new HashSet<>(expected);
        Set<Student> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByGroupIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        List<Student> actual = studentDAOImpl.getByGroupId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByCourseIdShouldReturnCourseStudentsListWhenCourseIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50003).setFirstName(EXISTENT_PERSON_FIRST_NAME_50003).setLastName(EXISTENT_PERSON_LAST_NAME_50003).setEmail(EXISTENT_PERSON_EMAIL_50003).setComment(EXISTENT_PERSON_COMMENT_50003).setGroup(new Group().setId(502)).setActive(true));

        List<Student> actual = studentDAOImpl.getByCourseId(53);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Student> expectedSet = new HashSet<>(expected);
        Set<Student> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByCourseIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        List<Student> actual = studentDAOImpl.getByCourseId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByLessonIdShouldReturnLessonStudentsListWhenLessonIdPassed() {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50001).setFirstName(EXISTENT_PERSON_FIRST_NAME_50001).setLastName(EXISTENT_PERSON_LAST_NAME_50001).setEmail(EXISTENT_PERSON_EMAIL_50001).setComment(EXISTENT_PERSON_COMMENT_50001).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50002).setFirstName(EXISTENT_PERSON_FIRST_NAME_50002).setLastName(EXISTENT_PERSON_LAST_NAME_50002).setEmail(EXISTENT_PERSON_EMAIL_50002).setComment(null).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50003).setFirstName(EXISTENT_PERSON_FIRST_NAME_50003).setLastName(EXISTENT_PERSON_LAST_NAME_50003).setEmail(EXISTENT_PERSON_EMAIL_50003).setComment("").setGroup(new Group().setId(502)).setActive(true));

        List<Student> actual = studentDAOImpl.getByLessonId(5000001L);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Student> expectedSet = new HashSet<>(expected);
        Set<Student> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByLessonIdShouldReturnEmptyStudentsListWhenNullPassed() {
        List<Student> expected = new ArrayList<>();
        List<Student> actual = studentDAOImpl.getByLessonId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }
}
