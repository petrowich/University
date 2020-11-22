package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.LecturerDAO;
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
import static org.mockito.MockitoAnnotations.initMocks;

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

    private final Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);
    private final Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50006).setEmail(PERSON_EMAIL_50006).setActive(false);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(firstLecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(firstLecturer).setActive(true);
    private final Course thirdCourse = new Course().setId(COURSE_ID_53).setName(COURSE_NAME_53).setAuthor(secondLecturer).setActive(false);

    private final Group firstGroup = new Group().setId(GROUP_ID_501).setActive(true);
    private final Group secondGroup = new Group().setId(GROUP_ID_502).setActive(false);
    private final Group thirdGroup = new Group().setId(GROUP_ID_503).setActive(false);

    @Mock
    private CourseDAO mockCourseDAO;

    @Mock
    private GroupDAO mockGroupDAO;

    @Mock
    private LecturerDAO mockLecturerDAO;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @BeforeEach
    private void setUp() {
        initMocks(this);
    }

    @Test
    void testGetByIdShouldReturnCourseWhenCourseIdPassed() {
        when(mockCourseDAO.getById(COURSE_ID_51)).thenReturn(firstCourse);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(firstLecturer);

        Course actual = courseServiceImpl.getById(COURSE_ID_51);

        verify(mockCourseDAO, times(1)).getById(COURSE_ID_51);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);

        assertThatObject(actual).isEqualToComparingFieldByField(firstCourse);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockCourseDAO.getById(-1)).thenReturn(null);
        Course actual = courseServiceImpl.getById(-1);

        verify(mockCourseDAO, times(1)).getById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        when(mockCourseDAO.getById(null)).thenReturn(null);
        Course actual = courseServiceImpl.getById(null);

        verify(mockCourseDAO, times(1)).getById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedCourse() {
        doNothing().when(mockCourseDAO).add(firstCourse);
        courseServiceImpl.add(firstCourse);
        verify(mockCourseDAO, times(1)).add(firstCourse);
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockCourseDAO).add(null);
        courseServiceImpl.add(null);
        verify(mockCourseDAO, times(1)).add(null);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedCourse() {
        doNothing().when(mockCourseDAO).update(firstCourse);
        courseServiceImpl.update(firstCourse);
        verify(mockCourseDAO, times(1)).update(firstCourse);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockCourseDAO).update(null);
        courseServiceImpl.update(null);
        verify(mockCourseDAO, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedCourse() {
        doNothing().when(mockCourseDAO).delete(firstCourse);
        courseServiceImpl.delete(firstCourse);
        verify(mockCourseDAO, times(1)).delete(firstCourse);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockCourseDAO).delete(null);
        courseServiceImpl.delete(null);
        verify(mockCourseDAO, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnCourseList() {
        List<Course> expected = new ArrayList<>();
        expected.add(firstCourse);
        expected.add(secondCourse);
        expected.add(thirdCourse);

        when(mockCourseDAO.getAll()).thenReturn(expected);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(firstLecturer);
        when(mockLecturerDAO.getById(PERSON_ID_50006)).thenReturn(secondLecturer);

        List<Course> actual = courseServiceImpl.getAll();

        verify(mockCourseDAO, times(1)).getAll();
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50006);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByAuthorIdShouldReturnCourseListWhenAuthorIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(firstCourse);
        expected.add(secondCourse);

        when(mockCourseDAO.getByAuthorId(PERSON_ID_50005)).thenReturn(expected);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(firstLecturer);

        List<Course> actual = courseServiceImpl.getByAuthorId(PERSON_ID_50005);

        verify(mockCourseDAO, times(1)).getByAuthorId(PERSON_ID_50005);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByAuthorIdShouldReturnEmptyCourseListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        when(mockCourseDAO.getByAuthorId(null)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByAuthorId(null);

        verify(mockCourseDAO, times(1)).getByAuthorId(null);
        assertEquals(expected, actual, "empty courses list should be returned");
    }

    @Test
    void testGetByStudentIdShouldReturnCourseListWhenStudentIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(secondCourse);
        expected.add(thirdCourse);

        when(mockCourseDAO.getByStudentId(PERSON_ID_50001)).thenReturn(expected);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(firstLecturer);
        when(mockLecturerDAO.getById(PERSON_ID_50006)).thenReturn(secondLecturer);

        List<Course> actual = courseServiceImpl.getByStudentId(PERSON_ID_50001);

        verify(mockCourseDAO, times(1)).getByStudentId(PERSON_ID_50001);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50006);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByStudentIdShouldReturnEmptyCourseListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        when(mockCourseDAO.getByStudentId(null)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByStudentId(null);

        verify(mockCourseDAO, times(1)).getByStudentId(null);
        assertEquals(expected, actual, "empty courses list should be returned");
    }

    @Test
    void testGetByGroupIdShouldReturnCourseListWhenGroupIdPassed() {
        List<Course> expected = new ArrayList<>();
        expected.add(secondCourse);
        expected.add(thirdCourse);

        when(mockCourseDAO.getByGroupId(GROUP_ID_501)).thenReturn(expected);
        when(mockLecturerDAO.getById(PERSON_ID_50005)).thenReturn(firstLecturer);
        when(mockLecturerDAO.getById(PERSON_ID_50006)).thenReturn(secondLecturer);

        List<Course> actual = courseServiceImpl.getByGroupId(GROUP_ID_501);

        verify(mockCourseDAO, times(1)).getByGroupId(GROUP_ID_501);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50005);
        verify(mockLecturerDAO, times(1)).getById(PERSON_ID_50006);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    @Test
    void testGetByGroupIdShouldReturnEmptyCourseListWhenNullPassed() {
        List<Course> expected = new ArrayList<>();
        when(mockCourseDAO.getByGroupId(null)).thenReturn(expected);

        List<Course> actual = courseServiceImpl.getByGroupId(null);

        verify(mockCourseDAO, times(1)).getByGroupId(null);
        assertEquals(expected, actual, "empty courses list should be returned");
    }

    @Test
    void testAssignGroupToCourseShouldInvokeDaoAssignGroupToCourse(){
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        when(mockGroupDAO.getByCourseId(COURSE_ID_51)).thenReturn(currentGroups);
        doNothing().when(mockCourseDAO).assignGroupToCourse(thirdGroup, firstCourse);

        courseServiceImpl.assignGroupToCourse(thirdGroup, firstCourse);

        verify(mockCourseDAO, times(1)).assignGroupToCourse(thirdGroup, firstCourse);
    }

    @Test
    void testRemoveGroupToCourseShouldInvokeDaoRemoveGroupFromCourse(){
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        when(mockGroupDAO.getByCourseId(COURSE_ID_51)).thenReturn(currentGroups);
        doNothing().when(mockCourseDAO).removeGroupFromCourse(secondGroup, firstCourse);

        courseServiceImpl.removeGroupFromCourse(secondGroup, firstCourse);

        verify(mockCourseDAO, times(1)).removeGroupFromCourse(secondGroup, firstCourse);
    }

    @Test
    void testApplyGroupsToCourseShouldInvokeDaoAssignGroupsAndRemoveGroups() {
        List<Group> currentGroups = new ArrayList<>();
        currentGroups.add(firstGroup);
        currentGroups.add(secondGroup);

        List<Group> newGroupList = new ArrayList<>();
        newGroupList.add(firstGroup);
        newGroupList.add(thirdGroup);

        List<Group> additionalGroups = new ArrayList<>();
        currentGroups.add(thirdGroup);

        List<Group> excessGroups = new ArrayList<>();
        excessGroups.add(secondGroup);

        when(mockGroupDAO.getByCourseId(COURSE_ID_51)).thenReturn(currentGroups);
        doNothing().when(mockCourseDAO).assignGroupsToCourse(additionalGroups, firstCourse);
        doNothing().when(mockCourseDAO).removeGroupsFromCourse(excessGroups, firstCourse);

        courseServiceImpl.applyGroupsToCourse(newGroupList, firstCourse);

        verify(mockCourseDAO, times(1)).assignGroupsToCourse(additionalGroups, firstCourse);
        verify(mockCourseDAO, times(1)).removeGroupsFromCourse(excessGroups, firstCourse);
    }
}
