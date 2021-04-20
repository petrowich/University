package ru.petrowich.university.integration.courses;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.LecturerService;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class CourseRestControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer COURSE_ID_51 = 51;
    private static final Integer NEW_COURSE_ID = 10;
    private static final Integer NONEXISTENT_COURSE_ID = 99;
    private static final String COURSE_NAME_51 = "math";
    private static final String CHANGED_COURSE_NAME = "new or changed name";
    private static final String COURSE_DESCRIPTION_51 = "exact";
    private static final String CHANGED_COURSE_DESCRIPTION = "new or changed description";
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_502 = 502;
    private static final Integer GROUP_ID_503 = 503;
    private static final Integer NONEXISTENT_GROUP_ID = 999;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private LecturerService lecturerService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetCourseShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/courses/{id}", COURSE_ID_51)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(COURSE_ID_51))
                .andExpect(jsonPath("$.groups.*", hasSize(2)));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetCourseShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(get("/api/courses/{id}", NONEXISTENT_COURSE_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAddCourseShouldReturnCreated() throws Exception {
        String newCourseJSON = "{\"name\":\"" + CHANGED_COURSE_NAME + "\""
                + ",\"description\":\"" + CHANGED_COURSE_DESCRIPTION + "\""
                + ",\"authorId\":" + PERSON_ID_50006
                + "}";

        mockMvc.perform(post("/api/courses/add")
                .content(newCourseJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_COURSE_ID));

        Course actualCourse = courseService.getById(NEW_COURSE_ID);
        Lecturer actualLecturer = lecturerService.getById(PERSON_ID_50006);

        assertThat(actualCourse.getName()).isEqualTo(CHANGED_COURSE_NAME);
        assertThat(actualCourse.getDescription()).isEqualTo(CHANGED_COURSE_DESCRIPTION);
        assertThat(actualCourse.getAuthor()).isEqualTo(actualLecturer);
        assertTrue(actualCourse.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateCourseShouldReturnOK() throws Exception {
        String courseJSON = "{\"id\":" + COURSE_ID_51
                + ",\"name\":\"" + CHANGED_COURSE_NAME + "\""
                + ",\"description\":\"" + CHANGED_COURSE_DESCRIPTION + "\""
                + ",\"authorId\":" + PERSON_ID_50006
                + "}";

        Course currentCourse = courseService.getById(COURSE_ID_51);
        Lecturer currentLecturer = lecturerService.getById(PERSON_ID_50005);

        assertThat(currentCourse.getName()).isEqualTo(COURSE_NAME_51);
        assertThat(currentCourse.getDescription()).isEqualTo(COURSE_DESCRIPTION_51);
        assertThat(currentCourse.getAuthor()).isEqualTo(currentLecturer);
        assertTrue(currentCourse.isActive());

        mockMvc.perform(put("/api/courses/update/{id}", COURSE_ID_51)
                .content(courseJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(COURSE_ID_51))
                .andExpect(jsonPath("$.authorId").value(PERSON_ID_50006));

        Course actualCourse = courseService.getById(COURSE_ID_51);
        Lecturer actualLecturer = lecturerService.getById(PERSON_ID_50006);

        assertThat(actualCourse.getName()).isEqualTo(CHANGED_COURSE_NAME);
        assertThat(actualCourse.getDescription()).isEqualTo(CHANGED_COURSE_DESCRIPTION);
        assertThat(actualCourse.getAuthor()).isEqualTo(actualLecturer);
        assertTrue(actualCourse.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateCourseShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String courseJSON = "{\"name\":\"" + CHANGED_COURSE_NAME + "\"}";

        mockMvc.perform(put("/api/courses/update/{id}", NONEXISTENT_COURSE_ID)
                .content(courseJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteCourseShouldReturnOK() throws Exception {
        Course currentCourse = courseService.getById(COURSE_ID_51);
        assertTrue(currentCourse.isActive());

        mockMvc.perform(delete("/api/courses/delete/{id}", COURSE_ID_51))
                .andDo(print())
                .andExpect(status().isOk());

        Course actualCourse = courseService.getById(COURSE_ID_51);
        assertFalse(actualCourse.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteCourseShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(delete("/api/courses/delete/{id}", NONEXISTENT_COURSE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetAllCoursesShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/courses/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.[0].id").value(COURSE_ID_51));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAssignGroupShouldReturnOK() throws Exception {
        String courseGroupAssignmentJSON = "{\"courseId\":" + COURSE_ID_51
                + ",\"groupId\":" + GROUP_ID_503
                + "}";

        mockMvc.perform(put("/api/courses/assign-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(GROUP_ID_501))
                .andExpect(jsonPath("$.[2].id").value(GROUP_ID_503))
                .andExpect(jsonPath("$.*", hasSize(3)));

        Course actualCourse = courseService.getById(COURSE_ID_51);
        Group group = groupService.getById(GROUP_ID_503);
        assertThat(actualCourse.getGroups(), hasItem(group));

    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAssignGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String courseGroupAssignmentJSON = "{\"courseId\":" + NONEXISTENT_COURSE_ID
                + ",\"groupId\":" + GROUP_ID_503
                + "}";

        mockMvc.perform(put("/api/courses/assign-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        courseGroupAssignmentJSON = "{\"courseId\":" + COURSE_ID_51
                + ",\"groupId\":" + NONEXISTENT_GROUP_ID
                + "}";

        mockMvc.perform(put("/api/courses/assign-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testRemoveGroupShouldReturnOK() throws Exception {
        Group group = groupService.getById(GROUP_ID_501);

        Course currentCourse = courseService.getById(COURSE_ID_51);
        MatcherAssert.assertThat(currentCourse.getGroups(), hasItem(group));

        String courseGroupAssignmentJSON = "{\"courseId\":" + COURSE_ID_51
                + ",\"groupId\":" + GROUP_ID_501
                + "}";

        mockMvc.perform(put("/api/courses/remove-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(GROUP_ID_502))
                .andExpect(jsonPath("$.*", hasSize(1)));

        Course actualCourse = courseService.getById(COURSE_ID_51);
        assertThat(actualCourse.getGroups(), not(hasItem(group)));
    }

    @Test
    void testRemoveGroupShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String courseGroupAssignmentJSON = "{\"courseId\":" + NONEXISTENT_COURSE_ID
                + ",\"groupId\":" + GROUP_ID_501
                + "}";

        mockMvc.perform(put("/api/courses/remove-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        courseGroupAssignmentJSON = "{\"courseId\":" + COURSE_ID_51
                + ",\"groupId\":" + NONEXISTENT_GROUP_ID
                + "}";

        mockMvc.perform(put("/api/courses/remove-group")
                .content(courseGroupAssignmentJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
