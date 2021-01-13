package ru.petrowich.university.controller.students;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class GroupControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;

    @Mock
    GroupService mockGroupService;

    @Mock
    CourseService mockCourseService;

    @InjectMocks
    GroupController groupController;

    @BeforeEach
    private void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(groupController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGroups() throws Exception {
        Group firstGroup = new Group().setId(GROUP_ID_501).setActive(true).setName("2");
        Group secondGroup = new Group().setId(GROUP_ID_502).setActive(true).setName("1");
        List<Group> groups = asList(firstGroup, secondGroup);

        when(mockGroupService.getAll()).thenReturn(groups);

        String expectedViewName = "students/groups";

        List<Group> expectedGroups = asList(secondGroup, firstGroup);

        mockMvc.perform(get("/students/groups"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allGroups", expectedGroups))
                .andExpect(view().name(expectedViewName));

        verify(mockGroupService, times(1)).getAll();
    }

    @Test
    void testGroup() throws Exception {
        Course firstCourse = new Course().setId(COURSE_ID_51).setName("2");
        Course secondCourse = new Course().setId(COURSE_ID_52).setName("1");
        List<Course> groupCourses = asList(firstCourse, secondCourse);

        Group expectedGroup = new Group().setId(GROUP_ID_501).setCourses(groupCourses);
        when(mockGroupService.getById(GROUP_ID_501)).thenReturn(expectedGroup);

        List<Course> expectedGroupCourses = asList(secondCourse, firstCourse);
        String expectedViewName = "students/group";

        mockMvc.perform(get("/students/group").param("id", String.valueOf(GROUP_ID_501)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("group", expectedGroup))
                .andExpect(model().attribute("courses", expectedGroupCourses))
                .andExpect(view().name(expectedViewName));

        verify(mockGroupService, times(1)).getById(GROUP_ID_501);
    }

    @Test
    void testEdit() throws Exception {
        Group expectedGroup = new Group().setId(GROUP_ID_501);
        when(mockGroupService.getById(GROUP_ID_501)).thenReturn(expectedGroup);

        String expectedViewName = "students/group_editor";

        mockMvc.perform(get("/students/group/edit").param("id", String.valueOf(GROUP_ID_501)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("group", expectedGroup))
                .andExpect(view().name(expectedViewName));

        verify(mockGroupService, times(1)).getById(GROUP_ID_501);
    }

    @Test
    void testUpdate() throws Exception {
        Course firstCourse = new Course().setId(COURSE_ID_51).setName("2");
        Course secondCourse = new Course().setId(COURSE_ID_52).setName("1");
        List<Course> groupCourses = asList(firstCourse, secondCourse);

        Group expectedGroup = new Group().setId(GROUP_ID_501).setCourses(groupCourses);
        when(mockGroupService.getById(GROUP_ID_501)).thenReturn(expectedGroup);

        String expectedViewName = "students/groups";

        mockMvc.perform(post("/students/group/update").flashAttr("group", expectedGroup).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockGroupService, times(1)).getById(GROUP_ID_501);
        verify(mockGroupService, times(1)).update(expectedGroup);
    }

    @Test
    void testUpdateShouldReturnBadRequestWhenResultHasErrors() throws Exception {
        String expectedViewName = "students/groups";

        mockMvc.perform(post("/students/group/update")
                .param("id", "wrong parameter value")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testCreate() throws Exception {
        Group expectedGroup = new Group();

        String expectedViewName = "students/group_creator";

        mockMvc.perform(get("/students/group/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("group", expectedGroup))
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testAdd() throws Exception {
        Group expectedGroup = new Group().setActive(true);

        String expectedViewName = "students/groups";

        mockMvc.perform(post("/students/group/add").flashAttr("group", expectedGroup).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockGroupService, times(1)).add(expectedGroup);
    }
}
