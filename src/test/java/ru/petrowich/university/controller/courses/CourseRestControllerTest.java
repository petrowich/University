package ru.petrowich.university.controller.courses;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class CourseRestControllerTest {
    private static final Integer NEW_COURSE_ID = 54;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer NONEXISTENT_GROUP_ID = 999;

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final CourseMapper courseMapper = new CourseMapper(modelMapper);
    private final CourseGroupMapper courseGroupMapper = new CourseGroupMapper(modelMapper);

    @Mock
    CourseService mockCourseService;

    @Mock
    GroupService mockGroupService;

    @Mock
    CourseMapper mockCourseMapper;

    @Mock
    CourseGroupMapper mockCourseGroupMapper;

    @InjectMocks
    CourseRestController courseRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetCourseShouldReturnOK() {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        CourseDTO courseDTO1 = courseMapper.toDto(course);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);
        when(mockCourseMapper.toDto(course)).thenReturn(courseDTO1);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.getCourse(EXISTENT_COURSE_ID_51);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseMapper, times(1)).toDto(course);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(courseDTO1);
    }

    @Test
    void testGetCourseShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<CourseDTO> responseEntity = courseRestController.getCourse(null);

        verify(mockCourseService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetCourseShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.getCourse(NONEXISTENT_COURSE_ID);

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddCourseShouldReturnCreated() {
        Course newCourse = new Course();
        CourseDTO newCourseDTO = courseMapper.toDto(newCourse);
        Course expectedCourse = courseMapper.toEntity(newCourseDTO).setId(NEW_COURSE_ID);

        when(mockCourseMapper.toEntity(newCourseDTO)).thenReturn(newCourse);
        when(mockCourseService.add(newCourse)).thenReturn(expectedCourse);

        CourseDTO expectedCourseDTO = courseMapper.toDto(expectedCourse);
        when(mockCourseMapper.toDto(expectedCourse)).thenReturn(expectedCourseDTO);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.addCourse(newCourseDTO);

        verify(mockCourseMapper, times(1)).toEntity(newCourseDTO);
        verify(mockCourseService, times(1)).add(newCourse);
        verify(mockCourseMapper, times(1)).toDto(expectedCourse);
        assertThat(responseEntity.getBody()).isEqualTo(expectedCourseDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateCourseShouldReturnOK() {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        CourseDTO courseDTO = courseMapper.toDto(course);

        Lecturer lecturer = new Lecturer().setId(EXISTENT_PERSON_ID_50005);
        Course expectedCourse = courseMapper.toEntity(courseDTO).setAuthor(lecturer);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);
        when(mockCourseMapper.toEntity(courseDTO)).thenReturn(course);
        when(mockCourseService.update(course)).thenReturn(expectedCourse);

        CourseDTO expectedCourseDTO = courseMapper.toDto(expectedCourse);
        when(mockCourseMapper.toDto(expectedCourse)).thenReturn(expectedCourseDTO);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.updateCourse(courseDTO, EXISTENT_COURSE_ID_51);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseMapper, times(1)).toEntity(courseDTO);
        verify(mockCourseService, times(1)).update(course);
        verify(mockCourseMapper, times(1)).toDto(expectedCourse);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedCourseDTO);
    }

    @Test
    void testUpdateCourseShouldReturnBadRequestWhenNullIdPassed() {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        CourseDTO courseDTO = courseMapper.toDto(course);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.updateCourse(courseDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateCourseShouldReturnNotFoundWhenNonexistentIdPassed() {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        CourseDTO courseDTO = courseMapper.toDto(course);

        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.updateCourse(courseDTO, NONEXISTENT_COURSE_ID);

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteCourseShouldReturnOK() {
        Course course = new Course().setId(EXISTENT_COURSE_ID_51);
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.deleteCourse(EXISTENT_COURSE_ID_51);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseService, times(1)).delete(course);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteCourseShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<CourseDTO> responseEntity = courseRestController.deleteCourse(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteCourseShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);
        ResponseEntity<CourseDTO> responseEntity = courseRestController.deleteCourse(NONEXISTENT_COURSE_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllCoursesShouldReturnOK() {
        Course course1 = new Course().setId(EXISTENT_COURSE_ID_51);

        CourseDTO courseDTO1 = courseMapper.toDto(course1);

        List<Course> courses = new ArrayList<>(singletonList(course1));

        when(mockCourseService.getAll()).thenReturn(courses);
        when(mockCourseMapper.toDto(course1)).thenReturn(courseDTO1);

        ResponseEntity<List<CourseDTO>> responseEntity = courseRestController.getAllCourses();

        verify(mockCourseService, times(1)).getAll();
        verify(mockCourseMapper, times(1)).toDto(course1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<CourseDTO> expectedCourseDTOList = new ArrayList<>(singletonList(courseDTO1));
        assertThat(expectedCourseDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }

    @Test
    void testAssignGroupShouldReturnOK() {
        Group group1 = new Group().setId(EXISTENT_GROUP_ID_501);
        List<Group> groupList = singletonList(group1);

        Group group2 = new Group().setId(EXISTENT_GROUP_ID_502);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_502)).thenReturn(group2);

        Course course = new Course().setId(EXISTENT_COURSE_ID_51).setGroups(groupList);
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);

        CourseGroupDTO courseGroupDTO1 = courseGroupMapper.toDto(group1);
        when(mockCourseGroupMapper.toDto(group1)).thenReturn(courseGroupDTO1);

        List<CourseGroupDTO> expectedCourseDTOList = singletonList(courseGroupDTO1);

        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO().setCourseId(EXISTENT_COURSE_ID_51).setGroupId(EXISTENT_GROUP_ID_502);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.assignGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(1)).assignGroupToCourse(group2, course);
        verify(mockCourseService, times(2)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_502);
        verify(mockCourseGroupMapper, times(1)).toDto(group1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(expectedCourseDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }

    @Test
    void testAssignGroupShouldReturnBadRequestWhenNullIdPassed() {
        CourseGroupAssignmentDTO emptyCourseGroupAssignmentDTO = new CourseGroupAssignmentDTO();
        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.assignGroup(emptyCourseGroupAssignmentDTO);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAssignGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(EXISTENT_COURSE_ID_51)
                .setGroupId(EXISTENT_GROUP_ID_501);

        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.assignGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testRemoveGroupShouldReturnOK() {
        Group group1 = new Group().setId(EXISTENT_GROUP_ID_501);
        List<Group> groupList = singletonList(group1);

        Course course = new Course().setId(EXISTENT_COURSE_ID_51).setGroups(groupList);
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course);

        CourseGroupDTO courseGroupDTO1 = courseGroupMapper.toDto(group1);
        when(mockCourseGroupMapper.toDto(group1)).thenReturn(courseGroupDTO1);

        List<CourseGroupDTO> expectedCourseDTOList = singletonList(courseGroupDTO1);

        Group group2 = new Group().setId(EXISTENT_GROUP_ID_502);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_502)).thenReturn(group2);

        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(EXISTENT_COURSE_ID_51)
                .setGroupId(EXISTENT_GROUP_ID_502);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.removeGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(1)).removeGroupFromCourse(group2, course);
        verify(mockCourseService, times(2)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_502);
        verify(mockCourseGroupMapper, times(1)).toDto(group1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(expectedCourseDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }

    @Test
    void testRemoveGroupShouldReturnBadRequestWhenNullIdPassed() {
        CourseGroupAssignmentDTO emptyCourseGroupAssignmentDTO = new CourseGroupAssignmentDTO();
        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.removeGroup(emptyCourseGroupAssignmentDTO);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testRemoveGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
                .setCourseId(NONEXISTENT_COURSE_ID)
                .setGroupId(NONEXISTENT_GROUP_ID);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.removeGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }
}
