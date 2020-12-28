package ru.petrowich.university.controller.lecturers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class LecturerControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;

    @Mock
    LecturerService mockLecturerService;

    @InjectMocks
    LecturerController lecturerController;

    @BeforeEach
    private void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(lecturerController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testLecturers() throws Exception {
        Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50005).setActive(true).setFirstName("2");
        Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50006).setActive(true).setFirstName("1");
        List<Lecturer> lecturers = asList(firstLecturer, secondLecturer);

        when(mockLecturerService.getAll()).thenReturn(lecturers);

        List<Lecturer> expectedLecturers = asList(secondLecturer, firstLecturer);
        String expectedViewName = "lecturers/lecturers";

        mockMvc.perform(get("/lecturers"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allLecturers", expectedLecturers))
                .andExpect(view().name(expectedViewName));

        verify(mockLecturerService, times(1)).getAll();
    }

    @Test
    void testLecturer() throws Exception {
        Lecturer expectedLecturer = new Lecturer().setId(PERSON_ID_50005);

        when(mockLecturerService.getById(PERSON_ID_50005)).thenReturn(expectedLecturer);

        String expectedViewName = "lecturers/lecturer";

        mockMvc.perform(get("/lecturers/lecturer").param("id", String.valueOf(PERSON_ID_50005)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lecturer", expectedLecturer))
                .andExpect(view().name(expectedViewName));

        verify(mockLecturerService, times(1)).getById(PERSON_ID_50005);
    }

    @Test
    void testEdit() throws Exception {
        Lecturer expectedLecturer = new Lecturer().setId(PERSON_ID_50005);

        when(mockLecturerService.getById(PERSON_ID_50005)).thenReturn(expectedLecturer);

        String expectedViewName = "lecturers/lecturer_editor";

        mockMvc.perform(get("/lecturers/lecturer/edit").param("id", String.valueOf(PERSON_ID_50005)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lecturer", expectedLecturer))
                .andExpect(view().name(expectedViewName));

        verify(mockLecturerService, times(1)).getById(PERSON_ID_50005);
    }

    @Test
    void testUpdate() throws Exception {
        Lecturer expectedLecturer = new Lecturer().setId(PERSON_ID_50005);

        String expectedViewName = "lecturers/lecturers";

        mockMvc.perform(post("/lecturers/lecturer/update").flashAttr("lecturer", expectedLecturer).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockLecturerService, times(1)).update(expectedLecturer);
    }

    @Test
    void testUpdateShouldReturnBadRequestWhenResultHasErrors() throws Exception {
        String expectedViewName = "lecturers/lecturers";

        mockMvc.perform(post("/lecturers/lecturer/update")
                .param("id", "wrong parameter value")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testCreate() throws Exception {
        Lecturer expectedLecturer = new Lecturer();

        String expectedViewName = "lecturers/lecturer_creator";

        mockMvc.perform(get("/lecturers/lecturer/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lecturer", expectedLecturer))
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testAdd() throws Exception {
        Lecturer expectedLecturer = new Lecturer().setActive(true);

        String expectedViewName = "lecturers/lecturers";

        mockMvc.perform(post("/lecturers/lecturer/add").flashAttr("lecturer", expectedLecturer).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockLecturerService, times(1)).add(expectedLecturer);
    }
}
