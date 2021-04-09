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
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.GroupService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GroupRestControllerTest {
    private static final Integer NEW_GROUP_ID = 504;
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final String ANOTHER_GROUP_NAME = "another group name";
    private static final Integer NONEXISTENT_GROUP_ID = 999;

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final GroupMapper groupMapper = new GroupMapper(modelMapper);

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
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO1 = groupMapper.toDto(group);

        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);
        when(mockGroupMapper.toDto(group)).thenReturn(groupDTO1);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.getGroup(EXISTENT_GROUP_ID_501);

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupMapper, times(1)).toDto(group);
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
        Group newGroup = new Group();
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
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO = groupMapper.toDto(group);
        Group expectedGroup = groupMapper.toEntity(groupDTO).setName(ANOTHER_GROUP_NAME);

        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);
        when(mockGroupMapper.toEntity(groupDTO)).thenReturn(group);
        when(mockGroupService.update(group)).thenReturn(expectedGroup);

        GroupDTO expectedGroupDTO = groupMapper.toDto(expectedGroup);
        when(mockGroupMapper.toDto(expectedGroup)).thenReturn(expectedGroupDTO);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.updateGroup(groupDTO, EXISTENT_GROUP_ID_501);

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupMapper, times(1)).toEntity(groupDTO);
        verify(mockGroupService, times(1)).update(group);
        verify(mockGroupMapper, times(1)).toDto(expectedGroup);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedGroupDTO);
    }

    @Test
    void testUpdateGroupShouldReturnBadRequestWhenNullIdPassed() {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO = groupMapper.toDto(group);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.updateGroup(groupDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateGroupShouldReturnNotFoundWhenNonexistentIdPassed() {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO = groupMapper.toDto(group);

        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.updateGroup(groupDTO, NONEXISTENT_GROUP_ID);

        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteGroupShouldReturnOK() {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);

        ResponseEntity<GroupDTO> responseEntity = groupRestController.deleteGroup(EXISTENT_GROUP_ID_501);

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupService, times(1)).delete(group);
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
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO1 = groupMapper.toDto(group);

        List<Group> groups = new ArrayList<>(singletonList(group));

        when(mockGroupService.getAll()).thenReturn(groups);
        when(mockGroupMapper.toDto(group)).thenReturn(groupDTO1);

        ResponseEntity<List<GroupDTO>> responseEntity = groupRestController.getAllGroups();

        verify(mockGroupService, times(1)).getAll();
        verify(mockGroupMapper, times(1)).toDto(group);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<GroupDTO> expectedGroupDTOList = new ArrayList<>(singletonList(groupDTO1));
        assertThat(expectedGroupDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
