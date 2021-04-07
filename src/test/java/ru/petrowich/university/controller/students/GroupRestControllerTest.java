package ru.petrowich.university.controller.students;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import ru.petrowich.university.dto.students.GroupDTO;
import ru.petrowich.university.mapper.students.GroupMapper;
import ru.petrowich.university.model.*;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.GroupService;

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

class GroupRestControllerTest {
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final String EXISTENT_COURSE_NAME_51 = "math";
    private static final String EXISTENT_COURSE_NAME_52 = "biology";
    private static final String EXISTENT_COURSE_NAME_53 = "physics";
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final Integer EXISTENT_PERSON_ID_50002 = 50002;
    private static final Integer EXISTENT_PERSON_ID_50003 = 50003;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_PERSON_ID_50006 = 50006;
    private static final String EXISTENT_PERSON_FIRST_NAME_50001 = "Рулон";
    private static final String EXISTENT_PERSON_FIRST_NAME_50002 = "Обвал";
    private static final String EXISTENT_PERSON_FIRST_NAME_50003 = "Рекорд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Отряд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50006 = "Ушат";
    private static final String EXISTENT_PERSON_LAST_NAME_50001 = "Обоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50002 = "Забоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50003 = "Надоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Ковбоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50006 = "Помоев";
    private static final Integer NEW_GROUP_ID = 504;
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer EXISTENT_GROUP_ID_503 = 503;
    private static final String NEW_GROUP_NAME = "new group";
    private static final String EXISTENT_GROUP_NAME_501 = "AA-01";
    private static final String EXISTENT_GROUP_NAME_502 = "BB-02";
    private static final String EXISTENT_GROUP_NAME_503 = "CC-03";
    private static final String ANOTHER_GROUP_NAME = "another group name";
    private static final Integer NONEXISTENT_GROUP_ID = 999;

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final GroupMapper groupMapper = new GroupMapper(modelMapper);

