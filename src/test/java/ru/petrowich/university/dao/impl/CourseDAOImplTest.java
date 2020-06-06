package ru.petrowich.university.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.DaoNotFoundException;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
class CourseDAOImplTest {
    private static final String NEW_COURSE_NAME = "Some Course Name";
    private static final String NEW_COURSE_DESCRIPTION = "Some Description";
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final Integer EXISTENT_COURSE_ID_54 = 54;
    private static final Integer EXISTENT_COURSE_ID_55 = 55;
    private static final Integer EXISTENT_COURSE_ID_56 = 56;
    private static final String EXISTENT_COURSE_NAME_51 = "math";
    private static final String EXISTENT_COURSE_NAME_52 = "biology";
    private static final String EXISTENT_COURSE_NAME_53 = "physics";
    private static final String EXISTENT_COURSE_NAME_54 = "literature";
    private static final String EXISTENT_COURSE_NAME_55 = "psychology";
    private static final String EXISTENT_COURSE_NAME_56 = "litrball";
    private static final String EXISTENT_COURSE_DESCRIPTION_51 = "exact";
    private static final String EXISTENT_COURSE_DESCRIPTION_52 = "natural";
    private static final String EXISTENT_COURSE_DESCRIPTION_53 = "exact";
    private static final String EXISTENT_COURSE_DESCRIPTION_54 = "humanities";
    private static final String EXISTENT_COURSE_DESCRIPTION_55 = "humanities";
    private static final String EXISTENT_COURSE_DESCRIPTION_56 = "sport";
    private static final Integer EXISTENT_STUDENT_ID_50001 = 50001;
    private static final Integer NONEXISTENT_STUDENT_ID = 99999;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CourseDAO courseDAOImpl;

    @Autowired
    private String populateDbSql;

    @BeforeEach
    private void setUp() {
        jdbcTemplate.execute(populateDbSql);
    }

    @Test
    void testGetByIdShouldReturnExistentCourse() {
        Course expected = new Course()
                .setId(EXISTENT_COURSE_ID_51)
                .setName(EXISTENT_COURSE_NAME_51)
                .setDescription(EXISTENT_COURSE_DESCRIPTION_51)
                .setAuthor(new Lecturer().setId(50005))
                .setActive(true);

        Course actual = courseDAOImpl.getById(EXISTENT_COURSE_ID_51);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testGetByIdShouldThrowDaoNotFoundExceptionWhenNonexistentIdPassed() {
        assertThrows(DaoNotFoundException.class, () -> courseDAOImpl.getById(NONEXISTENT_COURSE_ID), "DaoNotFoundException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoNotFoundExceptionWhenNullPassed() {
        assertThrows(DaoNotFoundException.class, () -> courseDAOImpl.getById(null), "DaoNotFoundException throw is expected");
    }

    @Test
    void testAddShouldAddNewCourseAndSetItsNewId() {
        Course expected = new Course()
                .setName(NEW_COURSE_NAME)
                .setDescription(NEW_COURSE_DESCRIPTION)
                .setAuthor(new Lecturer().setId(50005))
                .setActive(true);

        courseDAOImpl.add(expected);
        assertNotNull(expected.getId(), "add() should set new id to the course, new id cannot be null");

        Course actual = courseDAOImpl.getById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> courseDAOImpl.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    void testUpdateShouldUpdateExistentCourse() {
        Course actual = courseDAOImpl.getById(EXISTENT_COURSE_ID_51);

        actual.setName(NEW_COURSE_NAME)
                .setDescription(NEW_COURSE_DESCRIPTION)
                .setAuthor(new Lecturer().setId(50006))
                .setActive(false);

        courseDAOImpl.update(actual);

        Course expected = courseDAOImpl.getById(EXISTENT_COURSE_ID_51);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> courseDAOImpl.update(null), "update(null) should throw NullPointerException");
    }

    @Test
    void testDeleteShouldDeactivateExistentCourse() {
        Course course = new Course().setActive(true);
        courseDAOImpl.add(course);
        courseDAOImpl.delete(course);

        Course actual = courseDAOImpl.getById(course.getId());
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> courseDAOImpl.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    void testGetAllShouldReturnAllCoursesList() {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().setId(EXISTENT_COURSE_ID_51).setName(EXISTENT_COURSE_NAME_51).setDescription(EXISTENT_COURSE_DESCRIPTION_51).setAuthor(new Lecturer().setId(50005)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_52).setName(EXISTENT_COURSE_NAME_52).setDescription(EXISTENT_COURSE_DESCRIPTION_52).setAuthor(new Lecturer().setId(50005)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_53).setName(EXISTENT_COURSE_NAME_53).setDescription(EXISTENT_COURSE_DESCRIPTION_53).setAuthor(new Lecturer().setId(50006)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_55).setName(EXISTENT_COURSE_NAME_55).setDescription(EXISTENT_COURSE_DESCRIPTION_55).setAuthor(new Lecturer().setId(50006)).setActive(false));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_56).setName(EXISTENT_COURSE_NAME_56).setDescription(EXISTENT_COURSE_DESCRIPTION_56).setAuthor(new Lecturer().setId(50003)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_54).setName(EXISTENT_COURSE_NAME_54).setDescription(EXISTENT_COURSE_DESCRIPTION_54).setAuthor(new Lecturer()).setActive(true));

        List<Course> actual = courseDAOImpl.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Course> expectedSet = new HashSet<>(expected);
        Set<Course> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByAuthorIdShouldReturnAuthorCoursesListWhenAuthorIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().setId(EXISTENT_COURSE_ID_51).setName(EXISTENT_COURSE_NAME_51).setDescription(EXISTENT_COURSE_DESCRIPTION_51).setAuthor(new Lecturer().setId(50005)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_52).setName(EXISTENT_COURSE_NAME_52).setDescription(EXISTENT_COURSE_DESCRIPTION_52).setAuthor(new Lecturer().setId(50005)).setActive(true));

        List<Course> actual = courseDAOImpl.getByAuthorId(50005);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Course> expectedSet = new HashSet<>(expected);
        Set<Course> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByAuthorIdShouldReturnEmptyCoursesListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        List<Course> actual = courseDAOImpl.getByAuthorId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByStudentIdShouldReturnStudentCoursesListWhenStudentIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().setId(EXISTENT_COURSE_ID_51).setName(EXISTENT_COURSE_NAME_51).setDescription(EXISTENT_COURSE_DESCRIPTION_51).setAuthor(new Lecturer().setId(50005)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_54).setName(EXISTENT_COURSE_NAME_54).setDescription(EXISTENT_COURSE_DESCRIPTION_54).setAuthor(new Lecturer()).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_52).setName(EXISTENT_COURSE_NAME_52).setDescription(EXISTENT_COURSE_DESCRIPTION_52).setAuthor(new Lecturer().setId(50005)).setActive(true));

        List<Course> actual = courseDAOImpl.getByStudentId(EXISTENT_STUDENT_ID_50001);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Course> expectedSet = new HashSet<>(expected);
        Set<Course> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByCourseIdShouldReturnEmptyCoursesListWhenNonexistentCourseIdPassed() {
        List<Course> expected = new ArrayList<>();
        List<Course> actual = courseDAOImpl.getByStudentId(NONEXISTENT_STUDENT_ID);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByStudentIdShouldReturnEmptyCoursesListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        List<Course> actual = courseDAOImpl.getByStudentId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }
}
