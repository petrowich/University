package ru.petrowich.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.repository.GroupRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
@Transactional
class GroupRepositoryImplTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";
    private static final Integer NONEXISTENT_GROUP_ID = 999;
    private static final String NEW_GROUP_NAME = "DD-04";
    private static final Integer EXISTENT_STUDENT_ID_50001 = 50001;
    private static final Integer EXISTENT_STUDENT_ID_50002 = 50002;
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer EXISTENT_GROUP_ID_503 = 503;
    private static final String EXISTENT_GROUP_NAME_501 = "AA-01";
    private static final String EXISTENT_GROUP_NAME_502 = "BB-02";
    private static final String EXISTENT_GROUP_NAME_503 = "CC-03";
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_54 = 54;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetByIdShouldReturnExistentGroup() {
        Group expected = new Group()
                .setId(EXISTENT_GROUP_ID_501)
                .setName(EXISTENT_GROUP_NAME_501)
                .setActive(true);

        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(new Student().setId(EXISTENT_STUDENT_ID_50001));
        expectedStudents.add(new Student().setId(EXISTENT_STUDENT_ID_50002));

        expected.setStudents(expectedStudents);

        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_51));
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_52));
        expectedCourses.add(new Course().setId(EXISTENT_COURSE_ID_54));

        expected.setCourses(expectedCourses);

        Group actual = groupRepository.findById(EXISTENT_GROUP_ID_501);
        assertThatObject(actual).isEqualToComparingOnlyGivenFields(expected, "id", "name", "active");
        assertEquals(expected.getStudents(), actual.getStudents(), "student list should be filled");
        assertEquals(expected.getCourses(), actual.getCourses(), "course list should be filled");
    }


    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNullWhenNonexistentIdPassed() {
        Group actual = groupRepository.findById(NONEXISTENT_GROUP_ID);
        assertNull(actual, "null is expected when nonexistent id passed");
    }

    @Test
    void testFindByIdShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> groupRepository.findById(null), "RepositoryException throw is expected");
    }

    @Test
    void testSaveShouldAddNewGroupAndSetItsNewId() {
        Group expected = new Group()
                .setName(NEW_GROUP_NAME)
                .setActive(true);

        groupRepository.save(expected);
        assertNotNull(expected.getId(), "add() should set new id to the group, new id cannot be null");

        Group actual = groupRepository.findById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testSaveShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> groupRepository.save(null), "add(null) should throw IllegalArgumentException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldUpdateExistentGroup() {
        Group actual = groupRepository.findById(EXISTENT_GROUP_ID_501);

        actual.setName(NEW_GROUP_NAME)
                .setActive(false);

        groupRepository.update(actual);

        Group expected = groupRepository.findById(EXISTENT_GROUP_ID_501);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> groupRepository.update(null), "update(null) should throw IllegalArgumentException");
    }

    @Test
    void testDeleteShouldDeactivateExistentCourse() {
        Group group = new Group().setActive(true);
        groupRepository.save(group);
        groupRepository.delete(group);

        Group actual = groupRepository.findById(group.getId());
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> groupRepository.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindAllShouldReturnAllGroupsList() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_503).setName(EXISTENT_GROUP_NAME_503).setActive(false));

        List<Group> actual = groupRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByCourseIdShouldReturnCourseGroupsListWhenCourseIdPassed() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true));

        List<Group> actual = groupRepository.findByCourseId(EXISTENT_COURSE_ID_54);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void testFindByCourseIdShouldReturnEmptyCoursesListWhenNonexistentCourseId() {
        List<Group> expected = new ArrayList<>();
        List<Group> actual = groupRepository.findByCourseId(NONEXISTENT_COURSE_ID);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testFindByCourseIdShouldReturnEmptyCoursesListWhenNullPassed() {
        List<Group> expected = new ArrayList<>();
        List<Group> actual = groupRepository.findByCourseId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByLessonIdShouldReturnCourseGroupsListWhenCourseIdPassed() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true));

        List<Group> actual = groupRepository.findByLessonId(EXISTENT_LESSON_ID_5000001);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}