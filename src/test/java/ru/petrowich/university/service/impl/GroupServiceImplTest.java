package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.initMocks;

class GroupServiceImplTest {
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer GROUP_ID_503 = 503;
    private static final String GROUP_NAME_501 = "AA-01";
    private static final String GROUP_NAME_502 = "BB-02";
    private static final String GROUP_NAME_503 = "CC-03";
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer PERSON_ID_50003 = 50003;
    private static final String PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String PERSON_EMAIL_50002 = "obval.zaboev@university.edu";
    private static final String PERSON_EMAIL_50003 = "record.nadoev@university.edu";

    private final Group firstGroup = new Group().setId(GROUP_ID_501).setName(GROUP_NAME_501).setActive(true);
    private final Group secondGroup = new Group().setId(GROUP_ID_502).setName(GROUP_NAME_502).setActive(true);
    private final Group thirdGroup = new Group().setId(GROUP_ID_503).setName(GROUP_NAME_503).setActive(false);

    private final Student fistStudent = new Student().setId(PERSON_ID_50001).setEmail(PERSON_EMAIL_50001).setGroup(firstGroup).setActive(true);
    private final Student secondStudent = new Student().setId(PERSON_ID_50002).setEmail(PERSON_EMAIL_50002).setGroup(firstGroup).setActive(true);
    private final Student thirdStudent = new Student().setId(PERSON_ID_50003).setEmail(PERSON_EMAIL_50003).setGroup(secondGroup).setActive(true);

    @Mock
    private GroupDAO mockGroupDAO;

    @Mock
    private StudentDAO mockStudentDAO;

    @InjectMocks
    private GroupServiceImpl groupServiceImpl;

    @BeforeEach
    private void setUp() {
        initMocks(this);
    }

    @Test
    void testGetByIdShouldReturnGroupWhenGroupIdPassed() {
        List<Student> students = new ArrayList<>();
        students.add(fistStudent);
        students.add(secondStudent);
        students.add(thirdStudent);
        firstGroup.setStudents(students);

        when(mockGroupDAO.getById(GROUP_ID_501)).thenReturn(firstGroup);
        when(mockStudentDAO.getByGroupId(GROUP_ID_501)).thenReturn(students);

        Group actual = groupServiceImpl.getById(GROUP_ID_501);

        verify(mockGroupDAO, times(1)).getById(GROUP_ID_501);
        verify(mockStudentDAO, times(1)).getByGroupId(GROUP_ID_501);

        assertThatObject(actual).isEqualToComparingFieldByField(firstGroup);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockGroupDAO.getById(-1)).thenReturn(null);
        Group actual = groupServiceImpl.getById(-1);

        verify(mockGroupDAO, times(1)).getById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        when(mockGroupDAO.getById(null)).thenReturn(null);
        Group actual = groupServiceImpl.getById(null);

        verify(mockGroupDAO, times(1)).getById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedCourse() {
        doNothing().when(mockGroupDAO).add(firstGroup);
        groupServiceImpl.add(firstGroup);
        verify(mockGroupDAO, times(1)).add(firstGroup);
    }

    @Test
    void testAddShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockGroupDAO).add(null);
        groupServiceImpl.add(null);
        verify(mockGroupDAO, times(1)).add(null);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedCourse() {
        doNothing().when(mockGroupDAO).update(firstGroup);
        groupServiceImpl.update(firstGroup);
        verify(mockGroupDAO, times(1)).update(firstGroup);
    }

    @Test
    void testUpdateShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockGroupDAO).update(null);
        groupServiceImpl.update(null);
        verify(mockGroupDAO, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedCourse() {
        doNothing().when(mockGroupDAO).delete(firstGroup);
        groupServiceImpl.delete(firstGroup);
        verify(mockGroupDAO, times(1)).delete(firstGroup);
    }

    @Test
    void testDeleteShouldInvokeDaoUpdateWithPassedNull() {
        doNothing().when(mockGroupDAO).delete(null);
        groupServiceImpl.delete(null);
        verify(mockGroupDAO, times(1)).delete(null);
    }

    @Test
    void testGetAllShouldReturnGroupsList() {
        List<Student> firstGroupStudents = new ArrayList<>();
        firstGroupStudents.add(fistStudent);
        firstGroupStudents.add(secondStudent);
        firstGroup.setStudents(firstGroupStudents);

        List<Student> secondGroupStudents = new ArrayList<>();
        secondGroupStudents.add(thirdStudent);
        secondGroup.setStudents(secondGroupStudents);

        List<Group> expected = new ArrayList<>();
        expected.add(firstGroup);
        expected.add(secondGroup);
        expected.add(thirdGroup);

        when(mockGroupDAO.getAll()).thenReturn(expected);
        when(mockStudentDAO.getByGroupId(GROUP_ID_501)).thenReturn(firstGroupStudents);
        when(mockStudentDAO.getByGroupId(GROUP_ID_502)).thenReturn(secondGroupStudents);
        when(mockStudentDAO.getByGroupId(GROUP_ID_503)).thenReturn(new ArrayList<>());

        List<Group> actual = groupServiceImpl.getAll();

        verify(mockGroupDAO, times(1)).getAll();
        verify(mockStudentDAO, times(1)).getByGroupId(GROUP_ID_501);
        verify(mockStudentDAO, times(1)).getByGroupId(GROUP_ID_502);
        verify(mockStudentDAO, times(1)).getByGroupId(GROUP_ID_503);

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
