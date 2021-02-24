package ru.petrowich.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.repository.GroupRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
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
    private static final Integer EXISTENT_GROUP_CAPACITY = 20;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_54 = 54;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetByIdShouldReturnExistentGroup() {
        Group expected = new Group()
                .setId(EXISTENT_GROUP_ID_501)
                .setName(EXISTENT_GROUP_NAME_501)
                .setCapacity(EXISTENT_GROUP_CAPACITY)
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

        Group actual = groupRepository.findById(EXISTENT_GROUP_ID_501).orElse(new Group());

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("students", "courses")
                .isEqualTo(expected);
        assertEquals(expected.getStudents(), actual.getStudents(), "student list should be filled");
        assertEquals(expected.getCourses(), actual.getCourses(), "course list should be filled");
    }


    @Test
    @Sql(POPULATE_DB_SQL)
    void testFindByIdShouldReturnNotPresentWhenNonexistentIdPassed() {
        Optional<Group> actual = groupRepository.findById(NONEXISTENT_GROUP_ID);
        assertFalse(actual.isPresent(), "null is expected when nonexistent id passed");
    }

    @Test
    void testFindByIdShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> groupRepository.findById(null), "InvalidDataAccessApiUsageException throw is expected");
    }

    @Test
    void testSaveShouldAddNewGroupAndSetItsNewId() {
        Group expected = new Group()
                .setName(NEW_GROUP_NAME)
                .setActive(true);

        groupRepository.save(expected);
        assertNotNull(expected.getId(), "add() should set new id to the group, new id cannot be null");

        Group actual = groupRepository.findById(expected.getId()).orElse(new Group());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("students", "courses")
                .isEqualTo(expected);
    }

    @Test
    void testSaveShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> groupRepository.save(null), "add(null) should throw InvalidDataAccessApiUsageException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateShouldUpdateExistentGroup() {
        Group actual = groupRepository.findById(EXISTENT_GROUP_ID_501).orElse(new Group());

        actual.setName(NEW_GROUP_NAME)
                .setActive(false);

        groupRepository.save(actual);

        Group expected = groupRepository.findById(EXISTENT_GROUP_ID_501).orElse(new Group());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("students", "courses")
                .isEqualTo(expected);
    }

    @Test
    void testUpdateShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> groupRepository.save(null), "update(null) should throw InvalidDataAccessApiUsageException");
    }

    @Test
    void testDeleteShouldDeactivateExistentCourse() {
        Group group = new Group().setActive(true);
        groupRepository.save(group);
        groupRepository.delete(group);

        Group actual = groupRepository.findById(group.getId()).orElse(new Group());
        assertFalse(actual.isActive(), "actual should not be active");
    }

    @Test
    void testDeleteShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> groupRepository.delete(null), "delete(null) should throw InvalidDataAccessApiUsageException");
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
}
