package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.GroupRepository;
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

    private AutoCloseable autoCloseable;

    @Mock
    private GroupRepository mockGroupRepository;

    @InjectMocks
    private GroupServiceImpl groupServiceImpl;

    @BeforeEach
    private void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnGroupWhenGroupIdPassed() {
        List<Student> students = new ArrayList<>();
        students.add(fistStudent);
        students.add(secondStudent);
        students.add(thirdStudent);
        firstGroup.setStudents(students);

        when(mockGroupRepository.findById(GROUP_ID_501)).thenReturn(firstGroup);

        Group actual = groupServiceImpl.getById(GROUP_ID_501);

        verify(mockGroupRepository, times(1)).findById(GROUP_ID_501);

        assertThatObject(actual).isEqualToComparingFieldByField(firstGroup);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockGroupRepository.findById(-1)).thenReturn(null);
        Group actual = groupServiceImpl.getById(-1);

        verify(mockGroupRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNullPassed() {
        when(mockGroupRepository.findById(null)).thenReturn(null);
        Group actual = groupServiceImpl.getById(null);

        verify(mockGroupRepository, times(1)).findById(null);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedCourse() {
        doNothing().when(mockGroupRepository).save(firstGroup);
        groupServiceImpl.add(firstGroup);
        verify(mockGroupRepository, times(1)).save(firstGroup);
    }

    @Test
    void testAddShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockGroupRepository).save(null);
        groupServiceImpl.add(null);
        verify(mockGroupRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedCourse() {
        doNothing().when(mockGroupRepository).update(firstGroup);
        groupServiceImpl.update(firstGroup);
        verify(mockGroupRepository, times(1)).update(firstGroup);
    }

    @Test
    void testUpdateShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockGroupRepository).update(null);
        groupServiceImpl.update(null);
        verify(mockGroupRepository, times(1)).update(null);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedCourse() {
        doNothing().when(mockGroupRepository).delete(firstGroup);
        groupServiceImpl.delete(firstGroup);
        verify(mockGroupRepository, times(1)).delete(firstGroup);
    }

    @Test
    void testDeleteShouldInvokeRepositoryUpdateWithPassedNull() {
        doNothing().when(mockGroupRepository).delete(null);
        groupServiceImpl.delete(null);
        verify(mockGroupRepository, times(1)).delete(null);
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

        when(mockGroupRepository.findAll()).thenReturn(expected);

        List<Group> actual = groupServiceImpl.getAll();

        verify(mockGroupRepository, times(1)).findAll();

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