    private final Student student1 = new Student().setId(EXISTENT_PERSON_ID_50001)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50001).setLastName(EXISTENT_PERSON_LAST_NAME_50001).setActive(true);

    private final Student student2 = new Student().setId(EXISTENT_PERSON_ID_50002)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50002).setLastName(EXISTENT_PERSON_LAST_NAME_50002).setActive(true);

    private final Student student3 = new Student().setId(EXISTENT_PERSON_ID_50003)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50003).setLastName(EXISTENT_PERSON_LAST_NAME_50003).setActive(true);

    private final Lecturer lecturer1 = new Lecturer().setId(EXISTENT_PERSON_ID_50005)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005).setLastName(EXISTENT_PERSON_LAST_NAME_50005).setActive(true);

    private final Lecturer lecturer2 = new Lecturer().setId(EXISTENT_PERSON_ID_50006)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50006).setLastName(EXISTENT_PERSON_LAST_NAME_50006).setActive(true);

    private final Course course1 = new Course().setId(EXISTENT_COURSE_ID_51)
            .setName(EXISTENT_COURSE_NAME_51).setAuthor(lecturer1).setActive(true);

    private final Course course2 = new Course().setId(EXISTENT_COURSE_ID_52)
            .setName(EXISTENT_COURSE_NAME_52).setAuthor(lecturer1).setActive(true);

    private final Course course3 = new Course().setId(EXISTENT_COURSE_ID_53)
            .setName(EXISTENT_COURSE_NAME_53).setAuthor(lecturer2).setActive(false);

    private final List<Student> studentList1 = new ArrayList<>(asList(student1, student2));
    private final List<Student> studentList2 = new ArrayList<>(singletonList(student3));

    private final List<Course> courseList1 = new ArrayList<>(asList(course1, course2));
    private final List<Course> courseList2 = new ArrayList<>(asList(course2, course3));
    private final List<Course> courseList3 = new ArrayList<>(singletonList(course3));

    private final Group newGroup = new Group().setName(NEW_GROUP_NAME).setActive(true);
    private final Group group1 = new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setStudents(studentList1).setCourses(courseList1).setActive(true);
    private final Group group2 = new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setStudents(studentList2).setCourses(courseList2).setActive(true);
    private final Group group3 = new Group().setId(EXISTENT_GROUP_ID_503).setName(EXISTENT_GROUP_NAME_503).setCourses(courseList3).setActive(true);

    @Mock
    GroupService mockGroupService;

    @Mock
    GroupMapper mockGroupMapper;

    @InjectMocks
    GroupRestController groupRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetGroupShouldReturnOK() {
        GroupDTO groupDTO1 = groupMapper.toDto(group1);

        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group1);
        when(mockGroupMapper.toDto(group1)).thenReturn(groupDTO1);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.getGroup(EXISTENT_GROUP_ID_501);

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupMapper, times(1)).toDto(group1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(groupDTO1);
    }

    @Test
    void testGetGroupShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<GroupDTO> responseEntity = groupRestController.getGroup(null);

        verify(mockGroupService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.getGroup(NONEXISTENT_GROUP_ID);

        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddGroupShouldReturnCreated() {
        GroupDTO newGroupDTO = groupMapper.toDto(newGroup);
        Group expectedGroup = groupMapper.toEntity(newGroupDTO).setId(NEW_GROUP_ID);

        when(mockGroupMapper.toEntity(newGroupDTO)).thenReturn(newGroup);
        when(mockGroupService.add(newGroup)).thenReturn(expectedGroup);

        GroupDTO expectedGroupDTO = groupMapper.toDto(expectedGroup);
        when(mockGroupMapper.toDto(expectedGroup)).thenReturn(expectedGroupDTO);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.addGroup(newGroupDTO);

        verify(mockGroupMapper, times(1)).toEntity(newGroupDTO);
        verify(mockGroupService, times(1)).add(newGroup);
        verify(mockGroupMapper, times(1)).toDto(expectedGroup);
        assertThat(responseEntity.getBody()).isEqualTo(expectedGroupDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateGroupShouldReturnOK() {
        GroupDTO groupDTO = groupMapper.toDto(group1);
        Group expectedGroup = groupMapper.toEntity(groupDTO).setName(ANOTHER_GROUP_NAME);

        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group1);
        when(mockGroupMapper.toEntity(groupDTO)).thenReturn(group1);
        when(mockGroupService.update(group1)).thenReturn(expectedGroup);

        GroupDTO expectedGroupDTO = groupMapper.toDto(expectedGroup);
        when(mockGroupMapper.toDto(expectedGroup)).thenReturn(expectedGroupDTO);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.updateGroup(groupDTO, EXISTENT_GROUP_ID_501);

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupMapper, times(1)).toEntity(groupDTO);
        verify(mockGroupService, times(1)).update(group1);
        verify(mockGroupMapper, times(1)).toDto(expectedGroup);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedGroupDTO);
    }

    @Test
    void testUpdateGroupShouldReturnBadRequestWhenNullIdPassed() {
        GroupDTO groupDTO = groupMapper.toDto(group1);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.updateGroup(groupDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        GroupDTO groupDTO = groupMapper.toDto(group1);

        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.updateGroup(groupDTO, NONEXISTENT_GROUP_ID);

        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteGroupShouldReturnOK() {
        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group1);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.deleteGroup(EXISTENT_GROUP_ID_501);

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupService, times(1)).delete(group1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteGroupShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<GroupDTO> responseEntity = groupRestController.deleteGroup(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);
        ResponseEntity<GroupDTO> responseEntity = groupRestController.deleteGroup(NONEXISTENT_GROUP_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllGroupsShouldReturnOK() {
        GroupDTO groupDTO1 = groupMapper.toDto(group1);
        GroupDTO groupDTO2 = groupMapper.toDto(group2);
        GroupDTO groupDTO3 = groupMapper.toDto(group3);

        List<Group> groups = new ArrayList<>(asList(group1, group2, group3));

        when(mockGroupService.getAll()).thenReturn(groups);
        when(mockGroupMapper.toDto(group1)).thenReturn(groupDTO1);
        when(mockGroupMapper.toDto(group2)).thenReturn(groupDTO2);
        when(mockGroupMapper.toDto(group3)).thenReturn(groupDTO3);

        ResponseEntity<List<GroupDTO>> responseEntity = groupRestController.getAllGroups();

        verify(mockGroupService, times(1)).getAll();
        verify(mockGroupMapper, times(1)).toDto(group1);
        verify(mockGroupMapper, times(1)).toDto(group2);
        verify(mockGroupMapper, times(1)).toDto(group3);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<GroupDTO> expectedGroupDTOList = new ArrayList<>(asList(groupDTO1, groupDTO2, groupDTO3));
        assertThat(expectedGroupDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
