package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.CourseRepository;
import ru.petrowich.university.repository.GroupRepository;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;

class CourseServiceImplTest {
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;
    private static final String PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final String PERSON_EMAIL_50006 = "ushat.pomoev@university.edu";
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final Integer COURSE_ID_53 = 53;
    private static final String COURSE_NAME_51 = "math";
    private static final String COURSE_NAME_52 = "biology";
    private static final String COURSE_NAME_53 = "physics";
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer GROUP_ID_503 = 503;
    private static final Integer NONEXISTENT_GROUP_ID = 666;

    private final Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);
    private final Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50006).setEmail(PERSON_EMAIL_50006).setActive(false);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(firstLecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(firstLecturer).setActive(true);
    private final Course thirdCourse = new Course().setId(COURSE_ID_53).setName(COURSE_NAME_53).setAuthor(secondLecturer).setActive(false);

    private final Group firstGroup = new Group().setId(GROUP_ID_501).setActive(true);
    private final Group secondGroup = new Group().setId(GROUP_ID_502).setActive(false);
    private final Group thirdGroup = new Group().setId(GROUP_ID_503).setActive(false);

    private AutoCloseable autoCloseable;

    @Mock
    private CourseRepository mockCourseRepository;

    @Mock
    private GroupRepository mockGroupRepository;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @BeforeEach
    private void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnCourseWhenCourseIdPassed() {
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(firstCourse);
        Course actual = courseServiceImpl.getById(COURSE_ID_51);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);

        assertThatObject(actual).isEqualToComparingFieldByField(firstCourse);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockCourseRepository.findById(-1)).thenReturn(null);
        Course actual = courseServiceImpl.getById(-1);

        verify(mockCourseRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        when(mockCourseRepository.findById(null)).thenReturn(null);
        Course actual = courseServiceImpl.getById(null);

        verify(mockCourseRepository, times(1)).findById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedCourse() {
        doNothing().when(mockCourseRepository).save(firstCourse);
        courseServiceImpl.add(firstCourse);
        verify(mockCourseRepository, times(1)).save(firstCourse);
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockCourseRepository).save(null);
        courseServiceImpl.add(null);
        verify(mockCourseRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedCourse() {
        doNothing().when(mockCourseRepository).update(firstCourse);
        courseServiceImpl.update(firstCourse);
        verify(mockCourseRepository, times(1)).update(firstCourse);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockCourseRepository).update(null);
        courseServiceImpl.update(null);
        verify(mockCourseRepository, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedCourse() {
        doNothing().when(mockCourseRepository).delete(firstCourse);
        courseServiceImpl.delete(firstCourse);
        verify(mockCourseRepository, times(1)).delete(firstCourse);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockCourseRepository).delete(null);
        courseServiceImpl.delete(null);
        verify(mockCourseRepository, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnCourseList() {
        List<Course> expected = new ArrayList<>();
        expected.add(firstCourse);
        expected.add(secondCourse);
        expected.add(thirdCourse);

        when(mockCourseRepository.findAll()).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getAll();

        verify(mockCourseRepository, times(1)).findAll();

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByAuthorIdShouldReturnCourseListWhenAuthorIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(firstCourse);
        expected.add(secondCourse);

        when(mockCourseRepository.findByAuthorId(PERSON_ID_50005)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByAuthorId(PERSON_ID_50005);

        verify(mockCourseRepository, times(1)).findByAuthorId(PERSON_ID_50005);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByAuthorIdShouldReturnEmptyCourseListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        when(mockCourseRepository.findByAuthorId(null)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByAuthorId(null);

        verify(mockCourseRepository, times(1)).findByAuthorId(null);
        assertEquals(expected, actual, "empty courses list should be returned");
    }

    @Test
    void testGetByStudentIdShouldReturnCourseListWhenStudentIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(secondCourse);
        expected.add(thirdCourse);

        when(mockCourseRepository.findByStudentId(PERSON_ID_50001)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByStudentId(PERSON_ID_50001);

        verify(mockCourseRepository, times(1)).findByStudentId(PERSON_ID_50001);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByStudentIdShouldReturnEmptyCourseListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        when(mockCourseRepository.findByStudentId(null)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByStudentId(null);

        verify(mockCourseRepository, times(1)).findByStudentId(null);
        assertEquals(expected, actual, "empty courses list should be returned");
    }

    @Test
    void testGetByGroupIdShouldReturnCourseListWhenGroupIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(secondCourse);
        expected.add(thirdCourse);

        when(mockCourseRepository.findByGroupId(GROUP_ID_501)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByGroupId(GROUP_ID_501);

        verify(mockCourseRepository, times(1)).findByGroupId(GROUP_ID_501);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByGroupIdShouldReturnEmptyCourseListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        when(mockCourseRepository.findByGroupId(null)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByGroupId(null);

        verify(mockCourseRepository, times(1)).findByGroupId(null);
        assertEquals(expected, actual, "empty courses list should be returned");
    }

    @Test
    void testAssignGroupToCourseShouldInvokeRepositoryAssignGroupToCourse(){
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        firstCourse.setGroups(currentGroups);

        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockGroupRepository.findById(GROUP_ID_503)).thenReturn(thirdGroup);
        doNothing().when(mockCourseRepository).update(firstCourse);

        courseServiceImpl.assignGroupToCourse(thirdGroup, firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockGroupRepository, times(1)).findById(GROUP_ID_503);
        verify(mockCourseRepository, times(1)).update(firstCourse);

        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(secondGroup);
        expectedGroups.add(thirdGroup);

        assertEquals(expectedGroups, firstCourse.getGroups(), "list of group should contain thirdGroup");
    }

    @Test
    void testAssignGroupToCourseShouldNotInvokeRepositoryAssignGroupToCourseWhenAssignedGroupPassed(){
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        firstCourse.setGroups(currentGroups);

        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockGroupRepository.findById(GROUP_ID_502)).thenReturn(secondGroup);
        doNothing().when(mockCourseRepository).update(firstCourse);

        courseServiceImpl.assignGroupToCourse(secondGroup, firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockGroupRepository, times(1)).findById(GROUP_ID_502);
        verify(mockCourseRepository, times(0)).update(firstCourse);

        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(secondGroup);

        assertEquals(expectedGroups, firstCourse.getGroups(), "list of groups should be same");
    }

    @Test
    void testRemoveGroupToCourseShouldInvokeRepositoryRemoveGroupFromCourse(){
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        firstCourse.setGroups(currentGroups);

        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(firstCourse);
        doNothing().when(mockCourseRepository).update(firstCourse);

        courseServiceImpl.removeGroupFromCourse(secondGroup, firstCourse);
        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockCourseRepository, times(1)).update(firstCourse);

        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);

        assertEquals(expectedGroups, firstCourse.getGroups(), "list of groups should not contain secondGroup");
    }

    @Test
    void testApplyGroupsToCourseShouldInvokeRepositoryAssignGroupsAndRemoveGroups() {
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);
        firstCourse.setGroups(currentGroups);

        List<Group> actualGroups = new ArrayList<>();
        actualGroups.add(firstGroup);
        actualGroups.add(thirdGroup);
        actualGroups.add(new Group().setId(NONEXISTENT_GROUP_ID));
        actualGroups.add(null);

        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockGroupRepository.findById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockGroupRepository.findById(GROUP_ID_502)).thenReturn(secondGroup);
        when(mockGroupRepository.findById(GROUP_ID_503)).thenReturn(thirdGroup);
        when(mockGroupRepository.findById(NONEXISTENT_GROUP_ID)).thenReturn(null);
        doNothing().when(mockCourseRepository).update(firstCourse);

        courseServiceImpl.applyGroupsToCourse(actualGroups, firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockGroupRepository, times(1)).findById(GROUP_ID_501);
        verify(mockGroupRepository, times(0)).findById(GROUP_ID_502);
        verify(mockGroupRepository, times(1)).findById(GROUP_ID_503);
        verify(mockGroupRepository, times(1)).findById(NONEXISTENT_GROUP_ID);
        verify(mockCourseRepository, times(1)).update(firstCourse);

        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(thirdGroup);

        assertEquals(expectedGroups, firstCourse.getGroups(), "list of group should contain passed existed groups only");
    }
}
