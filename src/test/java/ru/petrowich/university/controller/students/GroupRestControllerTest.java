package ru.petrowich.university.controller.students;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.dto.students.GroupDTO;
import ru.petrowich.university.mapper.students.GroupMapper;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.GroupService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class GroupRestControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Integer NEW_GROUP_ID = 504;
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final String ANOTHER_GROUP_NAME = "another group name";
    private static final Integer NONEXISTENT_GROUP_ID = 999;

    private final ObjectMapper objectMapper = new ObjectMapper();
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
        mockMvc = standaloneSetup(groupRestController).build();
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetGroupShouldReturnOK() throws Exception {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO1 = groupMapper.toDto(group);

        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);
        when(mockGroupMapper.toDto(group)).thenReturn(groupDTO1);

        mockMvc.perform(get("/api/students/groups/{id}", EXISTENT_GROUP_ID_501)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_GROUP_ID_501));

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupMapper, times(1)).toDto(group);
    }

    @Test
    void testGetGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        mockMvc.perform(get("/api/students/groups/{id}", NONEXISTENT_GROUP_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
    }

    @Test
    void testAddGroupShouldReturnCreated() throws Exception {
        Group newGroup = new Group();
        GroupDTO newGroupDTO = groupMapper.toDto(newGroup);
        String newGroupJSON = objectMapper.writeValueAsString(newGroupDTO);

        Group expectedGroup = groupMapper.toEntity(newGroupDTO).setId(NEW_GROUP_ID);

        when(mockGroupMapper.toEntity(newGroupDTO)).thenReturn(newGroup);
        when(mockGroupService.add(newGroup)).thenReturn(expectedGroup);

        GroupDTO expectedGroupDTO = groupMapper.toDto(expectedGroup);
        when(mockGroupMapper.toDto(expectedGroup)).thenReturn(expectedGroupDTO);

        mockMvc.perform(post("/api/students/groups/add")
                .content(newGroupJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_GROUP_ID));

        verify(mockGroupMapper, times(1)).toEntity(newGroupDTO);
        verify(mockGroupService, times(1)).add(newGroup);
        verify(mockGroupMapper, times(1)).toDto(expectedGroup);
    }

    @Test
    void testUpdateGroupShouldReturnOK() throws Exception {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501).setName(ANOTHER_GROUP_NAME);
        GroupDTO groupDTO = groupMapper.toDto(group);

        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(new Group().setId(EXISTENT_GROUP_ID_501));
        when(mockGroupMapper.toEntity(groupDTO)).thenReturn(group);
        when(mockGroupService.update(group)).thenReturn(group);
        when(mockGroupMapper.toDto(group)).thenReturn(groupDTO);

        String groupJSON = objectMapper.writeValueAsString(groupDTO);

        mockMvc.perform(put("/api/students/groups/update/{id}", EXISTENT_GROUP_ID_501)
                .content(groupJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(EXISTENT_GROUP_ID_501))
                .andExpect(jsonPath("$.name").value(ANOTHER_GROUP_NAME));

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupMapper, times(1)).toEntity(groupDTO);
        verify(mockGroupService, times(1)).update(group);
        verify(mockGroupMapper, times(1)).toDto(group);
    }

    @Test
    void testUpdateGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        Group group = new Group().setId(NONEXISTENT_GROUP_ID);
        GroupDTO groupDTO = groupMapper.toDto(group);
        String groupJSON = objectMapper.writeValueAsString(groupDTO);

        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        mockMvc.perform(put("/api/students/groups/update/{id}", NONEXISTENT_GROUP_ID)
                .content(groupJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mockGroupService, times(1)).getById(NONEXISTENT_GROUP_ID);
    }

    @Test
    void testDeleteGroupShouldReturnOK() throws Exception {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        when(mockGroupService.getById(EXISTENT_GROUP_ID_501)).thenReturn(group);

        mockMvc.perform(delete("/api/students/groups/delete/{id}", EXISTENT_GROUP_ID_501))
                .andExpect(status().isOk());

        verify(mockGroupService, times(1)).getById(EXISTENT_GROUP_ID_501);
        verify(mockGroupService, times(1)).delete(group);
    }

    @Test
    void testDeleteGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        when(mockGroupService.getById(NONEXISTENT_GROUP_ID)).thenReturn(null);

        mockMvc.perform(delete("/api/students/groups/delete/{id}", NONEXISTENT_GROUP_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllGroupsShouldReturnOK() throws Exception {
        Group group = new Group().setId(EXISTENT_GROUP_ID_501);
        GroupDTO groupDTO = groupMapper.toDto(group);

        List<Group> groups = new ArrayList<>(singletonList(group));

        when(mockGroupService.getAll()).thenReturn(groups);
        when(mockGroupMapper.toDto(group)).thenReturn(groupDTO);

        mockMvc.perform(get("/api/students/groups/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(EXISTENT_GROUP_ID_501));

        verify(mockGroupService, times(1)).getAll();
        verify(mockGroupMapper, times(1)).toDto(group);
    }
}
