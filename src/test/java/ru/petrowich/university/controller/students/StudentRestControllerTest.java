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
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class StudentRestControllerTest {
    private static final Integer NEW_PERSON_ID = 50007;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String NEW_PERSON_FIRST_NAME = "Улов";
    private static final String NEW_PERSON_LAST_NAME = "Налимов";
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final Integer EXISTENT_PERSON_ID_50002 = 50002;
    private static final Integer EXISTENT_PERSON_ID_50003 = 50003;
    private static final String EXISTENT_PERSON_FIRST_NAME_50001 = "Рулон";
    private static final String EXISTENT_PERSON_FIRST_NAME_50002 = "Обвал";
    private static final String EXISTENT_PERSON_FIRST_NAME_50003 = "Рекорд";
    private static final String EXISTENT_PERSON_LAST_NAME_50001 = "Обоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50002 = "Забоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50003 = "Надоев";
    private static final String EXISTENT_PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50002 = "obval.zaboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50003 = "record.nadoev@university.edu";
    private static final String ANOTHER_PERSON_EMAIL = "another@mail.com";
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final String EXISTENT_GROUP_NAME_501 = "AA-01";
    private static final String EXISTENT_GROUP_NAME_502 = "BB-02";
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final String EXISTENT_COURSE_NAME_51 = "math";
    private static final String EXISTENT_COURSE_NAME_52 = "biology";
    private static final String EXISTENT_COURSE_NAME_53 = "physics";

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final StudentMapper studentMapper = new StudentMapper(modelMapper);

    private final Course course1 = new Course().setId(EXISTENT_COURSE_ID_51).setName(EXISTENT_COURSE_NAME_51).setActive(true);
    private final Course course2 = new Course().setId(EXISTENT_COURSE_ID_52).setName(EXISTENT_COURSE_NAME_52).setActive(true);
    private final Course course3 = new Course().setId(EXISTENT_COURSE_ID_53).setName(EXISTENT_COURSE_NAME_53).setActive(false);

    private final List<Course> courseList1 = new ArrayList<>(asList(course1, course2));
    private final List<Course> courseList2 = new ArrayList<>(singletonList(course3));
    
    private final Group group1 = new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setCourses(courseList1).setActive(true);
    private final Group group2 = new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setCourses(courseList2).setActive(true);

    private final Student newStudent = new Student()
            .setFirstName(NEW_PERSON_FIRST_NAME).setLastName(NEW_PERSON_LAST_NAME)
            .setActive(true);

    private final Student student1 = new Student().setId(EXISTENT_PERSON_ID_50001)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50001).setLastName(EXISTENT_PERSON_LAST_NAME_50001)
            .setEmail(EXISTENT_PERSON_EMAIL_50001).setGroup(group1)
            .setActive(true);

    private final Student student2 = new Student().setId(EXISTENT_PERSON_ID_50002)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50002).setLastName(EXISTENT_PERSON_LAST_NAME_50002)
            .setEmail(EXISTENT_PERSON_EMAIL_50002).setGroup(group1)
            .setActive(true);

    private final Student student3 = new Student().setId(EXISTENT_PERSON_ID_50003)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50003).setLastName(EXISTENT_PERSON_LAST_NAME_50003)
            .setEmail(EXISTENT_PERSON_EMAIL_50003).setGroup(group2)
            .setActive(true);

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
        StudentDTO studentDTO = studentMapper.toDto(student1);

        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student1);
        when(mockStudentMapper.toDto(student1)).thenReturn(studentDTO);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.getStudent(EXISTENT_PERSON_ID_50001);

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentMapper, times(1)).toDto(student1);
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
        StudentDTO studentDTO = studentMapper.toDto(student1);
        Student expectedStudent = studentMapper.toEntity(studentDTO).setEmail(ANOTHER_PERSON_EMAIL);

        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student1);
        when(mockStudentMapper.toEntity(studentDTO)).thenReturn(student1);
        when(mockStudentService.update(student1)).thenReturn(expectedStudent);

        StudentDTO expectedStudentDTO = studentMapper.toDto(expectedStudent);
        when(mockStudentMapper.toDto(expectedStudent)).thenReturn(expectedStudentDTO);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.updateStudent(studentDTO, EXISTENT_PERSON_ID_50001);

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentMapper, times(1)).toEntity(studentDTO);
        verify(mockStudentService, times(1)).update(student1);
        verify(mockStudentMapper, times(1)).toDto(expectedStudent);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedStudentDTO);
    }

    @Test
    void testUpdateStudentShouldReturnBadRequestWhenNullIdPassed() {
        StudentDTO studentDTO = studentMapper.toDto(student1);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.updateStudent(studentDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateStudentShouldReturnNotFoundWhenNonexistentIdPassed() {
        StudentDTO studentDTO = studentMapper.toDto(student1);

        when(mockStudentService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.updateStudent(studentDTO, NONEXISTENT_PERSON_ID);

        verify(mockStudentService, times(1)).getById(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteStudentShouldReturnOK() {
        when(mockStudentService.getById(EXISTENT_PERSON_ID_50001)).thenReturn(student1);

        ResponseEntity<StudentDTO> responseEntity = studentRestController.deleteStudent(EXISTENT_PERSON_ID_50001);

        verify(mockStudentService, times(1)).getById(EXISTENT_PERSON_ID_50001);
        verify(mockStudentService, times(1)).delete(student1);
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
        StudentDTO studentDTO1 = studentMapper.toDto(student1);
        StudentDTO studentDTO2 = studentMapper.toDto(student2);

        List<Student> students = new ArrayList<>(asList(student1, student2));

        when(mockStudentService.getAll()).thenReturn(students);
        when(mockStudentMapper.toDto(student1)).thenReturn(studentDTO1);
        when(mockStudentMapper.toDto(student2)).thenReturn(studentDTO2);

        ResponseEntity<List<StudentDTO>> responseEntity = studentRestController.getAllStudents();

        verify(mockStudentService, times(1)).getAll();
        verify(mockStudentMapper, times(1)).toDto(student1);
        verify(mockStudentMapper, times(1)).toDto(student2);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<StudentDTO> expectedStudentDTOList = new ArrayList<>(asList(studentDTO1, studentDTO2));
        assertThat(expectedStudentDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
