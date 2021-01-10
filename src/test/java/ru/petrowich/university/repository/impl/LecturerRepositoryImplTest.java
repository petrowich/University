package ru.petrowich.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.repository.LecturerRepository;
import ru.petrowich.university.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
@Transactional
class LecturerRepositoryImplTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";
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
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;
    private static final Long EXISTENT_LESSON_ID_5000002 = 5000002L;
    private static final Long EXISTENT_LESSON_ID_5000003 = 5000003L;
    private static final Long EXISTENT_LESSON_ID_5000004 = 5000004L;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnExistentLecturer() {
        Lecturer expected = new Lecturer()
                .setId(EXISTENT_PERSON_ID_50005)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50005)
                .setEmail(EXISTENT_PERSON_EMAIL_50005)
                .setComment(EXISTENT_PERSON_COMMENT_50005)
                .setActive(true);

        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_51));
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_52));
        expected.setCourses(expectedCourses);

        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000001));
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000002));
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000003));
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000004));
        expected.setLessons(expectedLessons);

        Lecturer actual = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);
        assertThatObject(expected).isEqualToComparingOnlyGivenFields(actual, "id", "firstName", "firstName", "lastName", "email", "comment", "active");
        assertEquals(expected.getCourses(), actual.getCourses(), "courses list should be filled");
        assertEquals(expected.getLessons(), actual.getLessons(), "lessons list should be filled");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNullWhenAnotherRolePersonIdPassed() {
        Lecturer actual = lecturerRepository.findById(EXISTENT_PERSON_ID_50001);
        assertNull(actual, "null is expected when not lecturer role person id passed");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNullWhenNonexistentIdPassed() {
        Lecturer actual = lecturerRepository.findById(NONEXISTENT_PERSON_ID);
        assertNull(actual, "null is expected when nonexistent person id passed");
    }

    @Test
    void testFindByIdShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> lecturerRepository.findById(null), "IllegalArgumentException throw is expected");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testSaveShouldAddNewLecturerAndSetItsNewId() {
        Lecturer expected = new Lecturer()
                .setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setActive(true);

        lecturerRepository.save(expected);
        assertNotNull(expected.getId(), "add() should set new id to the lecturer, new id cannot be null");

        Lecturer actual = lecturerRepository.findById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testSaveShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> lecturerRepository.save(null), "add(null) should throw NullPointerException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldUpdateExistentLecturer() {
        Lecturer actual = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);

        actual.setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setActive(false);

        lecturerRepository.update(actual);

        Lecturer expected = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldNotUpdateExistentNonLecturer() {
        Lecturer lecturer = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);
        lecturer.setId(EXISTENT_PERSON_ID_50001);
        lecturerRepository.update(lecturer);

        Student actual = studentRepository.findById(EXISTENT_PERSON_ID_50001);
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
    void testUpdateShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> lecturerRepository.save(null), "update(null) should throw IllegalArgumentException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteShouldDeactivateExistentLecturer() {
        Lecturer lecturer = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);
        lecturerRepository.delete(lecturer);

        Lecturer actual = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteShouldNotDeactivateExistentNonLecturer() {
        Lecturer lecturer = lecturerRepository.findById(EXISTENT_PERSON_ID_50005);
        lecturer.setId(EXISTENT_PERSON_ID_50005);
        lecturerRepository.delete(lecturer);

        Student actual = studentRepository.findById(EXISTENT_PERSON_ID_50001);
        assertTrue(actual.isActive(), "actual should be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lecturerRepository.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindAllShouldReturnAllLecturersList() {
        List<Lecturer> expected = new ArrayList<>();
        expected.add(new Lecturer().setId(EXISTENT_PERSON_ID_50005).setFirstName(EXISTENT_PERSON_FIRST_NAME_50005).setLastName(EXISTENT_PERSON_LAST_NAME_50005).setEmail(EXISTENT_PERSON_EMAIL_50005).setComment(EXISTENT_PERSON_COMMENT_50005).setActive(true));
        expected.add(new Lecturer().setId(EXISTENT_PERSON_ID_50006).setFirstName(EXISTENT_PERSON_FIRST_NAME_50006).setLastName(EXISTENT_PERSON_LAST_NAME_50006).setEmail(EXISTENT_PERSON_EMAIL_50006).setComment(EXISTENT_PERSON_COMMENT_50006).setActive(false));

        List<Lecturer> actual = lecturerRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}