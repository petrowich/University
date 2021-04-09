package ru.petrowich.university.controller.students;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.petrowich.university.dto.students.StudentDTO;
import ru.petrowich.university.mapper.students.StudentMapper;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class StudentRestControllerTest {
    private static final Integer NEW_PERSON_ID = 50007;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final String ANOTHER_PERSON_EMAIL = "another@mail.com";

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final StudentMapper studentMapper = new StudentMapper(modelMapper);

    @Mock
    StudentService mockStudentService;

    @Mock
    StudentMapper mockStudentMapper;

    @InjectMocks
    StudentRestController studentRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetStudentShouldReturnOK() {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO = studentMapper.toDto(student);

        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student);
        when(mockStudentMapper.toDto(student)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.getStudent(EXISTENT_PERSON_ID_50001);

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentMapper, times(1)).toDto(student);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(studentDTO);
    }

    @Test
    void testGetStudentShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<StudentDTO> responseEntity = studentRestController.getStudent(null);

        verify(mockStudentService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetStudentShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.getStudent(NONEXISTENT_PERSON_ID);

        verify(mockStudentService, times(1)).getById(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddStudentShouldReturnCreated() {
        Student newStudent = new Student();
        StudentDTO newStudentDTO = studentMapper.toDto(newStudent);
        Student expectedStudent = studentMapper.toEntity(newStudentDTO).setId(NEW_PERSON_ID);

        when(mockStudentMapper.toEntity(newStudentDTO)).thenReturn(newStudent);
        when(mockStudentService.add(newStudent)).thenReturn(expectedStudent);

        StudentDTO expectedStudentDTO = studentMapper.toDto(expectedStudent);
        when(mockStudentMapper.toDto(expectedStudent)).thenReturn(expectedStudentDTO);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.addStudent(newStudentDTO);

        verify(mockStudentMapper, times(1)).toEntity(newStudentDTO);
        verify(mockStudentService, times(1)).add(newStudent);
        verify(mockStudentMapper, times(1)).toDto(expectedStudent);
        assertThat(responseEntity.getBody()).isEqualTo(expectedStudentDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateStudentShouldReturnOK() {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO = studentMapper.toDto(student);
        Student expectedStudent = studentMapper.toEntity(studentDTO).setEmail(ANOTHER_PERSON_EMAIL);

        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student);
        when(mockStudentMapper.toEntity(studentDTO)).thenReturn(student);
        when(mockStudentService.update(student)).thenReturn(expectedStudent);

        StudentDTO expectedStudentDTO = studentMapper.toDto(expectedStudent);
        when(mockStudentMapper.toDto(expectedStudent)).thenReturn(expectedStudentDTO);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.updateStudent(studentDTO, EXISTENT_PERSON_ID_50001);

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentMapper, times(1)).toEntity(studentDTO);
        verify(mockStudentService, times(1)).update(student);
        verify(mockStudentMapper, times(1)).toDto(expectedStudent);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedStudentDTO);
    }

    @Test
    void testUpdateStudentShouldReturnBadRequestWhenNullIdPassed() {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO = studentMapper.toDto(student);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.updateStudent(studentDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateStudentShouldReturnNotFoundWhenNonexistentIdPassed() {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO = studentMapper.toDto(student);

        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.updateStudent(studentDTO, NONEXISTENT_PERSON_ID);

        verify(mockStudentService, times(1)).getById(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteStudentShouldReturnOK() {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.deleteStudent(EXISTENT_PERSON_ID_50001);

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentService, times(1)).delete(student);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteStudentShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<StudentDTO> responseEntity = studentRestController.deleteStudent(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteStudentShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);
        ResponseEntity<StudentDTO> responseEntity = studentRestController.deleteStudent(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllStudentsShouldReturnOK() {
        Student student = new Student().setId(EXISTENT_PERSON_ID_50001);
        StudentDTO studentDTO1 = studentMapper.toDto(student);

        List<Student> students = new ArrayList<>(singletonList(student));

        when(mockStudentService.getAll()).thenReturn(students);
        when(mockStudentMapper.toDto(student)).thenReturn(studentDTO1);

        ResponseEntity<List<StudentDTO>> responseEntity = studentRestController.getAllStudents();

        verify(mockStudentService, times(1)).getAll();
        verify(mockStudentMapper, times(1)).toDto(student);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<StudentDTO> expectedStudentDTOList = new ArrayList<>(singletonList(studentDTO1));
        assertThat(expectedStudentDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
