package ru.petrowich.university.integration.students;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.GroupService;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class GroupRestControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer GROUP_ID_503 = 503;
    private static final Integer NEW_GROUP_ID = 101;
    private static final Integer NONEXISTENT_GROUP_ID = 999;
    private static final String GROUP_NAME_501 = "AA-01";
    private static final String CHANGED_GROUP_NAME = "new or changed name";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupService groupService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetGroupShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/students/groups/{id}", GROUP_ID_501)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(GROUP_ID_501));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(get("/api/students/groups/{id}", NONEXISTENT_GROUP_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAddGroupShouldReturnCreated() throws Exception {
        String newGroupJSON = "{\"name\":\"" + CHANGED_GROUP_NAME + "\"}";

        mockMvc.perform(post("/api/students/groups/add")
                .content(newGroupJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_GROUP_ID));

        Group actualGroup = groupService.getById(NEW_GROUP_ID);

        assertThat(actualGroup.getName()).isEqualTo(CHANGED_GROUP_NAME);
        assertTrue(actualGroup.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateGroupShouldReturnOK() throws Exception {
        String groupJSON = "{\"id\":" + GROUP_ID_501
                + ",\"name\":\"" + CHANGED_GROUP_NAME + "\""
                + "}";

        Group currentGroup = groupService.getById(GROUP_ID_501);

        assertThat(currentGroup.getName()).isEqualTo(GROUP_NAME_501);
        assertTrue(currentGroup.isActive());

        mockMvc.perform(put("/api/students/groups/update/{id}", GROUP_ID_501)
                .content(groupJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(GROUP_ID_501))
                .andExpect(jsonPath("$.name").value(CHANGED_GROUP_NAME));

        Group actualGroup = groupService.getById(GROUP_ID_501);

        assertThat(actualGroup.getName()).isEqualTo(CHANGED_GROUP_NAME);
        assertTrue(actualGroup.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String groupJSON = "{\"name\":\"" + CHANGED_GROUP_NAME + "\"}";

        mockMvc.perform(put("/api/students/groups/update/{id}", NONEXISTENT_GROUP_ID)
                .content(groupJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteGroupShouldReturnOK() throws Exception {
        Group currentGroup = groupService.getById(GROUP_ID_501);
        assertTrue(currentGroup.isActive());

        mockMvc.perform(delete("/api/students/groups/delete/{id}", GROUP_ID_501))
                .andDo(print())
                .andExpect(status().isOk());

        Group actualGroup = groupService.getById(GROUP_ID_501);
        assertFalse(actualGroup.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(delete("/api/students/groups/delete/{id}", NONEXISTENT_GROUP_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetAllGroupsShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/students/groups/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value(GROUP_ID_501));
    }
}
