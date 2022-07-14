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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class GroupControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer GROUP_ID_501 = 501;
    private static final Integer NEw_GROUP_ID = 101;
    private static final String GROUP_NAME_501 = "AA-01";
    private static final Integer GROUP_CAPACITY_501 = 20;
    private static final String CHANGED_GROUP_NAME = "new or changed name";
    private static final Integer CHANGED_GROUP_CAPACITY = 30;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupService groupService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGroups() throws Exception {
        mockMvc.perform(get("/students/groups")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"text-center\">Groups</h1>"),
                        containsString("<td><a href=\"/students/group?id=501\">AA-01</a></td>"),
                        containsString("<td><a href=\"/students/group/edit?id=501\" class=\"btn btn-light fas fa-edit\"></a></td>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGroup() throws Exception {
        mockMvc.perform(get("/students/group?id=501")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"display-4\">AA-01</h1>"),
                        containsString("<a href=\"/courses/course?id=52\">biology</a>"),
                        containsString("<a href=\"/courses/course?id=54\">literature</a>"),
                        containsString("<a href=\"/courses/course?id=51\">math</a>"),
                        containsString("<a href=\"/students/student?id=50001\">Giorgio Parisi</a>"),
                        containsString("<a href=\"/students/student?id=50002\">Klaus Hasselmann</a>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testEdit() throws Exception {
        mockMvc.perform(get("/students/group/edit?id=501")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"header\">Edit group</h1>"),
                        containsString("<input type=\"text\" class=\"form-control\" id=\"groupName\" placeholder=\"Group Name\" required name=\"name\" value=\"AA-01\">"),
                        containsString("<input type=\"number\" min=\"5\" max=\"30\" class=\"form-control\" id=\"groupCapacity\" placeholder=\"20\" required name=\"capacity\" value=\"20\">"),
                        containsString("<label class=\"form-check-label\" for=\"active\">active group</label>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdate() throws Exception {
        Group currentGroup = groupService.getById(GROUP_ID_501);

        assertThat(currentGroup.getName()).isEqualTo(GROUP_NAME_501);
        assertThat(currentGroup.getCapacity()).isEqualTo(GROUP_CAPACITY_501);
        assertTrue(currentGroup.isActive());

        mockMvc.perform(post("/students/group/update?id=501")
                .param("name", CHANGED_GROUP_NAME)
                .param("capacity", CHANGED_GROUP_CAPACITY.toString())
                .param("actual", "false")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/groups/"));

        mockMvc.perform(get("/students/groups/")
                        .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<td><a href=\"/students/group?id=501\" class=\"text-danger\">new or changed name</a></td>"),
                        containsString("<td>30</td>")))
                );

        Group actualGroup = groupService.getById(GROUP_ID_501);

        assertThat(actualGroup.getName()).isEqualTo(CHANGED_GROUP_NAME);
        assertThat(actualGroup.getCapacity()).isEqualTo(CHANGED_GROUP_CAPACITY);
        assertFalse(actualGroup.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testCreate() throws Exception {
        mockMvc.perform(get("/students/group/new")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(containsString("<h1 class=\"header\">New group</h1>"))));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAdd() throws Exception {
        mockMvc.perform(post("/students/group/add")
                .param("name", CHANGED_GROUP_NAME)
                .param("capacity", CHANGED_GROUP_CAPACITY.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students/groups/"));

        mockMvc.perform(get("/students/groups/")
                        .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<td><a href=\"/students/group?id=101\">new or changed name</a></td>"),
                        containsString("<td>30</td>")))
                );

        Group actualGroup = groupService.getById(NEw_GROUP_ID);

        assertThat(actualGroup.getName()).isEqualTo(CHANGED_GROUP_NAME);
        assertThat(actualGroup.getCapacity()).isEqualTo(CHANGED_GROUP_CAPACITY);
        assertTrue(actualGroup.isActive());
    }
}
