package ru.petrowich.university.controller.lecturers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.petrowich.university.dto.lecturers.LecturerDTO;
import ru.petrowich.university.mapper.lecturers.LecturerMapper;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.ArrayList;
import java.util.List;

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
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String ANOTHER_PERSON_EMAIL = "another@mail.com";

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final LecturerMapper lecturerMapper = new LecturerMapper(modelMapper);

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
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);

        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer);
        when(mockLecturerMapper.toDto(lecturer)).thenReturn(lecturerDTO);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.getLecturer(EXISTENT_PERSON_ID_50005);

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerMapper, times(1)).toDto(lecturer);
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
        Lecturer newLecturer = new Lecturer().setActive(true);
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
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);
        Lecturer expectedLecturer = lecturerMapper.toEntity(lecturerDTO).setEmail(ANOTHER_PERSON_EMAIL);

        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer);
        when(mockLecturerMapper.toEntity(lecturerDTO)).thenReturn(lecturer);
        when(mockLecturerService.update(lecturer)).thenReturn(expectedLecturer);

        LecturerDTO expectedLecturerDTO = lecturerMapper.toDto(expectedLecturer);
        when(mockLecturerMapper.toDto(expectedLecturer)).thenReturn(expectedLecturerDTO);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.updateLecturer(lecturerDTO, EXISTENT_PERSON_ID_50005);

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerMapper, times(1)).toEntity(lecturerDTO);
        verify(mockLecturerService, times(1)).update(lecturer);
        verify(mockLecturerMapper, times(1)).toDto(expectedLecturer);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLecturerDTO);
    }

    @Test
    void testUpdateLecturerShouldReturnBadRequestWhenNullIdPassed() {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.updateLecturer(lecturerDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateLecturerShouldReturnNotFoundWhenNonexistentIdPassed() {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);

        when(mockLecturerService.getById(NONEXISTENT_PERSON_ID)).thenReturn(null);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.updateLecturer(lecturerDTO, NONEXISTENT_PERSON_ID);

        verify(mockLecturerService, times(1)).getById(NONEXISTENT_PERSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteLecturerShouldReturnOK() {
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        when(mockLecturerService.getById(EXISTENT_PERSON_ID_50005)).thenReturn(lecturer);

        ResponseEntity<LecturerDTO> responseEntity = lecturerRestController.deleteLecturer(EXISTENT_PERSON_ID_50005);

        verify(mockLecturerService, times(1)).getById(EXISTENT_PERSON_ID_50005);
        verify(mockLecturerService, times(1)).delete(lecturer);
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
        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        LecturerDTO lecturerDTO1 = lecturerMapper.toDto(lecturer);

        List<Lecturer> lecturers = new ArrayList<>(singletonList(lecturer));

        when(mockLecturerService.getAll()).thenReturn(lecturers);
        when(mockLecturerMapper.toDto(lecturer)).thenReturn(lecturerDTO1);

        ResponseEntity<List<LecturerDTO>> responseEntity = lecturerRestController.getAllLecturers();

        verify(mockLecturerService, times(1)).getAll();
        verify(mockLecturerMapper, times(1)).toDto(lecturer);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<LecturerDTO> expectedLecturerDTOList = new ArrayList<>(singletonList(lecturerDTO1));
        assertThat(expectedLecturerDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
