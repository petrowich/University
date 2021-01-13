package ru.petrowich.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.repository.CourseRepository;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
@Transactional
class CourseRepositoryImplTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";
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
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer NONEXISTENT_GROUP_ID = 999;
    private static final Integer EXISTENT_STUDENT_ID_50001 = 50001;
    private static final Integer EXISTENT_STUDENT_ID_50002 = 50002;
    private static final Integer EXISTENT_STUDENT_ID_50003 = 50003;
    private static final Integer NONEXISTENT_STUDENT_ID = 99999;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnExistentCourse() {
        Course expected = new Course()
                .setId(EXISTENT_COURSE_ID_51)
                .setName(EXISTENT_COURSE_NAME_51)
                .setDescription(EXISTENT_COURSE_DESCRIPTION_51)
                .setAuthor(new Lecturer().setId(50005))
                .setActive(true);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group().setId(EXISTENT_GROUP_ID_501));
        groups.add(new Group().setId(EXISTENT_GROUP_ID_502));
        expected.setGroups(groups);

        Course actual = courseRepository.findById(EXISTENT_COURSE_ID_51);
        assertThatObject(actual).isEqualToIgnoringGivenFields(expected, "groups", "students");
        assertTrue(actual.getGroups().size() == expected.getGroups().size() && actual.getGroups().containsAll(expected.getGroups()) && expected.getGroups().containsAll(actual.getGroups()));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNullWhenNonexistentIdPassed() {
        Course actual = courseRepository.findById(NONEXISTENT_COURSE_ID);
        assertNull(actual, "null is expected when nonexistent id passed");
    }

    @Test
    void testFindByIdShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> courseRepository.findById(null), "findById(null) should throw IllegalArgumentException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testSaveShouldAddNewCourseAndSetItsNewId() {
        Course expected = new Course()
                .setName(NEW_COURSE_NAME)
                .setDescription(NEW_COURSE_DESCRIPTION)
                .setAuthor(new Lecturer().setId(50005))
                .setActive(true);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group().setId(EXISTENT_GROUP_ID_501));
        groups.add(new Group().setId(EXISTENT_GROUP_ID_502));
        expected.setGroups(groups);

        courseRepository.save(expected);
        assertNotNull(expected.getId(), "add() should set new id to the course, new id cannot be null");

        Course actual = courseRepository.findById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testSaveShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> courseRepository.save(null), "add(null) should throw IllegalArgumentException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldUpdateExistentCourse() {
        Course actual = courseRepository.findById(EXISTENT_COURSE_ID_51);

        actual.setName(NEW_COURSE_NAME)
                .setDescription(NEW_COURSE_DESCRIPTION)
                .setAuthor(new Lecturer().setId(50006))
                .setActive(false);

        courseRepository.update(actual);

        Course expected = courseRepository.findById(EXISTENT_COURSE_ID_51);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> courseRepository.update(null), "update(null) should throw IllegalArgumentException");
    }

    @Test
    void testDeleteShouldDeactivateExistentCourse() {
        Course course = new Course().setActive(true);
        courseRepository.save(course);
        courseRepository.delete(course);

        Course actual = courseRepository.findById(course.getId());
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> courseRepository.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindAllShouldReturnAllCoursesList() {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course().setId(EXISTENT_COURSE_ID_51).setName(EXISTENT_COURSE_NAME_51).setDescription(EXISTENT_COURSE_DESCRIPTION_51).setAuthor(new Lecturer().setId(50005)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_52).setName(EXISTENT_COURSE_NAME_52).setDescription(EXISTENT_COURSE_DESCRIPTION_52).setAuthor(new Lecturer().setId(50005)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_53).setName(EXISTENT_COURSE_NAME_53).setDescription(EXISTENT_COURSE_DESCRIPTION_53).setAuthor(new Lecturer().setId(50006)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_55).setName(EXISTENT_COURSE_NAME_55).setDescription(EXISTENT_COURSE_DESCRIPTION_55).setAuthor(new Lecturer().setId(50006)).setActive(false));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_56).setName(EXISTENT_COURSE_NAME_56).setDescription(EXISTENT_COURSE_DESCRIPTION_56).setAuthor(new Lecturer().setId(null)).setActive(true));
        expected.add(new Course().setId(EXISTENT_COURSE_ID_54).setName(EXISTENT_COURSE_NAME_54).setDescription(EXISTENT_COURSE_DESCRIPTION_54).setAuthor(new Lecturer()).setActive(true));

        List<Course> actual = courseRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
