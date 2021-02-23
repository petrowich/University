package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.GroupRepository;
import ru.petrowich.university.repository.StudentRepository;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Group;

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

class StudentServiceImplTest {
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer PERSON_ID_50003 = 50003;
    private static final String PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String PERSON_EMAIL_50002 = "obval.zaboev@university.edu";
    private static final String PERSON_EMAIL_50003 = "record.nadoev@university.edu";
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final String GROUP_NAME_501 = "AA-01";
    private static final String GROUP_NAME_502 = "BB-02";

    private static final Set<ConstraintViolation<Student>> violations = new HashSet<>();

    private final Group firstGroup = new Group().setId(GROUP_ID_501).setName(GROUP_NAME_501).setActive(true);
    private final Group secondGroup = new Group().setId(GROUP_ID_502).setName(GROUP_NAME_502).setActive(true);

    private final Student firstStudent = new Student().setId(PERSON_ID_50001).setGroup(firstGroup).setEmail(PERSON_EMAIL_50001).setActive(true);
    private final Student secondStudent = new Student().setId(PERSON_ID_50002).setGroup(firstGroup).setEmail(PERSON_EMAIL_50002).setActive(true);
    private final Student thirdStudent = new Student().setId(PERSON_ID_50003).setGroup(secondGroup).setEmail(PERSON_EMAIL_50003).setActive(false);

    private AutoCloseable autoCloseable;

    @Mock
    private StudentRepository mockStudentRepository;

    @Mock
    private GroupRepository mockGroupRepository;

    @Mock
    private Validator mockValidator;

    @InjectMocks
    private StudentServiceImpl studentServiceImpl;

    @BeforeEach
    private void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnStudentWhenStudentIdPassed() {
        Optional<Student> optionalFirstStudent = Optional.of(firstStudent);
        when(mockStudentRepository.findById(PERSON_ID_50001)).thenReturn(optionalFirstStudent);
        Optional<Group> optionalFirstGroup = Optional.of(firstGroup);
        when(mockGroupRepository.findById(GROUP_ID_501)).thenReturn(optionalFirstGroup);

        Student actual = studentServiceImpl.getById(PERSON_ID_50001);

        verify(mockStudentRepository, times(1)).findById(PERSON_ID_50001);

        assertThat(actual).usingRecursiveComparison().isEqualTo(firstStudent);
    }

    @Test
    void testGetByIdShouldReturnNullWhenNonexistentIdPassed() {
        when(mockStudentRepository.findById(-1)).thenReturn(Optional.empty());
        Student actual = studentServiceImpl.getById(-1);

        verify(mockStudentRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> studentServiceImpl.getById(null), "GetById(null) should throw NullPointerException");
        verify(mockStudentRepository, times(0)).findById(null);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedStudent() {
        when(mockValidator.validate(firstStudent)).thenReturn(violations);
        studentServiceImpl.add(firstStudent);

        verify(mockValidator, times(1)).validate(firstStudent);
        verify(mockStudentRepository, times(1)).save(firstStudent);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        when(mockValidator.validate(firstStudent)).thenReturn(violations);
        assertThrows(NullPointerException.class, () -> studentServiceImpl.add(null), "add(null) should throw NullPointerException");

        verify(mockValidator, times(0)).validate(firstStudent);
        verify(mockStudentRepository, times(0)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedStudent() {
        when(mockValidator.validate(firstStudent)).thenReturn(violations);
        studentServiceImpl.update(firstStudent);

        verify(mockValidator, times(1)).validate(firstStudent);
        verify(mockStudentRepository, times(1)).save(firstStudent);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        when(mockValidator.validate(firstStudent)).thenReturn(violations);
        assertThrows(NullPointerException.class, () -> studentServiceImpl.update(null), "update(null) should throw NullPointerException");

        verify(mockValidator, times(0)).validate(firstStudent);
        verify(mockStudentRepository, times(0)).save(null);
    }

    @Test
    void testDeleteShouldInvokeRepositorySaveWithPassedStudent() {
        Student actual = new Student().setId(PERSON_ID_50001).setActive(true);

        Optional<Student> optionalFirstStudent = Optional.of(actual);
        when(mockStudentRepository.findById(PERSON_ID_50001)).thenReturn(optionalFirstStudent);

        studentServiceImpl.delete(firstStudent);

        verify(mockStudentRepository, times(1)).findById(PERSON_ID_50001);
        assertFalse(actual.isActive(),"lecturer should turn inactive");
        verify(mockStudentRepository, times(1)).save(firstStudent);
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> studentServiceImpl.delete(null),"delete(null) should throw NullPointerException");
        verify(mockStudentRepository, times(0)).save(null);
    }

    @Test
    void testGetAllShouldReturnStudentsList() {
        List<Student> expected = new ArrayList<>();
        expected.add(firstStudent);
        expected.add(secondStudent);
        expected.add(thirdStudent);

        when(mockStudentRepository.findAll()).thenReturn(expected);

        List<Student> actual = studentServiceImpl.getAll();

        verify(mockStudentRepository, times(1)).findAll();

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
