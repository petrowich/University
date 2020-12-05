package ru.petrowich.university.controller.students;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.StudentService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class StudentControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer GROUP_ID_503 = 503;
    private static final Integer GROUP_ID_504 = 504;

    @Mock
    GroupService mockGroupService;

    @Mock
    StudentService mockStudentService;

    @InjectMocks
    StudentController studentController;

    @BeforeEach
    private void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(studentController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testStudents() throws Exception {
        Student firstStudent = new Student().setId(PERSON_ID_50001).setActive(true).setLastName("2");
        Student secondStudent = new Student().setId(PERSON_ID_50002).setActive(true).setLastName("1");
        List<Student> students = asList(firstStudent, secondStudent);

        when(mockStudentService.getAll()).thenReturn(students);

        List<Student> expectedStudent = asList(secondStudent, firstStudent);
        String expectedViewName = "students/students";

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allStudents", expectedStudent))
                .andExpect(view().name(expectedViewName));

        verify(mockStudentService, times(1)).getAll();
    }

    @Test
    void testStudent() throws Exception {
        Student expectedStudent = new Student().setId(PERSON_ID_50001);

        when(mockStudentService.getById(PERSON_ID_50001)).thenReturn(expectedStudent);

        String expectedViewName = "students/student";

        mockMvc.perform(get("/students/student").param("id", String.valueOf(PERSON_ID_50001)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", expectedStudent))
                .andExpect(view().name(expectedViewName));

        verify(mockStudentService, times(1)).getById(PERSON_ID_50001);
    }

    @Test
    void edit() throws Exception {
        Group firstGroup = new Group().setId(GROUP_ID_501).setActive(true);
        Group secondGroup = new Group().setId(GROUP_ID_502).setActive(false);
        Group thirdGroup = new Group().setId(GROUP_ID_503).setActive(true).setName("2");
        Group fourthGroup = new Group().setId(GROUP_ID_504).setActive(true).setName("1");
        List<Group> groups = asList(firstGroup, secondGroup, thirdGroup, fourthGroup);

        when(mockGroupService.getAll()).thenReturn(groups);

        Student expectedStudent = new Student().setId(PERSON_ID_50001).setGroup(firstGroup);
        when(mockStudentService.getById(PERSON_ID_50001)).thenReturn(expectedStudent);

        List<Group> expectedGroups = asList(fourthGroup, thirdGroup, new Group());
        String expectedViewName = "students/student_editor";

        mockMvc.perform(get("/students/student/edit").param("id", String.valueOf(PERSON_ID_50001)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", expectedStudent))
                .andExpect(model().attribute("groups", expectedGroups))
                .andExpect(view().name(expectedViewName));

        verify(mockStudentService, times(1)).getById(PERSON_ID_50001);
    }

    @Test
    void testEditWithoutPreAssignments() throws Exception {
        Group firstGroup = new Group().setId(GROUP_ID_501).setActive(true).setName("2");
        Group secondGroup = new Group().setId(GROUP_ID_502).setActive(false);
        Group thirdGroup = new Group().setId(GROUP_ID_503).setActive(true).setName("1");
        List<Group> groups = asList(firstGroup, secondGroup, thirdGroup);

        when(mockGroupService.getAll()).thenReturn(groups);

        String expectedViewName = "students/student_editor";

        Student expectedStudent = new Student().setId(PERSON_ID_50001).setGroup(new Group());
        when(mockStudentService.getById(PERSON_ID_50001)).thenReturn(expectedStudent);

        List<Group> expectedGroups = asList(thirdGroup, firstGroup);

        mockMvc.perform(get("/students/student/edit").param("id", String.valueOf(PERSON_ID_50001)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", expectedStudent))
                .andExpect(model().attribute("groups", expectedGroups))
                .andExpect(view().name(expectedViewName));

        verify(mockStudentService, times(1)).getById(PERSON_ID_50001);
    }

    @Test
    void testUpdate() throws Exception {
        Student expectedStudent = new Student().setId(PERSON_ID_50001);

        String expectedViewName = "students/students";

        mockMvc.perform(post("/students/student/update").flashAttr("student", expectedStudent).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockStudentService, times(1)).update(expectedStudent);
    }

    @Test
    void testUpdateShouldReturnBadRequestWhenResultHasErrors() throws Exception {
        String expectedViewName = "students/students";

        mockMvc.perform(post("/students/student/update")
                .param("id", "wrong parameter value")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testCreate() throws Exception {
        Group firstGroup = new Group().setId(GROUP_ID_501).setActive(true).setName("2");
        Group secondGroup = new Group().setId(GROUP_ID_502).setActive(false);
        Group thirdGroup = new Group().setId(GROUP_ID_503).setActive(true).setName("1");
        List<Group> groups = asList(firstGroup, secondGroup, thirdGroup);

        when(mockGroupService.getAll()).thenReturn(groups);

        Student expectedStudent = new Student();

        List<Group> expectedGroups = asList(thirdGroup, firstGroup);
        String expectedViewName = "students/student_creator";

        mockMvc.perform(get("/students/student/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", expectedStudent))
                .andExpect(model().attribute("groups", expectedGroups))
                .andExpect(view().name(expectedViewName));

        verify(mockGroupService, times(1)).getAll();
    }

    @Test
    void testAdd() throws Exception {
        Student expectedStudent = new Student().setActive(true);

        String expectedViewName = "students/students";

        mockMvc.perform(post("/students/student/add").flashAttr("students", expectedStudent).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockStudentService, times(1)).add(expectedStudent);
    }
}
