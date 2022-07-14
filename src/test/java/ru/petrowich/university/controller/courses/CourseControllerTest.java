package ru.petrowich.university.controller.courses;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.LecturerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CourseControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer COURSE_ID_51 = 51;
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer GROUP_ID_503 = 503;
    private static final Integer GROUP_ID_504 = 504;
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer PERSON_ID_50003 = 50003;
    private static final Integer PERSON_ID_50004 = 50004;

    @Mock
    private CourseService mockCourseService;

    @Mock
    private GroupService mockGroupService;

    @Mock
    private LecturerService mockLecturerService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
        mockMvc = standaloneSetup(courseController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCourses() throws Exception {
        List<Course> expectedCourses = new ArrayList<>();

        String expectedViewName = "courses/courses";

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allCourses", expectedCourses))
                .andExpect(view().name(expectedViewName));

        verify(mockCourseService, times(1)).getAll();
    }

    @Test
    void testCourse() throws Exception {
        Group firstGroup = new Group().setId(GROUP_ID_501).setActive(false).setName("2");
        Group secondGroup = new Group().setId(GROUP_ID_502).setActive(true).setName("1");
        Group thirdGroup = new Group().setId(GROUP_ID_503).setActive(true);
        Group fourthGroup = new Group().setId(GROUP_ID_504).setActive(false);

        List<Group> courseGroups = asList(firstGroup, secondGroup);
        Course expectedCourse = new Course().setId(COURSE_ID_51).setGroups(courseGroups);
        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(expectedCourse);

        List<Group> expectedGroups = asList(firstGroup, secondGroup, thirdGroup, fourthGroup);
        when(mockGroupService.getAll()).thenReturn(expectedGroups);

        List<Group> expectedCourseGroups = asList(secondGroup, firstGroup);
        List<Group> expectedRestGroups = singletonList(thirdGroup);
        String expectedViewName = "courses/course";

        mockMvc.perform(get("/courses/course").param("id", String.valueOf(COURSE_ID_51)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("course", expectedCourse))
                .andExpect(model().attribute("courseGroups", expectedCourseGroups))
                .andExpect(model().attribute("restGroups", expectedRestGroups))
                .andExpect(view().name(expectedViewName));

        verify(mockCourseService, times(1)).getById(COURSE_ID_51);
        verify(mockGroupService, times(1)).getAll();
    }

    @Test
    void testEdit() throws Exception {
        Lecturer fistLecturer = new Lecturer().setId(PERSON_ID_50001).setActive(true);
        Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50002).setActive(false);
        Lecturer thirdLecturer = new Lecturer().setId(PERSON_ID_50003).setActive(true).setFirstName("2");
        Lecturer fourthLecturer = new Lecturer().setId(PERSON_ID_50004).setActive(true).setFirstName("1");

        List<Lecturer> lecturers = asList(fistLecturer, secondLecturer, thirdLecturer, fourthLecturer);
        when(mockLecturerService.getAll()).thenReturn(lecturers);

        Course expectedCourse = new Course().setId(COURSE_ID_51).setAuthor(fistLecturer);
        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(expectedCourse);

        List<Lecturer> expectedLecturers = asList(fourthLecturer, thirdLecturer, new Lecturer());
        String expectedViewName = "courses/course_editor";

        mockMvc.perform(get("/courses/course/edit").param("id", String.valueOf(COURSE_ID_51)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("course", expectedCourse))
                .andExpect(model().attribute("lecturers", expectedLecturers))
                .andExpect(view().name(expectedViewName));

        verify(mockCourseService, times(1)).getById(COURSE_ID_51);
        verify(mockLecturerService, times(1)).getAll();
    }

    @Test
    void testEditWithoutPreAssignments() throws Exception {
        Course expectedCourse = new Course().setId(COURSE_ID_51);
        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(expectedCourse);

        Lecturer fistLecturer = new Lecturer().setId(PERSON_ID_50001).setActive(true).setFirstName("2");
        Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50002).setActive(false);
        Lecturer thirdLecturer = new Lecturer().setId(PERSON_ID_50003).setActive(true).setFirstName("1");

        List<Lecturer> lecturers = asList(fistLecturer, secondLecturer, thirdLecturer);
        when(mockLecturerService.getAll()).thenReturn(lecturers);

        List<Lecturer> expectedLecturers = asList(thirdLecturer, fistLecturer);
        String expectedViewName = "courses/course_editor";

        mockMvc.perform(get("/courses/course/edit").param("id", String.valueOf(COURSE_ID_51)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("course", expectedCourse))
                .andExpect(model().attribute("lecturers", expectedLecturers))
                .andExpect(view().name(expectedViewName));

        verify(mockCourseService, times(1)).getById(COURSE_ID_51);
        verify(mockLecturerService, times(1)).getAll();
    }

    @Test
    void testUpdate() throws Exception {
        Group firstGroup = new Group().setId(GROUP_ID_501).setActive(false).setName("2");
        Group secondGroup = new Group().setId(GROUP_ID_502).setActive(true).setName("1");

        List<Group> courseGroups = asList(firstGroup, secondGroup);
        Course expectedCourse = new Course().setId(COURSE_ID_51).setGroups(courseGroups);
        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(expectedCourse);

        String expectedViewName = "courses/courses";

        mockMvc.perform(post("/courses/course/update").flashAttr("course", expectedCourse).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/"));

        verify(mockCourseService, times(1)).update(expectedCourse);
    }

    @Test
    void testUpdateShouldReturnBadRequestWhenResultHasErrors() throws Exception {
        String expectedViewName = "courses/courses";

        mockMvc.perform(post("/courses/course/update")
                .param("id", "wrong parameter value")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testCreate() throws Exception {
        Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50001).setActive(true).setFirstName("z");
        Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50002).setActive(false);
        Lecturer thirdLecturer = new Lecturer().setId(PERSON_ID_50003).setActive(true).setFirstName("a");

        List<Lecturer> lecturers = asList(firstLecturer, secondLecturer, thirdLecturer);

        when(mockLecturerService.getAll()).thenReturn(lecturers);

        String expectedViewName = "courses/course_creator";

        Course expectedCourse = new Course();
        List<Lecturer> expectedLecturers = asList(thirdLecturer, firstLecturer);

        mockMvc.perform(get("/courses/course/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("course", expectedCourse))
                .andExpect(model().attribute("lecturers", expectedLecturers))
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void testAdd() throws Exception {
        Course expectedCourse = new Course().setActive(true);

        String expectedViewName = "courses/courses";

        mockMvc.perform(post("/courses/course/add").flashAttr("course", expectedCourse).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/"));

        verify(mockCourseService, times(1)).add(expectedCourse);
    }

    @Test
    void testAssignGroup() throws Exception {
        Course course = new Course().setId(COURSE_ID_51);
        Group group = new Group().setId(GROUP_ID_501);
        List<Group> expectedGroups = new ArrayList<>();

        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(course);
        String expectedViewName = "courses/course";

        mockMvc.perform(post("/courses/course/assign-group")
                .param("courseId", String.valueOf(COURSE_ID_51))
                .param("groupId", String.valueOf(GROUP_ID_501)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/course?id=51"));

        verify(mockCourseService, times(1)).assignGroupToCourse(group, course);
    }

    @Test
    void testRemoveGroup() throws Exception {
        Course expectedCourse = new Course().setId(COURSE_ID_51);
        Group group = new Group().setId(GROUP_ID_501);
        List<Group> expectedGroups = new ArrayList<>();

        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(expectedCourse);
        String expectedViewName = "courses/course";

        mockMvc.perform(post("/courses/course/remove-group")
                .param("courseId", String.valueOf(COURSE_ID_51))
                .param("groupId", String.valueOf(GROUP_ID_501)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses/course?id=51"));

        verify(mockCourseService, times(1)).removeGroupFromCourse(group, expectedCourse);
    }
}
