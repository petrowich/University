package ru.petrowich.university.integration.students;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

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
class StudentRestControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer NEW_PERSON_ID = 10001;
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String PERSON_FIRST_NAME_50005 = "Рулон";
    private static final String ANOTHER_PERSON_FIRST_NAME = "some first name";
    private static final String PERSON_LAST_NAME_50005 = "Обоев";
    private static final String ANOTHER_PERSON_LAST_NAME = "some last name";
    private static final String PERSON_EMAIL_50005 = "rulon.oboev@university.edu";
    private static final String ANOTHER_PERSON_EMAIL = "first.last@university.edu";
    private static final String PERSON_COMMENT_50001 = "stupid";
    private static final String ANOTHER_COMMENT = "some comment";
    private static final Integer GROUP_ID_501 = 501;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetStudentShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/students/{id}", PERSON_ID_50001)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(PERSON_ID_50001))
                .andExpect(jsonPath("$.groupId").value(GROUP_ID_501))
                .andExpect(jsonPath("$.courses.*", hasSize(3)));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetStudentShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(get("/api/students/{id}", NONEXISTENT_PERSON_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAddStudentShouldReturnCreated() throws Exception {
        String newLecturerJSON = "{\"firstName\":\"" + ANOTHER_PERSON_FIRST_NAME + "\""
                + ",\"lastName\":\"" + ANOTHER_PERSON_LAST_NAME + "\""
                + ",\"email\":\"" + ANOTHER_PERSON_EMAIL + "\""
                + ",\"comment\":\"" + ANOTHER_COMMENT + "\""
                + "}";

        mockMvc.perform(post("/api/students/add")
                .content(newLecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_PERSON_ID));

        Student actualStudent = studentService.getById(NEW_PERSON_ID);
        assertThat(actualStudent.getFirstName()).isEqualTo(ANOTHER_PERSON_FIRST_NAME);
        assertThat(actualStudent.getLastName()).isEqualTo(ANOTHER_PERSON_LAST_NAME);
        assertThat(actualStudent.getEmail()).isEqualTo(ANOTHER_PERSON_EMAIL);
        assertThat(actualStudent.getComment()).isEqualTo(ANOTHER_COMMENT);
        assertTrue(actualStudent.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateStudentShouldReturnOK() throws Exception {
        String lecturerJSON = "{\"id\":" + PERSON_ID_50001
                + ",\"firstName\":\"" + ANOTHER_PERSON_FIRST_NAME + "\""
                + ",\"lastName\":\"" + ANOTHER_PERSON_LAST_NAME + "\""
                + ",\"email\":\"" + ANOTHER_PERSON_EMAIL + "\""
                + ",\"comment\":\"" + ANOTHER_COMMENT + "\""
                + "}";

        Student currentStudent = studentService.getById(PERSON_ID_50001);
        assertThat(currentStudent.getFirstName()).isEqualTo(PERSON_FIRST_NAME_50005);
        assertThat(currentStudent.getLastName()).isEqualTo(PERSON_LAST_NAME_50005);
        assertThat(currentStudent.getEmail()).isEqualTo(PERSON_EMAIL_50005);
        assertThat(currentStudent.getComment()).isEqualTo(PERSON_COMMENT_50001);
        assertTrue(currentStudent.isActive());

        mockMvc.perform(put("/api/students/update/{id}", PERSON_ID_50001)
                .content(lecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(PERSON_ID_50001))
                .andExpect(jsonPath("$.email").value(ANOTHER_PERSON_EMAIL));

        Student actualStudent = studentService.getById(PERSON_ID_50001);
        assertThat(actualStudent.getFirstName()).isEqualTo(ANOTHER_PERSON_FIRST_NAME);
        assertThat(actualStudent.getLastName()).isEqualTo(ANOTHER_PERSON_LAST_NAME);
        assertThat(actualStudent.getEmail()).isEqualTo(ANOTHER_PERSON_EMAIL);
        assertThat(actualStudent.getComment()).isEqualTo(ANOTHER_COMMENT);
        assertTrue(actualStudent.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateStudentShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String lecturerJSON = "{\"id\":" + PERSON_ID_50001
                + ",\"firstName\":\"" + ANOTHER_PERSON_FIRST_NAME + "\""
                + ",\"lastName\":\"" + ANOTHER_PERSON_LAST_NAME + "\""
                + ",\"email\":\"" + ANOTHER_PERSON_EMAIL + "\""
                + ",\"comment\":\"" + ANOTHER_COMMENT + "\""
                + "}";

        mockMvc.perform(put("/api/students/update/{id}", NONEXISTENT_PERSON_ID)
                .content(lecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteStudentShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/students/delete/{id}", PERSON_ID_50001))
                .andDo(print())
                .andExpect(status().isOk());

        Student actualStudent = studentService.getById(PERSON_ID_50001);
        assertFalse(actualStudent.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteStudentShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(delete("/api/students/delete/{id}", NONEXISTENT_PERSON_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetAllStudentsShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/students/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.[0].id").value(PERSON_ID_50001));
    }
}
