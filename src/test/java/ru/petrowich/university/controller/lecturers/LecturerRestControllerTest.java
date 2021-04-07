package ru.petrowich.university.controller.lecturers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.petrowich.university.dto.lecturers.LecturerDTO;
import ru.petrowich.university.mapper.lecturers.LecturerCourseMapper;
import ru.petrowich.university.mapper.lecturers.LecturerMapper;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class LecturerRestControllerTest {
    private static final Integer NEW_PERSON_ID = 50007;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_PERSON_ID_50006 = 50006;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String NEW_PERSON_FIRST_NAME = "new lecturer some name";
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Отряд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50006 = "Ушат";
    private static final String NEW_PERSON_LAST_NAME = "new lecturer some last name";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Ковбоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50006 = "Помоев";
    private static final String EXISTENT_PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final String EXISTENT_PERSON_EMAIL_50006 = "ushat.pomoev@university.edu";
    private static final String ANOTHER_PERSON_EMAIL = "another@mail.com";
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final String EXISTENT_COURSE_NAME_51 = "math";
    private static final String EXISTENT_COURSE_NAME_52 = "biology";
    private static final String EXISTENT_COURSE_NAME_53 = "physics";
    private static final String EXISTENT_COURSE_DESCRIPTION_51 = "exact";
    private static final String EXISTENT_COURSE_DESCRIPTION_52 = "natural";
    private static final String EXISTENT_COURSE_DESCRIPTION_53 = "exact";

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final LecturerMapper lecturerMapper = new LecturerMapper(modelMapper);
    private final LecturerCourseMapper lecturerCourseMapper = new LecturerCourseMapper(modelMapper);

    private final Course course1 = new Course().setId(EXISTENT_COURSE_ID_51)
            .setName(EXISTENT_COURSE_NAME_51).setDescription(EXISTENT_COURSE_DESCRIPTION_51).setActive(true);

    private final Course course2 = new Course().setId(EXISTENT_COURSE_ID_52)
            .setName(EXISTENT_COURSE_NAME_52).setDescription(EXISTENT_COURSE_DESCRIPTION_52).setActive(true);

    private final Course course3 = new Course().setId(EXISTENT_COURSE_ID_53)
            .setName(EXISTENT_COURSE_NAME_53).setDescription(EXISTENT_COURSE_DESCRIPTION_53).setActive(false);

    private final List<Course> courseList1 = new ArrayList<>(asList(course1, course2));
    private final List<Course> courseList2 = new ArrayList<>(singletonList(course3));

    private final Lecturer newLecturer = new Lecturer()
            .setFirstName(NEW_PERSON_FIRST_NAME).setLastName(NEW_PERSON_LAST_NAME)
            .setActive(true);

    private final Lecturer lecturer1 = new Lecturer().setId(EXISTENT_PERSON_ID_50005)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005).setLastName(EXISTENT_PERSON_LAST_NAME_50005)
            .setEmail(EXISTENT_PERSON_EMAIL_50005).setCourses(courseList1)
            .setActive(true);

    private final Lecturer lecturer2 = new Lecturer().setId(EXISTENT_PERSON_ID_50006)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50006).setLastName(EXISTENT_PERSON_LAST_NAME_50006)
            .setEmail(EXISTENT_PERSON_EMAIL_50006).setCourses(courseList2)
            .setActive(true);

    @Mock
    LecturerService mockLecturerService;

    @Mock
    LecturerMapper mockLecturerMapper;

    @InjectMocks
    LecturerRestController lecturerRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetLecturerShouldReturnOK() {
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer1);

        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer1);
        when(mockLecturerMapper.toDto(lecturer1)).thenReturn(lecturerDTO);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.getLecturer(EXISTENT_PERSON_ID_50005);

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerMapper, times(1)).toDto(lecturer1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(lecturerDTO);
    }

    @Test
    void testGetLecturerShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.getLecturer(null);

        verify(mockLecturerService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetLecturerShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.getLecturer(NONEXISTENT_PERSON_ID);

        verify(mockLecturerService, times(1)).getById(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddLecturerShouldReturnCreated() {
        LecturerDTO newLecturerDTO = lecturerMapper.toDto(newLecturer);
        Lecturer expectedLecturer = lecturerMapper.toEntity(newLecturerDTO).setId(NEW_PERSON_ID);

        when(mockLecturerMapper.toEntity(newLecturerDTO)).thenReturn(newLecturer);
        when(mockLecturerService.add(newLecturer)).thenReturn(expectedLecturer);

        LecturerDTO expectedLecturerDTO = lecturerMapper.toDto(expectedLecturer);
        when(mockLecturerMapper.toDto(expectedLecturer)).thenReturn(expectedLecturerDTO);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.addLecturer(newLecturerDTO);

        verify(mockLecturerMapper, times(1)).toEntity(newLecturerDTO);
        verify(mockLecturerService, times(1)).add(newLecturer);
        verify(mockLecturerMapper, times(1)).toDto(expectedLecturer);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLecturerDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateLecturerShouldReturnOK() {
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer1);
        Lecturer expectedLecturer = lecturerMapper.toEntity(lecturerDTO).setEmail(ANOTHER_PERSON_EMAIL);

        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer1);
        when(mockLecturerMapper.toEntity(lecturerDTO)).thenReturn(lecturer1);
        when(mockLecturerService.update(lecturer1)).thenReturn(expectedLecturer);

        LecturerDTO expectedLecturerDTO = lecturerMapper.toDto(expectedLecturer);
        when(mockLecturerMapper.toDto(expectedLecturer)).thenReturn(expectedLecturerDTO);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.updateLecturer(lecturerDTO, EXISTENT_PERSON_ID_50005);

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerMapper, times(1)).toEntity(lecturerDTO);
        verify(mockLecturerService, times(1)).update(lecturer1);
        verify(mockLecturerMapper, times(1)).toDto(expectedLecturer);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLecturerDTO);
    }

    @Test
    void testUpdateLecturerShouldReturnBadRequestWhenNullIdPassed() {
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer1);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.updateLecturer(lecturerDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateLecturerShouldReturnNotFoundWhenNonexistentIdPassed() {
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer1);

        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.updateLecturer(lecturerDTO, NONEXISTENT_PERSON_ID);

        verify(mockLecturerService, times(1)).getById(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteLecturerShouldReturnOK() {
        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer1);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.deleteLecturer(EXISTENT_PERSON_ID_50005);

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerService, times(1)).delete(lecturer1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteLecturerShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.deleteLecturer(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteLecturerShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);
        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.deleteLecturer(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllLecturersShouldReturnOK() {
        LecturerDTO lecturerDTO1 = lecturerMapper.toDto(lecturer1);
        LecturerDTO lecturerDTO2 = lecturerMapper.toDto(lecturer2);

        List<Lecturer> lecturers = new ArrayList<>(asList(lecturer1, lecturer2));

        when(mockLecturerService.getAll()).thenReturn(lecturers);
        when(mockLecturerMapper.toDto(lecturer1)).thenReturn(lecturerDTO1);
        when(mockLecturerMapper.toDto(lecturer2)).thenReturn(lecturerDTO2);

        ResponseEntity<List<LecturerDTO>> responseEntity = lecturerRestController.getAllLecturers();

        verify(mockLecturerService, times(1)).getAll();
        verify(mockLecturerMapper, times(1)).toDto(lecturer1);
        verify(mockLecturerMapper, times(1)).toDto(lecturer2);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<LecturerDTO> expectedLecturerDTOList = new ArrayList<>(asList(lecturerDTO1, lecturerDTO2));
        assertThat(expectedLecturerDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
