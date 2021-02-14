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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class CourseServiceImplTest {
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
        Optional<Course> optionalFirstCourse = Optional.of(firstCourse);
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(optionalFirstCourse);
        Course actual = courseServiceImpl.getById(COURSE_ID_51);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);

        assertThat(actual).usingRecursiveComparison().isEqualTo(firstCourse);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockCourseRepository.findById(-1)).thenReturn(Optional.empty());
        Course actual = courseServiceImpl.getById(-1);

        verify(mockCourseRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        Course actual = courseServiceImpl.getById(null);
        verify(mockCourseRepository, times(0)).findById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedCourse() {
        courseServiceImpl.add(firstCourse);
        verify(mockCourseRepository, times(1)).save(firstCourse);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedNull() {
        courseServiceImpl.add(null);
        verify(mockCourseRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedCourse() {
        courseServiceImpl.update(firstCourse);
        verify(mockCourseRepository, times(1)).save(firstCourse);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedNull() {
        courseServiceImpl.update(null);
        verify(mockCourseRepository, times(1)).save(null);
    }

    @Test
    void testDeleteShouldInvokeRepositorySaveWithPassedCourse() {
        Course actual = new Course().setId(COURSE_ID_51).setActive(true);

        Optional<Course> optionalFirstCourse = Optional.of(actual);
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(optionalFirstCourse);

        courseServiceImpl.delete(firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        assertFalse(actual.isActive(),"course should turn inactive");
        verify(mockCourseRepository, times(1)).save(firstCourse);
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> courseServiceImpl.delete(null),"delete(null) should throw NullPointerException");
        verify(mockCourseRepository, times(0)).save(null);
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
    void testAssignGroupToCourseShouldInvokeRepositoryAssignGroupToCourse(){
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        firstCourse.setGroups(currentGroups);

        Optional<Group> optionalSecondGroup = Optional.of(thirdGroup);
        when(mockGroupRepository.findById(GROUP_ID_503)).thenReturn(optionalSecondGroup);
        Optional<Course> optionalFirstCourse = Optional.of(firstCourse);
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(optionalFirstCourse);

        courseServiceImpl.assignGroupToCourse(thirdGroup, firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockGroupRepository, times(1)).findById(GROUP_ID_503);
        verify(mockCourseRepository, times(1)).save(firstCourse);

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

        Optional<Group> optionalSecondGroup = Optional.of(secondGroup);
        when(mockGroupRepository.findById(GROUP_ID_502)).thenReturn(optionalSecondGroup);
        Optional<Course> optionalFirstCourse = Optional.of(firstCourse);
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(optionalFirstCourse);

        courseServiceImpl.assignGroupToCourse(secondGroup, firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockGroupRepository, times(1)).findById(GROUP_ID_502);
        verify(mockCourseRepository, times(0)).save(firstCourse);

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

        Optional<Course> optionalFirstCourse = Optional.of(firstCourse);
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(optionalFirstCourse);

        courseServiceImpl.removeGroupFromCourse(secondGroup, firstCourse);
        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockCourseRepository, times(1)).save(firstCourse);

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

        List<Integer> groupIds = new ArrayList<>();
        groupIds.add(GROUP_ID_501);
        groupIds.add(GROUP_ID_503);
        groupIds.add(NONEXISTENT_GROUP_ID);

        List<Group> groups = new ArrayList<>();
        groups.add(firstGroup);
        groups.add(thirdGroup);
        groups.add(new Group().setId(NONEXISTENT_GROUP_ID));
        groups.add(null);

        List<Group> actualGroups = new ArrayList<>();
        actualGroups.add(firstGroup);
        actualGroups.add(thirdGroup);

        Optional<Course> optionalFirstCourse = Optional.of(firstCourse);
        when(mockCourseRepository.findById(COURSE_ID_51)).thenReturn(optionalFirstCourse);
        when(mockGroupRepository.findAllById(groupIds)).thenReturn(actualGroups);

        courseServiceImpl.applyGroupsToCourse(groups, firstCourse);

        verify(mockCourseRepository, times(1)).findById(COURSE_ID_51);
        verify(mockGroupRepository, times(1)).findAllById(groupIds);
        verify(mockCourseRepository, times(1)).save(firstCourse);

        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(thirdGroup);

        assertEquals(expectedGroups, firstCourse.getGroups(), "list of group should contain passed existed groups only");
    }
}
