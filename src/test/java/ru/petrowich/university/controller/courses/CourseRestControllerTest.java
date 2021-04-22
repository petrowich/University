package ru.petrowich.university.controller.courses;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.modelmapper.ModelMapper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.petrowich.university.dto.courses.CourseDTO;
import ru.petrowich.university.dto.courses.CourseGroupAssignmentDTO;
import ru.petrowich.university.dto.courses.CourseGroupDTO;
import ru.petrowich.university.mapper.courses.CourseGroupMapper;
import ru.petrowich.university.mapper.courses.CourseMapper;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static java.util.Collections.singletonList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CourseRestControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer NEW_COURSE_ID = 54;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer NONEXISTENT_GROUP_ID = 999;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ModelMapper modelMapper = new ModelMapper();
    private final CourseMapper courseMapper = new CourseMapper(modelMapper);
    private final CourseGroupMapper courseGroupMapper = new CourseGroupMapper(modelMapper);

    @Mock
    private CourseService mockCourseService;

    @Mock
    private GroupService mockGroupService;

    @Mock
    private CourseMapper mockCourseMapper;

    @Mock
    private CourseGroupMapper mockCourseGroupMapper;

    @InjectMocks
    private CourseRestController courseRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
        mockMvc = standaloneSetup(courseRestController).build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetCourseShouldReturnOK() throws Exception {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        CourseDTO courseDTO = courseMapper.toDto(course);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);
        when(mockCourseMapper.toDto(course)).thenReturn(courseDTO);

        mockMvc.perform(get("/api/courses/{id}", EXISTENT_COURSE_ID_51)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_COURSE_ID_51));

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseMapper, times(1)).toDto(course);
    }

    @Test
    void testGetCourseShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);

        mockMvc.perform(get("/api/courses/{id}", NONEXISTENT_COURSE_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddCourseShouldReturnCreated() throws Exception {
        Course newCourse = new Course();
        CourseDTO newCourseDTO = courseMapper.toDto(newCourse);
        String newCourseJSON = objectMapper.writeValueAsString(newCourseDTO);

        Course expectedCourse = courseMapper.toEntity(newCourseDTO).setId(NEW_COURSE_ID);

        when(mockCourseMapper.toEntity(newCourseDTO)).thenReturn(newCourse);
        when(mockCourseService.add(newCourse)).thenReturn(expectedCourse);

        CourseDTO expectedCourseDTO = courseMapper.toDto(expectedCourse);
        when(mockCourseMapper.toDto(expectedCourse)).thenReturn(expectedCourseDTO);

        mockMvc.perform(post("/api/courses/add")
                .content(newCourseJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_COURSE_ID));

        verify(mockCourseMapper, times(1)).toEntity(newCourseDTO);
        verify(mockCourseService, times(1)).add(newCourse);
        verify(mockCourseMapper, times(1)).toDto(expectedCourse);
    }

    @Test
    void testUpdateCourseShouldReturnOK() throws Exception {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51).setAuthor(new Lecturer().setId(EXISTENT_PERSON_ID_50005));
        CourseDTO courseDTO = courseMapper.toDto(course);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(new Course().setId(EXISTENT_COURSE_ID_51));
        when(mockCourseMapper.toEntity(courseDTO)).thenReturn(course);
        when(mockCourseService.update(course)).thenReturn(course);
        when(mockCourseMapper.toDto(course)).thenReturn(courseDTO);

        String courseJSON = objectMapper.writeValueAsString(courseDTO);

        mockMvc.perform(put("/api/courses/update/{id}", EXISTENT_COURSE_ID_51)
                .content(courseJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_COURSE_ID_51))
                .andExpect(jsonPath("$.authorId").value(EXISTENT_PERSON_ID_50005));

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseMapper, times(1)).toEntity(courseDTO);
        verify(mockCourseService, times(1)).update(course);
        verify(mockCourseMapper, times(1)).toDto(course);
    }

    @Test
    void testUpdateCourseShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        Course course = new Course().setId(NONEXISTENT_COURSE_ID);
        CourseDTO courseDTO = courseMapper.toDto(course);
        String courseJSON = objectMapper.writeValueAsString(courseDTO);

        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);

        mockMvc.perform(put("/api/courses/update/{id}", NONEXISTENT_COURSE_ID)
                .content(courseJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
    }

    @Test
    void testDeleteCourseShouldReturnOK() throws Exception {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);

        mockMvc.perform(delete("/api/courses/delete/{id}", EXISTENT_COURSE_ID_51))
                .andExpect(status().isOk());

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseService, times(1)).delete(course);
    }

    @Test
    void testDeleteCourseShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/courses/delete/{id}", NONEXISTENT_COURSE_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCoursesShouldReturnOK() throws Exception {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        CourseDTO courseDTO = courseMapper.toDto(course);
        List<Course> courses = new ArrayList<>(singletonList(course));

        when(mockCourseService.getAll()).thenReturn(courses);
        when(mockCourseMapper.toDto(course)).thenReturn(courseDTO);

        mockMvc.perform(get("/api/courses/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_COURSE_ID_51));

        verify(mockCourseService, times(1)).getAll();
        verify(mockCourseMapper, times(1)).toDto(course);
    }

    @Test
    void testAssignGroupShouldReturnOK() throws Exception {
        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(EXISTENT_COURSE_ID_51)
                .setGroupId(EXISTENT_GROUP_ID_501);
        String courseGroupAssignmentJSON = objectMapper.writeValueAsString(courseGroupAssignmentDTO);

        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);
        List<Group> groupList = singletonList(group);

        Course course = new Course().setId(EXISTENT_COURSE_ID_51).setGroups(groupList);
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);

        CourseGroupDTO courseGroupDTO = courseGroupMapper.toDto(group);
        when(mockCourseGroupMapper.toDto(group)).thenReturn(courseGroupDTO);

        mockMvc.perform(put("/api/courses/assign-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_GROUP_ID_501));

        verify(mockCourseService, times(1)).assignGroupToCourse(group, course);
        verify(mockCourseService, times(2)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockCourseGroupMapper, times(1)).toDto(group);
    }

    @Test
    void testAssignGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(NONEXISTENT_COURSE_ID)
                .setGroupId(NONEXISTENT_GROUP_ID);
        String courseGroupAssignmentJSON = objectMapper.writeValueAsString(courseGroupAssignmentDTO);

        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        mockMvc.perform(put("/api/courses/assign-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
    }

    @Test
    void testRemoveGroupShouldReturnOK() throws Exception {
        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(EXISTENT_COURSE_ID_51)
                .setGroupId(EXISTENT_GROUP_ID_501);

        String courseGroupAssignmentJSON = objectMapper.writeValueAsString(courseGroupAssignmentDTO);

        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);
        List<Group> groupList = singletonList(group);

        Course course = new Course().setId(EXISTENT_COURSE_ID_51).setGroups(groupList);
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);

        CourseGroupDTO courseGroupDTO = courseGroupMapper.toDto(group);
        when(mockCourseGroupMapper.toDto(group)).thenReturn(courseGroupDTO);

        mockMvc.perform(put("/api/courses/remove-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_GROUP_ID_501));

        verify(mockCourseService, times(1)).removeGroupFromCourse(group, course);
        verify(mockCourseService, times(2)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockCourseGroupMapper, times(1)).toDto(group);
    }

    @Test
    void testRemoveGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(NONEXISTENT_COURSE_ID)
                .setGroupId(NONEXISTENT_GROUP_ID);
        String courseGroupAssignmentJSON = objectMapper.writeValueAsString(courseGroupAssignmentDTO);

        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        mockMvc.perform(put("/api/courses/remove-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
    }
}
