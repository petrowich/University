package ru.petrowich.university.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.dao.DaoException;
import ru.petrowich.university.dao.GroupDAO;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
class GroupDAOImplTest {
    private static final Integer NONEXISTENT_GROUP_ID = 999;
    private static final String NEW_GROUP_NAME = "DD-04";
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer EXISTENT_GROUP_ID_503 = 503;
    private static final String EXISTENT_GROUP_NAME_501 = "AA-01";
    private static final String EXISTENT_GROUP_NAME_502 = "BB-02";
    private static final String EXISTENT_GROUP_NAME_503 = "CC-03";
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final Integer EXISTENT_COURSE_ID_54 = 54;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GroupDAO groupDAOImpl;

    @Autowired
    private String populateDbSql;

    @BeforeEach
    private void setUp() {
        jdbcTemplate.execute(populateDbSql);
    }

    @Test
    void testGetByIdShouldReturnExistentGroup() {
        Group expected = new Group()
                .setId(EXISTENT_GROUP_ID_501)
                .setName(EXISTENT_GROUP_NAME_501)
                .setActive(true);

        Group actual = groupDAOImpl.getById(EXISTENT_GROUP_ID_501);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNonexistentIdPassed() {
        assertThrows(DaoException.class, () -> groupDAOImpl.getById(NONEXISTENT_GROUP_ID), "DaoException throw is expected");
    }

    @Test
    void testGetByIdShouldThrowDaoExceptionWhenNullPassed() {
        assertThrows(DaoException.class, () -> groupDAOImpl.getById(null), "DaoException throw is expected");
    }

    @Test
    void testAddShouldAddNewGroupAndSetItsNewId() {
        Group expected = new Group()
                .setName(NEW_GROUP_NAME)
                .setActive(true);

        groupDAOImpl.add(expected);
        assertNotNull(expected.getId(), "add() should set new id to the group, new id cannot be null");

        Group actual = groupDAOImpl.getById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> groupDAOImpl.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    void testUpdateShouldUpdateExistentGroup() {
        Group actual = groupDAOImpl.getById(EXISTENT_GROUP_ID_501);

        actual.setName(NEW_GROUP_NAME)
                .setActive(false);

        groupDAOImpl.update(actual);

        Group expected = groupDAOImpl.getById(EXISTENT_GROUP_ID_501);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> groupDAOImpl.update(null), "update(null) should throw NullPointerException");
    }

    @Test
    void testDeleteShouldDeactivateExistentCourse() {
        Group group = new Group().setActive(true);
        groupDAOImpl.add(group);
        groupDAOImpl.delete(group);

        Group actual = groupDAOImpl.getById(group.getId());
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> groupDAOImpl.delete(null), "delete(null) should throw NullPointerException");
    }

    @Test
    void testGetAllShouldReturnAllGroupsList() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_503).setName(EXISTENT_GROUP_NAME_503).setActive(false));

        List<Group> actual = groupDAOImpl.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Group> expectedSet = new HashSet<>(expected);
        Set<Group> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByCourseIdShouldReturnCourseGroupsListWhenCourseIdPassed() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true));

        List<Group> actual = groupDAOImpl.getByCourseId(EXISTENT_COURSE_ID_54);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Group> expectedSet = new HashSet<>(expected);
        Set<Group> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }

    @Test
    void testGetByCourseIdShouldReturnEmptyCoursesListWhenNonexistentCourseId() {
        List<Group> expected = new ArrayList<>();
        List<Group> actual = groupDAOImpl.getByCourseId(NONEXISTENT_COURSE_ID);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByCourseIdShouldReturnEmptyCoursesListWhenNullPassed() {
        List<Group> expected = new ArrayList<>();
        List<Group> actual = groupDAOImpl.getByCourseId(null);
        assertEquals(expected, actual, "empty courses list is expected");
    }

    @Test
    void testGetByLessonIdShouldReturnCourseGroupsListWhenCourseIdPassed() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true));
        expected.add(new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true));

        List<Group> actual = groupDAOImpl.getByLessonId(EXISTENT_LESSON_ID_5000001);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<Group> expectedSet = new HashSet<>(expected);
        Set<Group> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }
}
