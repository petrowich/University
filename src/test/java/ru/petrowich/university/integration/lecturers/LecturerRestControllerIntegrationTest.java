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
import ru.petrowich.university.service.LecturerService;

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
class LecturerRestControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer NEW_PERSON_ID = 10001;
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer NONEXISTENT_PERSON_ID = 99999;
    private static final String PERSON_FIRST_NAME_50005 = "Reinhard";
    private static final String ANOTHER_PERSON_FIRST_NAME = "some first name";
    private static final String PERSON_LAST_NAME_50005 = "Genzel";
    private static final String ANOTHER_PERSON_LAST_NAME = "some last name";
    private static final String PERSON_EMAIL_50005 = "reinhard.genzel@university.edu";
    private static final String ANOTHER_PERSON_EMAIL = "first.last@university.edu";
    private static final String PERSON_COMMENT_50005 = "";
    private static final String ANOTHER_COMMENT = "some comment";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LecturerService lecturerService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetLecturerShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/lecturers/{id}", PERSON_ID_50005)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(PERSON_ID_50005))
                .andExpect(jsonPath("$.courses.*", hasSize(2)));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetLecturerShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(get("/api/lecturers/{id}", NONEXISTENT_PERSON_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAddLecturerShouldReturnCreated() throws Exception {
        String newLecturerJSON = "{\"firstName\":\"" + ANOTHER_PERSON_FIRST_NAME + "\""
                + ",\"lastName\":\"" + ANOTHER_PERSON_LAST_NAME + "\""
                + ",\"email\":\"" + ANOTHER_PERSON_EMAIL + "\""
                + ",\"comment\":\"" + ANOTHER_COMMENT + "\""
                + "}";

        mockMvc.perform(post("/api/lecturers/add")
                .content(newLecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_PERSON_ID));

        Lecturer actualLecturer = lecturerService.getById(NEW_PERSON_ID);
        assertThat(actualLecturer.getFirstName()).isEqualTo(ANOTHER_PERSON_FIRST_NAME);
        assertThat(actualLecturer.getLastName()).isEqualTo(ANOTHER_PERSON_LAST_NAME);
        assertThat(actualLecturer.getEmail()).isEqualTo(ANOTHER_PERSON_EMAIL);
        assertThat(actualLecturer.getComment()).isEqualTo(ANOTHER_COMMENT);
        assertTrue(actualLecturer.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateLecturerShouldReturnOK() throws Exception {
        String lecturerJSON = "{\"id\":" + PERSON_ID_50005
                + ",\"firstName\":\"" + ANOTHER_PERSON_FIRST_NAME + "\""
                + ",\"lastName\":\"" + ANOTHER_PERSON_LAST_NAME + "\""
                + ",\"email\":\"" + ANOTHER_PERSON_EMAIL + "\""
                + ",\"comment\":\"" + ANOTHER_COMMENT + "\""
                + "}";

        Lecturer currentLecturer = lecturerService.getById(PERSON_ID_50005);
        assertThat(currentLecturer.getFirstName()).isEqualTo(PERSON_FIRST_NAME_50005);
        assertThat(currentLecturer.getLastName()).isEqualTo(PERSON_LAST_NAME_50005);
        assertThat(currentLecturer.getEmail()).isEqualTo(PERSON_EMAIL_50005);
        assertThat(currentLecturer.getComment()).isEqualTo(PERSON_COMMENT_50005);
        assertTrue(currentLecturer.isActive());

        mockMvc.perform(put("/api/lecturers/update/{id}", PERSON_ID_50005)
                .content(lecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(PERSON_ID_50005))
                .andExpect(jsonPath("$.email").value(ANOTHER_PERSON_EMAIL));

        Lecturer actualLecturer = lecturerService.getById(PERSON_ID_50005);
        assertThat(actualLecturer.getFirstName()).isEqualTo(ANOTHER_PERSON_FIRST_NAME);
        assertThat(actualLecturer.getLastName()).isEqualTo(ANOTHER_PERSON_LAST_NAME);
        assertThat(actualLecturer.getEmail()).isEqualTo(ANOTHER_PERSON_EMAIL);
        assertThat(actualLecturer.getComment()).isEqualTo(ANOTHER_COMMENT);
        assertTrue(actualLecturer.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateLecturerShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String lecturerJSON = "{\"id\":" + PERSON_ID_50005
                + ",\"firstName\":\"" + ANOTHER_PERSON_FIRST_NAME + "\""
                + ",\"lastName\":\"" + ANOTHER_PERSON_LAST_NAME + "\""
                + ",\"email\":\"" + ANOTHER_PERSON_EMAIL + "\""
                + ",\"comment\":\"" + ANOTHER_COMMENT + "\""
                + "}";

        mockMvc.perform(put("/api/lecturers/update/{id}", NONEXISTENT_PERSON_ID)
                .content(lecturerJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteLecturerShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/lecturers/delete/{id}", PERSON_ID_50005))
                .andDo(print())
                .andExpect(status().isOk());

        Lecturer actualLecturer = lecturerService.getById(PERSON_ID_50005);
        assertFalse(actualLecturer.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteLecturerShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(delete("/api/lecturers/delete/{id}", NONEXISTENT_PERSON_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetAllLecturersShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/lecturers/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(PERSON_ID_50005));
    }
}
