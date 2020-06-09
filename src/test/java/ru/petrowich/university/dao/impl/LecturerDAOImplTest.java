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
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
class LecturerDAOImplTest {
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String NEW_PERSON_FIRST_NAME = "Улов";
    private static final String NEW_PERSON_LAST_NAME = "Налимов";
    private static final String NEW_PERSON_EMAIL = "ulov.nalimov@university.edu";
    private static final String NEW_PERSON_COMMENT = "new user";
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_PERSON_ID_50006 = 50006;
    private static final String EXISTENT_PERSON_FIRST_NAME_50001 = "Рулон";
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Отряд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50006 = "Ушат";
    private static final String EXISTENT_PERSON_LAST_NAME_50001 = "Обоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Ковбоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50006 = "Помоев";
    private static final String EXISTENT_PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50006 = "ushat.pomoev@university.edu";
    private static final String EXISTENT_PERSON_COMMENT_50001 = "stupid";
    private static final String EXISTENT_PERSON_COMMENT_50005 = "";
    private static final String EXISTENT_PERSON_COMMENT_50006 = "died";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentDAO studentDAOImpl;

    @Autowired
    private LecturerDAO lecturerDAOImpl;

    @Autowired
    private String populateDbSql;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(populateDbSql);
    }

    @Test
    void testGetByIdShouldReturnExistentLecturer() {
        Lecturer expected = new Lecturer()
                .setId(EXISTENT_PERSON_ID_50005)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50005)
                .setEmail(EXISTENT_PERSON_EMAIL_50005)
                .setComment(EXISTENT_PERSON_COMMENT_50005)
                .setActive(true);

        Lecturer actual = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenAnotherRoleLecturerIdPassed() {
        assertThrows(DaoException.class, () -> lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50001), "DaoException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNonexistentIdPassed() {
        assertThrows(DaoException.class, () -> lecturerDAOImpl.getById(NONEXISTENT_PERSON_ID), "DaoException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNullPassed() {
        assertThrows(DaoException.class, () -> lecturerDAOImpl.getById(null), "DaoException throw is expected");
    }

    @Test
    void testAddShouldAddNewLecturerAndSetItsNewId() {
        Lecturer expected = new Lecturer()
                .setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setActive(true);

        lecturerDAOImpl.add(expected);
        assertNotNull(expected.getId(), "add() should set new id to the lecturer, new id cannot be null");

        Lecturer actual = lecturerDAOImpl.getById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lecturerDAOImpl.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    void testUpdateShouldUpdateExistentLecturer() {
        Lecturer actual = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);

        actual.setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setActive(false);

        lecturerDAOImpl.update(actual);

        Lecturer expected = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldNotUpdateExistentNonLecturer() {
        Lecturer lecturer = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        lecturer.setId(EXISTENT_PERSON_ID_50001);
        lecturerDAOImpl.update(lecturer);

        Student actual = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        Student expected = new Student()
                .setId(EXISTENT_PERSON_ID_50001)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50001)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50001)
                .setEmail(EXISTENT_PERSON_EMAIL_50001)
                .setComment(EXISTENT_PERSON_COMMENT_50001)
                .setGroup(new Group().setId(501))
                .setActive(true);

        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lecturerDAOImpl.add(null), "update(null) should throw NullPointerException");
    }

    @Test
    void testDeleteShouldDeactivateExistentLecturer() {
        Lecturer lecturer = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        lecturerDAOImpl.delete(lecturer);

        Lecturer actual = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldNotDeactivateExistentNonLecturer() {
        Lecturer lecturer = lecturerDAOImpl.getById(EXISTENT_PERSON_ID_50005);
        lecturer.setId(EXISTENT_PERSON_ID_50005);
        lecturerDAOImpl.delete(lecturer);

        Student actual = studentDAOImpl.getById(EXISTENT_PERSON_ID_50001);
        assertTrue(actual.isActive(), "actual should be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lecturerDAOImpl.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    void testGetAllShouldReturnAllLecturersList() {
        List<Lecturer> expected = new ArrayList<>();
        expected.add(new Lecturer().setId(EXISTENT_PERSON_ID_50005).setFirstName(EXISTENT_PERSON_FIRST_NAME_50005).setLastName(EXISTENT_PERSON_LAST_NAME_50005).setEmail(EXISTENT_PERSON_EMAIL_50005).setComment(EXISTENT_PERSON_COMMENT_50005).setActive(true));
        expected.add(new Lecturer().setId(EXISTENT_PERSON_ID_50006).setFirstName(EXISTENT_PERSON_FIRST_NAME_50006).setLastName(EXISTENT_PERSON_LAST_NAME_50006).setEmail(EXISTENT_PERSON_EMAIL_50006).setComment(EXISTENT_PERSON_COMMENT_50006).setActive(false));

        List<Lecturer> actual = lecturerDAOImpl.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Lecturer> expectedSet = new HashSet<>(expected);
        Set<Lecturer> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }
}
