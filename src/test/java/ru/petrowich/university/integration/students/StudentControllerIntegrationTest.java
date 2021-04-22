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
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.StudentService;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class StudentControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer NEW_COURSE_ID = 10001;
    private static final String PERSON_FIRST_NAME_50001 = "Рулон";
    private static final String PERSON_LAST_NAME_50001 = "Обоев";
    private static final String PERSON_EMAIL_50001 = "rulon.oboev@university.edu";
    private static final String PERSON_COMMENT_50001 = "stupid";
    private static final String CHANGED_PERSON_FIRST_NAME = "new or changed first name";
    private static final String CHANGED_PERSON_LAST_NAME = "new or changed last name";
    private static final String CHANGED_PERSON_EMAIL = "new.changed@university.edu";
    private static final String CHANGED_PERSON_COMMENT = "some description";
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testStudents() throws Exception {
        mockMvc.perform(get("/students")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"text-center\">Students</h1>"),
                        containsString("<td><a href=\"/students/student?id=50001\">Рулон Обоев</a></td>"),
                        containsString("<td><a href=\"mailto:rulon.oboev@university.edu\">rulon.oboev@university.edu</a></td>"),
                        containsString("<td><a href=\"/students/group?id=501\">AA-01</a></td>"),
                        containsString("<td><a href=\"/students/student/edit?id=50001\" class=\"btn btn-light fas fa-user-edit\"></a></td>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testStudent() throws Exception {
        mockMvc.perform(get("/students/student?id=50001")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<p>Group:<a class=\"ml-1\" href=\"/students/group?id=501\">AA-01</a></p>"),
                        containsString("<h1 class=\"display-4\">Рулон Обоев</h1>"),
                        containsString("<p class=\"lead\">stupid</p>"),
                        containsString("<p><a href=\"/students/student/edit?id=50001\" class=\"btn btn-primary\">Edit</a></p>"),
                        containsString("<p class=\"lead\">stupid</p>"),
                        containsString("<td><a href=\"/courses/course?id=51\">math</a></td>"),
                        containsString("<td><a href=\"/courses/course?id=52\">biology</a></td>"),
                        containsString("<td><a href=\"/courses/course?id=54\">literature</a></td>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testEditStudent() throws Exception {
        mockMvc.perform(get("/students/student/edit?id=50001")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"header\">Edit student</h1>"),
                        containsString("<input type=\"text\" class=\"form-control\" id=\"firstName\" placeholder=\"First Name\" required name=\"firstName\" value=\"Рулон\">"),
                        containsString("<input type=\"text\" class=\"form-control\" id=\"lastName\" placeholder=\"Last Name\" required name=\"lastName\" value=\"Обоев\">"),
                        containsString("<input type=\"email\" class=\"form-control\" id=\"email\" placeholder=\"Enter email\" required name=\"email\" value=\"rulon.oboev@university.edu\">"),
                        containsString("<option value=\"501\" selected=\"selected\">AA-01</option>"),
                        containsString("<option value=\"502\">BB-02</option>"),
                        containsString("<option value=\"\">no group</option>"),
                        containsString("<textarea type=\"text\" class=\"form-control\" id=\"comment\" placeholder=\"Comment\" rows=\"3\" name=\"comment\">stupid</textarea>"),
                        stringContainsInOrder("<input type=\"checkbox\"", "value=\"true\" checked=\"checked\"><input type=\"hidden\" name=\"_active\" value=\"on\"/>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdate() throws Exception {
        Student currentLecturer = studentService.getById(PERSON_ID_50001);
        Group currentGroup = groupService.getById(GROUP_ID_501);

        assertThat(currentLecturer.getFirstName()).isEqualTo(PERSON_FIRST_NAME_50001);
        assertThat(currentLecturer.getLastName()).isEqualTo(PERSON_LAST_NAME_50001);
        assertThat(currentLecturer.getEmail()).isEqualTo(PERSON_EMAIL_50001);
        assertThat(currentLecturer.getComment()).isEqualTo(PERSON_COMMENT_50001);
        assertThat(currentLecturer.getGroup()).isEqualTo(currentGroup);
        assertTrue(currentLecturer.isActive());

        mockMvc.perform(post("/students/student/update?id=50001")
                .param("firstName", CHANGED_PERSON_FIRST_NAME)
                .param("lastName", CHANGED_PERSON_LAST_NAME)
                .param("email", CHANGED_PERSON_EMAIL)
                .param("comment", CHANGED_PERSON_COMMENT)
                .param("group.id", GROUP_ID_502.toString())
                .param("actual", "false")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("<td><a href=\"/students/student?id=50001\" class=\"text-danger\">new or changed first name new or changed last name</a></td>"),
                        containsString("<td><a href=\"/students/group?id=502\">BB-02</a></td>")))
                );

        Student actualStudent = studentService.getById(PERSON_ID_50001);
        Group group = groupService.getById(GROUP_ID_502);

        assertThat(actualStudent.getFirstName()).isEqualTo(CHANGED_PERSON_FIRST_NAME);
        assertThat(actualStudent.getLastName()).isEqualTo(CHANGED_PERSON_LAST_NAME);
        assertThat(actualStudent.getEmail()).isEqualTo(CHANGED_PERSON_EMAIL);
        assertThat(actualStudent.getComment()).isEqualTo(CHANGED_PERSON_COMMENT);
        assertThat(actualStudent.getGroup()).isEqualTo(group);
        assertFalse(actualStudent.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testEdit() throws Exception {
        mockMvc.perform(get("/students/student/new")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(containsString("<h1 class=\"header\">New student</h1>"))));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAdd() throws Exception {
        mockMvc.perform(post("/students/student/add")
                .param("firstName", CHANGED_PERSON_FIRST_NAME)
                .param("lastName", CHANGED_PERSON_LAST_NAME)
                .param("email", CHANGED_PERSON_EMAIL)
                .param("comment", CHANGED_PERSON_COMMENT)
                .param("group.id", GROUP_ID_502.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("<td><a href=\"/students/student?id=10001\">new or changed first name new or changed last name</a></td>"),
                        containsString("<td><a href=\"/students/group?id=502\">BB-02</a></td>")))
                );

        Student actualStudent = studentService.getById(NEW_COURSE_ID);
        Group group = groupService.getById(GROUP_ID_502);

        assertNotNull(actualStudent);
        assertThat(actualStudent.getFirstName()).isEqualTo(CHANGED_PERSON_FIRST_NAME);
        assertThat(actualStudent.getLastName()).isEqualTo(CHANGED_PERSON_LAST_NAME);
        assertThat(actualStudent.getEmail()).isEqualTo(CHANGED_PERSON_EMAIL);
        assertThat(actualStudent.getComment()).isEqualTo(CHANGED_PERSON_COMMENT);
        assertThat(actualStudent.getGroup()).isEqualTo(group);
        assertTrue(actualStudent.isActive());
    }
}
