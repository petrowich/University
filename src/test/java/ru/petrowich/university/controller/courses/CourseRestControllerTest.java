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
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class CourseRestControllerTest {
    private static final Integer NEW_COURSE_ID = 54;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final String NEW_COURSE_NAME = "Some Course Name";
    private static final String EXISTENT_COURSE_NAME_51 = "math";
    private static final String EXISTENT_COURSE_NAME_52 = "biology";
    private static final String EXISTENT_COURSE_NAME_53 = "physics";
    private static final String NEW_COURSE_DESCRIPTION = "Some Description";
    private static final String EXISTENT_COURSE_DESCRIPTION_51 = "exact";
    private static final String EXISTENT_COURSE_DESCRIPTION_52 = "natural";
    private static final String EXISTENT_COURSE_DESCRIPTION_53 = "exact";
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_PERSON_ID_50006 = 50006;
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Отряд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50006 = "Ушат";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Ковбоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50006 = "Помоев";
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer EXISTENT_GROUP_ID_503 = 503;
    private static final String EXISTENT_GROUP_NAME_501 = "AA-01";
    private static final String EXISTENT_GROUP_NAME_502 = "BB-02";
    private static final String EXISTENT_GROUP_NAME_503 = "CC-03";

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final CourseMapper courseMapper = new CourseMapper(modelMapper);
    private final CourseGroupMapper courseGroupMapper = new CourseGroupMapper(modelMapper);

    private final Lecturer lecturer1 = new Lecturer().setId(EXISTENT_PERSON_ID_50005)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005).setLastName(EXISTENT_PERSON_LAST_NAME_50005).setActive(true);

    private final Lecturer lecturer2 = new Lecturer().setId(EXISTENT_PERSON_ID_50006)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50006).setLastName(EXISTENT_PERSON_LAST_NAME_50006).setActive(true);

    private final Group group1 = new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setActive(true);
    private final Group group2 = new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setActive(true);
    private final Group group3 = new Group().setId(EXISTENT_GROUP_ID_503).setName(EXISTENT_GROUP_NAME_503).setActive(true);

    List<Group> groupList1 = new ArrayList<>(asList(group1, group2));
    List<Group> groupList2 = new ArrayList<>(singletonList(group3));

    private final Course newCourse = new Course()
            .setName(NEW_COURSE_NAME).setDescription(NEW_COURSE_DESCRIPTION)
            .setAuthor(lecturer1).setActive(true);

    private final Course course1 = new Course().setId(EXISTENT_COURSE_ID_51)
            .setName(EXISTENT_COURSE_NAME_51).setDescription(EXISTENT_COURSE_DESCRIPTION_51)
            .setAuthor(lecturer1).setGroups(groupList1).setActive(true);

    private final Course course2 = new Course().setId(EXISTENT_COURSE_ID_52)
            .setName(EXISTENT_COURSE_NAME_52).setDescription(EXISTENT_COURSE_DESCRIPTION_52)
            .setAuthor(lecturer1).setGroups(groupList2).setActive(true);

    private final Course course3 = new Course().setId(EXISTENT_COURSE_ID_53)
            .setName(EXISTENT_COURSE_NAME_53).setDescription(EXISTENT_COURSE_DESCRIPTION_53)
            .setAuthor(lecturer2).setActive(false);

    private final CourseGroupAssignmentDTO courseGroupAssignmentDTO = new CourseGroupAssignmentDTO()
            .setCourseId(EXISTENT_COURSE_ID_51).setGroupId(EXISTENT_GROUP_ID_503);

    private final CourseGroupAssignmentDTO emptyCourseGroupAssignmentDTO = new CourseGroupAssignmentDTO().setGroupId(null).setCourseId(null);

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
        CourseDTO courseDTO1 = courseMapper.toDto(course1);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course1);
        when(mockCourseMapper.toDto(course1)).thenReturn(courseDTO1);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.getCourse(EXISTENT_COURSE_ID_51);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseMapper, times(1)).toDto(course1);
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
        CourseDTO courseDTO = courseMapper.toDto(course1);
        Course expectedCourse = courseMapper.toEntity(courseDTO).setAuthor(lecturer2);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course1);
        when(mockCourseMapper.toEntity(courseDTO)).thenReturn(course1);
        when(mockCourseService.update(course1)).thenReturn(expectedCourse);

        CourseDTO expectedCourseDTO = courseMapper.toDto(expectedCourse);
        when(mockCourseMapper.toDto(expectedCourse)).thenReturn(expectedCourseDTO);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.updateCourse(courseDTO, EXISTENT_COURSE_ID_51);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseMapper, times(1)).toEntity(courseDTO);
        verify(mockCourseService, times(1)).update(course1);
        verify(mockCourseMapper, times(1)).toDto(expectedCourse);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedCourseDTO);
    }

    @Test
    void testUpdateCourseShouldReturnBadRequestWhenNullIdPassed() {
        CourseDTO courseDTO = courseMapper.toDto(course1);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.updateCourse(courseDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateCourseShouldReturnNotFoundWhenNonexistentIdPassed() {
        CourseDTO courseDTO = courseMapper.toDto(course1);

        when(mockCourseService.getById(NONEXISTENT_COURSE_ID)).thenReturn(null);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.updateCourse(courseDTO, NONEXISTENT_COURSE_ID);

        verify(mockCourseService, times(1)).getById(NONEXISTENT_COURSE_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteCourseShouldReturnOK() {
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course1);

        ResponseEntity<CourseDTO> responseEntity = courseRestController.deleteCourse(EXISTENT_COURSE_ID_51);

        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockCourseService, times(1)).delete(course1);
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
        CourseDTO courseDTO1 = courseMapper.toDto(course1);
        CourseDTO courseDTO2 = courseMapper.toDto(course2);
        CourseDTO courseDTO3 = courseMapper.toDto(course3);

        List<Course> courses = new ArrayList<>(asList(course1, course2, course3));

        when(mockCourseService.getAll()).thenReturn(courses);
        when(mockCourseMapper.toDto(course1)).thenReturn(courseDTO1);
        when(mockCourseMapper.toDto(course2)).thenReturn(courseDTO2);
        when(mockCourseMapper.toDto(course3)).thenReturn(courseDTO3);

        ResponseEntity<List<CourseDTO>> responseEntity = courseRestController.getAllCourses();

        verify(mockCourseService, times(1)).getAll();
        verify(mockCourseMapper, times(1)).toDto(course1);
        verify(mockCourseMapper, times(1)).toDto(course2);
        verify(mockCourseMapper, times(1)).toDto(course3);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<CourseDTO> expectedCourseDTOList = new ArrayList<>(asList(courseDTO1, courseDTO2, courseDTO3));
        assertThat(expectedCourseDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }

    @Test
    void testAssignGroupShouldReturnOK() {
        CourseGroupDTO courseGroupDTO1 = courseGroupMapper.toDto(group1);
        CourseGroupDTO courseGroupDTO2 = courseGroupMapper.toDto(group2);
        List<CourseGroupDTO> expectedCourseDTOList = asList(courseGroupDTO1, courseGroupDTO2);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course1);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_503)).thenReturn(group3);
        when(mockCourseGroupMapper.toDto(group1)).thenReturn(courseGroupDTO1);
        when(mockCourseGroupMapper.toDto(group2)).thenReturn(courseGroupDTO2);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.assignGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(1)).assignGroupToCourse(group3, course1);
        verify(mockCourseService, times(2)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_503);
        verify(mockCourseGroupMapper, times(1)).toDto(group1);
        verify(mockCourseGroupMapper, times(1)).toDto(group2);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(expectedCourseDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }

    @Test
    void testAssignGroupShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.assignGroup(emptyCourseGroupAssignmentDTO);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAssignGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(null);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_503)).thenReturn(null);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.assignGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(0)).assignGroupToCourse(group3, course1);
        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_503);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testRemoveGroupShouldReturnOK() {
        CourseGroupDTO courseGroupDTO1 = courseGroupMapper.toDto(group1);
        CourseGroupDTO courseGroupDTO2 = courseGroupMapper.toDto(group2);
        List<CourseGroupDTO> expectedCourseDTOList = asList(courseGroupDTO1, courseGroupDTO2);

        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(course1);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_503)).thenReturn(group3);
        when(mockCourseGroupMapper.toDto(group1)).thenReturn(courseGroupDTO1);
        when(mockCourseGroupMapper.toDto(group2)).thenReturn(courseGroupDTO2);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.removeGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(1)).removeGroupFromCourse(group3, course1);
        verify(mockCourseService, times(2)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_503);
        verify(mockCourseGroupMapper, times(1)).toDto(group1);
        verify(mockCourseGroupMapper, times(1)).toDto(group2);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(expectedCourseDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }

    @Test
    void testRemoveGroupShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.removeGroup(emptyCourseGroupAssignmentDTO);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testRemoveGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockCourseService.getById(EXISTENT_COURSE_ID_51)).thenReturn(null);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_503)).thenReturn(null);

        ResponseEntity<List<CourseGroupDTO>> responseEntity = courseRestController.removeGroup(courseGroupAssignmentDTO);

        verify(mockCourseService, times(0)).removeGroupFromCourse(group3, course1);
        verify(mockCourseService, times(1)).getById(EXISTENT_COURSE_ID_51);
        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_503);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }
}
