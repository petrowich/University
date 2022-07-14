package ru.petrowich.university.integration.lecturers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.LecturerService;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class LecturerControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer NEW_COURSE_ID = 10001;
    private static final String PERSON_FIRST_NAME_50005 = "Reinhard";
    private static final String PERSON_LAST_NAME_50005 = "Genzel";
    private static final String PERSON_EMAIL_50005 = "reinhard.genzel@university.edu";
    private static final String PERSON_COMMENT_50005 = "";

    private static final String CHANGED_PERSON_FIRST_NAME = "new or changed first name";
    private static final String CHANGED_PERSON_LAST_NAME = "new or changed last name";
    private static final String CHANGED_PERSON_EMAIL = "new.changed@university.edu";
    private static final String CHANGED_PERSON_COMMENT = "some description";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private CourseService courseService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testLecturers() throws Exception {
        mockMvc.perform(get("/lecturers")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"text-center\">Lecturers</h1>"),
                        containsString("<td><a href=\"/lecturers/lecturer?id=50005\">Reinhard Genzel</a></td>"),
                        containsString("<td><a href=\"/lecturers/lecturer?id=50006\" class=\"text-danger\">Roger Penrose</a></td>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testLecturer() throws Exception {
        mockMvc.perform(get("/lecturers/lecturer?id=50005")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"display-4\">Reinhard Genzel</h1>"),
                        containsString("<a href=\"mailto:reinhard.genzel@university.edu\">reinhard.genzel@university.edu</a>"),
                        containsString("<td><a href=\"/courses/course?id=51\">math</a><td>"),
                        containsString("<td><a href=\"/courses/course?id=52\">biology</a><td>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testEdit() throws Exception {
        mockMvc.perform(get("/lecturers/lecturer/edit?id=50005")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"header\">Edit lecturer</h1>"),
                        containsString("<input type=\"text\" class=\"form-control\" id=\"firstName\" placeholder=\"First Name\" required name=\"firstName\" value=\"Reinhard\">"),
                        containsString("<input type=\"text\" class=\"form-control\" id=\"lastName\" placeholder=\"Last Name\" required name=\"lastName\" value=\"Genzel\">"),
                        containsString("<input type=\"email\" class=\"form-control\" id=\"email\" placeholder=\"Enter email\" required name=\"email\" value=\"reinhard.genzel@university.edu\">"),
                        stringContainsInOrder("<input type=\"checkbox\"", "value=\"true\" checked=\"checked\"><input type=\"hidden\" name=\"_active\" value=\"on\"/>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdate() throws Exception {
        Lecturer currentLecturer = lecturerService.getById(PERSON_ID_50005);

        assertThat(currentLecturer.getFirstName()).isEqualTo(PERSON_FIRST_NAME_50005);
        assertThat(currentLecturer.getLastName()).isEqualTo(PERSON_LAST_NAME_50005);
        assertThat(currentLecturer.getEmail()).isEqualTo(PERSON_EMAIL_50005);
        assertThat(currentLecturer.getComment()).isEqualTo(PERSON_COMMENT_50005);
        assertTrue(currentLecturer.isActive());

        mockMvc.perform(post("/lecturers/lecturer/update?id=50005")
                .param("firstName", CHANGED_PERSON_FIRST_NAME)
                .param("lastName", CHANGED_PERSON_LAST_NAME)
                .param("email", CHANGED_PERSON_EMAIL)
                .param("comment", CHANGED_PERSON_COMMENT)
                .param("actual", "false")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturers/"));

        mockMvc.perform(get("/lecturers/")
                        .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<td><a href=\"/lecturers/lecturer?id=50005\" class=\"text-danger\">new or changed first name new or changed last name</a></td>"),
                        containsString("<td><a href=\"/lecturers/lecturer?id=50006\" class=\"text-danger\">Roger Penrose</a></td>")))
                );

        Lecturer actualLecturer = lecturerService.getById(PERSON_ID_50005);

        assertThat(actualLecturer.getFirstName()).isEqualTo(CHANGED_PERSON_FIRST_NAME);
        assertThat(actualLecturer.getLastName()).isEqualTo(CHANGED_PERSON_LAST_NAME);
        assertThat(actualLecturer.getEmail()).isEqualTo(CHANGED_PERSON_EMAIL);
        assertThat(actualLecturer.getComment()).isEqualTo(CHANGED_PERSON_COMMENT);
        assertFalse(actualLecturer.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testCreate() throws Exception {
        mockMvc.perform(get("/lecturers/lecturer/new")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(containsString("<h1 class=\"header\">New lecturer</h1>"))));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAdd() throws Exception {
        mockMvc.perform(post("/lecturers/lecturer/add")
                .param("firstName", CHANGED_PERSON_FIRST_NAME)
                .param("lastName", CHANGED_PERSON_LAST_NAME)
                .param("email", CHANGED_PERSON_EMAIL)
                .param("comment", CHANGED_PERSON_COMMENT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturers/"));

        mockMvc.perform(get("/lecturers/")
                        .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<td><a href=\"/lecturers/lecturer?id=50005\">Reinhard Genzel</a></td>"),
                        containsString("<td><a href=\"/lecturers/lecturer?id=10001\">new or changed first name new or changed last name</a></td>"),
                        containsString("<td><a href=\"/lecturers/lecturer?id=50006\" class=\"text-danger\">Roger Penrose</a></td>")))
                );

        Lecturer actualLecturer = lecturerService.getById(NEW_COURSE_ID);

        assertNotNull(actualLecturer);
        assertThat(actualLecturer.getFirstName()).isEqualTo(CHANGED_PERSON_FIRST_NAME);
        assertThat(actualLecturer.getLastName()).isEqualTo(CHANGED_PERSON_LAST_NAME);
        assertThat(actualLecturer.getEmail()).isEqualTo(CHANGED_PERSON_EMAIL);
        assertThat(actualLecturer.getComment()).isEqualTo(CHANGED_PERSON_COMMENT);
        assertTrue(actualLecturer.isActive());
    }
}
