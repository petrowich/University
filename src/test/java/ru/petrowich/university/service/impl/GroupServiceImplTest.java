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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

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
    private static final String PERSON_EMAIL_50001 = "giorgio.parisi@university.edu";
    private static final String PERSON_EMAIL_50002 = "klaus.hasselmann@university.edu";
    private static final String PERSON_EMAIL_50003 = "syukuro.manabe@university.edu";

    private static final Set<ConstraintViolation<Group>> violations = new HashSet<>();

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

    @Mock
    private Validator mockValidator;

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

        Optional<Group> optionalFirstGroup = Optional.of(firstGroup);
        when(mockGroupRepository.findById(GROUP_ID_501)).thenReturn(optionalFirstGroup);

        Group actual = groupServiceImpl.getById(GROUP_ID_501);

        verify(mockGroupRepository, times(1)).findById(GROUP_ID_501);

        assertThat(actual).usingRecursiveComparison().isEqualTo(firstGroup);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockGroupRepository.findById(-1)).thenReturn(Optional.empty());
        Group actual = groupServiceImpl.getById(-1);

        verify(mockGroupRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldIllegalArgumentExceptionWhenNullPassed() {
        assertThrows(IllegalArgumentException.class, () -> groupServiceImpl.getById(null), "GetById(null) should throw IllegalArgumentException");
        verify(mockGroupRepository, times(0)).findById(null);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedCourse() {
        when(mockValidator.validate(firstGroup)).thenReturn(violations);
        groupServiceImpl.add(firstGroup);

        verify(mockValidator, times(1)).validate(firstGroup);
        verify(mockGroupRepository, times(1)).save(firstGroup);
    }

    @Test
    void testAddShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        when(mockValidator.validate(firstGroup)).thenReturn(violations);
        assertThrows(IllegalArgumentException.class, () -> groupServiceImpl.add(null), "add(null) should throw IllegalArgumentException");

        verify(mockValidator, times(0)).validate(firstGroup);
        verify(mockGroupRepository, times(0)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedCourse() {
        when(mockValidator.validate(firstGroup)).thenReturn(violations);
        groupServiceImpl.update(firstGroup);

        verify(mockValidator, times(1)).validate(firstGroup);
        verify(mockGroupRepository, times(1)).save(firstGroup);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        when(mockValidator.validate(firstGroup)).thenReturn(violations);
        assertThrows(NullPointerException.class, () -> groupServiceImpl.update(null), "update(null) should throw NullPointerException");

        verify(mockValidator, times(0)).validate(firstGroup);
        verify(mockGroupRepository, times(0)).save(null);
    }

    @Test
    void testDeleteShouldInvokeRepositorySaveWithPassedCourse() {
        Group actual = new Group().setId(GROUP_ID_501).setActive(true);

        Optional<Group> optionalFirstGroup = Optional.of(actual);
        when(mockGroupRepository.findById(GROUP_ID_501)).thenReturn(optionalFirstGroup);

        groupServiceImpl.delete(firstGroup);

        verify(mockGroupRepository, times(1)).findById(GROUP_ID_501);
        assertFalse(actual.isActive(), "group should turn inactive");
        verify(mockGroupRepository, times(1)).save(firstGroup);
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> groupServiceImpl.delete(null), "delete(null) should throw NullPointerException");
        verify(mockGroupRepository, times(0)).save(null);
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
