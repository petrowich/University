package ru.petrowich.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.repository.LecturerRepository;
import ru.petrowich.university.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StudentRepositoryImplTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";
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
    private static final String EXISTENT_PERSON_FIRST_NAME_50001 = "Giorgio";
    private static final String EXISTENT_PERSON_FIRST_NAME_50002 = "Klaus";
    private static final String EXISTENT_PERSON_FIRST_NAME_50003 = "Syukuro";
    private static final String EXISTENT_PERSON_FIRST_NAME_50004 = "Andrea";
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Reinhard";
    private static final String EXISTENT_PERSON_LAST_NAME_50001 = "Parisi";
    private static final String EXISTENT_PERSON_LAST_NAME_50002 = "Hasselmann";
    private static final String EXISTENT_PERSON_LAST_NAME_50003 = "Manabe";
    private static final String EXISTENT_PERSON_LAST_NAME_50004 = "Ghez";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Genzel";
    private static final String EXISTENT_PERSON_EMAIL_50001 = "giorgio.parisi@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50002 = "klaus.hasselmann@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50003 = "syukuro.manabe@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50004 = "andrea.ghez@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50005 = "reinhard.genzel@university.edu";
    private static final String EXISTENT_PERSON_COMMENT_50001 = "stupid";
    private static final String EXISTENT_PERSON_COMMENT_50003 = "";
    private static final String EXISTENT_PERSON_COMMENT_50004 = "expelled";
    private static final String EXISTENT_PERSON_COMMENT_50005 = "";
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;
    private static final Long EXISTENT_LESSON_ID_5000002 = 5000002L;
    private static final Long EXISTENT_LESSON_ID_5000003 = 5000003L;
    private static final Long EXISTENT_LESSON_ID_5000004 = 5000004L;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetByIdShouldReturnExistentStudent() {
        Student expected = new Student()
                .setId(EXISTENT_PERSON_ID_50001)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50001)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50001)
                .setEmail(EXISTENT_PERSON_EMAIL_50001)
                .setComment(EXISTENT_PERSON_COMMENT_50001)
                .setGroup(new Group().setId(501))
                .setActive(true);

        Student actual = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("group")
                .isEqualTo(expected);
        assertThat(actual.getGroup()).isEqualTo(expected.getGroup());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNullWhenNonexistentIdPassed() {
        Optional<Student> actual = studentRepository.findById(NONEXISTENT_PERSON_ID);
        assertFalse(actual.isPresent(), "null is expected when nonexistent person id passed");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNullWhenAnotherRolePersonIdPassed() {
        Optional<Student> actual = studentRepository.findById(EXISTENT_PERSON_ID_50005);
        assertFalse(actual.isPresent(), "null is expected when not student role person id passed");
    }

    @Test
    void testFindByIdShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.findById(null), "InvalidDataAccessApiUsageException throw is expected");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testSaveShouldAddNewStudentAndSetItsNewId() {
        Student expected = new Student()
                .setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setGroup(new Group().setId(502))
                .setActive(true);

        studentRepository.save(expected);
        assertNotNull(expected.getId(), "add() should set new id to the student, new id cannot be null");

        Student actual = studentRepository.findById(expected.getId()).orElse(new Student());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("group")
                .isEqualTo(expected);
        assertThat(actual.getGroup()).isEqualTo(expected.getGroup());
    }

    @Test
    void testSaveShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.save(null), "add(null) should throw InvalidDataAccessApiUsageException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldUpdateExistentStudent() {
        Student actual = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());

        actual.setFirstName(NEW_PERSON_FIRST_NAME)
                .setLastName(NEW_PERSON_LAST_NAME)
                .setEmail(NEW_PERSON_EMAIL)
                .setComment(NEW_PERSON_COMMENT)
                .setGroup(new Group().setId(502))
                .setActive(false);

        studentRepository.save(actual);

        Student expected = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("group")
                .isEqualTo(expected);
        assertThat(actual.getGroup()).isEqualTo(expected.getGroup());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldNotUpdateExistentNonStudent() {
        Student student = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());
        student.setId(EXISTENT_PERSON_ID_50005);
        studentRepository.save(student);

        Lecturer actualLecturer = lecturerRepository.findById(EXISTENT_PERSON_ID_50005).orElse(new Lecturer());

        Lecturer expectedLecturer = new Lecturer()
                .setId(EXISTENT_PERSON_ID_50005)
                .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005)
                .setLastName(EXISTENT_PERSON_LAST_NAME_50005)
                .setEmail(EXISTENT_PERSON_EMAIL_50005)
                .setComment(EXISTENT_PERSON_COMMENT_50005)
                .setActive(true);

        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_51));
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_52));
        expectedLecturer.setCourses(expectedCourses);

        List<Lesson> expectedLessons = new ArrayList<>();
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000001));
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000002));
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000003));
        expectedLessons.add(new Lesson().setId(EXISTENT_LESSON_ID_5000004));
        expectedLecturer.setLessons(expectedLessons);

        assertThat(actualLecturer)
                .usingRecursiveComparison()
                .ignoringFields("courses", "lessons")
                .isEqualTo(expectedLecturer);

        assertEquals(expectedLecturer.getCourses(), actualLecturer.getCourses(), "courses list should be filled");
        assertEquals(expectedLecturer.getLessons(), actualLecturer.getLessons(), "lessons list should be filled");
    }

    @Test
    void testUpdateShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.save(null), "update(null) should throw InvalidDataAccessApiUsageException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteShouldDeactivateExistentStudent() {
        Student student = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());
        studentRepository.delete(student);

        Student actual = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteShouldNotDeactivateExistentNonStudent() {
        Student student = studentRepository.findById(EXISTENT_PERSON_ID_50001).orElse(new Student());
        student.setId(EXISTENT_PERSON_ID_50005);
        studentRepository.delete(student);

        Lecturer actual = lecturerRepository.findById(EXISTENT_PERSON_ID_50005).orElse(new Lecturer());
        assertTrue(actual.isActive(), "actual should be active");
    }

    @Test
    void testDeleteShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> studentRepository.delete(null), "delete(null) should throw InvalidDataAccessApiUsageException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindAllShouldReturnAllStudentsList() {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50001).setFirstName(EXISTENT_PERSON_FIRST_NAME_50001).setLastName(EXISTENT_PERSON_LAST_NAME_50001).setEmail(EXISTENT_PERSON_EMAIL_50001).setComment(EXISTENT_PERSON_COMMENT_50001).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50002).setFirstName(EXISTENT_PERSON_FIRST_NAME_50002).setLastName(EXISTENT_PERSON_LAST_NAME_50002).setEmail(EXISTENT_PERSON_EMAIL_50002).setComment(null).setGroup(new Group().setId(501)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50003).setFirstName(EXISTENT_PERSON_FIRST_NAME_50003).setLastName(EXISTENT_PERSON_LAST_NAME_50003).setEmail(EXISTENT_PERSON_EMAIL_50003).setComment(EXISTENT_PERSON_COMMENT_50003).setGroup(new Group().setId(502)).setActive(true));
        expected.add(new Student().setId(EXISTENT_PERSON_ID_50004).setFirstName(EXISTENT_PERSON_FIRST_NAME_50004).setLastName(EXISTENT_PERSON_LAST_NAME_50004).setEmail(EXISTENT_PERSON_EMAIL_50004).setComment(EXISTENT_PERSON_COMMENT_50004).setGroup(new Group()).setActive(false));

        List<Student> actual = studentRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
