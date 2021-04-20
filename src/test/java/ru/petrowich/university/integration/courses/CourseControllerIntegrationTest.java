package ru.petrowich.university.integration.courses;

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
import java.util.List;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class CourseControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer COURSE_ID_51 = 51;
    private static final Integer NEW_COURSE_ID = 10;
    private static final String COURSE_NAME_51 = "math";
    private static final String CHANGED_COURSE_NAME = "new or changed name";
    private static final String COURSE_DESCRIPTION_51 = "exact";
    private static final String CHANGED_COURSE_DESCRIPTION = "new or changed description";
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;
    private static final Integer GROUP_ID_501 = 501;
    private static final Integer GROUP_ID_503 = 503;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private GroupService groupService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testCourses() throws Exception {
        mockMvc.perform(get("/courses")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<a href=\"/courses/course?id=51\" class=\"card-title\">"),
                        containsString("<h5>math</h5>"),
                        containsString("<a href=\"/lecturers/lecturer?id=50005\" class=\"card-link\">Отряд Ковбоев</a>"),
                        containsString("<a href=\"/courses/course?id=55\" class=\"card-title text-dark\">"),
                        containsString("<h5>psychology</h5>"),
                        containsString("<a href=\"/lecturers/lecturer?id=50006\" class=\"card-link\">Ушат Помоев</a>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testCourse() throws Exception {
        mockMvc.perform(get("/courses/course?id=51")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"display-4\">math</h1>"),
                        stringContainsInOrder("href=\"/lecturers/lecturer?id=50005\"", ">Отряд Ковбоев</a>"),
                        stringContainsInOrder("href=\"/students/group?id=501\"", ">AA-01</a>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testEdit() throws Exception {
        mockMvc.perform(get("/courses/course/edit?id=51")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"header\">edit course</h1>"),
                        containsString("<input type=\"text\" class=\"form-control\" id=\"courseName\" placeholder=\"Course Name\" required name=\"name\" value=\"math\">"),
                        containsString("<option value=\"50005\" selected=\"selected\">Отряд Ковбоев</option>"),
                        containsString("<form action=\"/courses/course/update?id=51\" method=\"post\" class=\"needs-validation\" novalidate>"),
                        stringContainsInOrder("<textarea type=\"text\" class=\"form-control\" id=\"description\" placeholder=\"Description\"", "name=\"description\">exact</textarea>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdate() throws Exception {
        Course currentCourse = courseService.getById(COURSE_ID_51);
        Lecturer currentLecturer = lecturerService.getById(PERSON_ID_50005);

        assertThat(currentCourse.getName()).isEqualTo(COURSE_NAME_51);
        assertThat(currentCourse.getDescription()).isEqualTo(COURSE_DESCRIPTION_51);
        assertThat(currentCourse.getAuthor()).isEqualTo(currentLecturer);
        assertTrue(currentCourse.isActive());

        mockMvc.perform(post("/courses/course/update?id=51")
                .param("name", CHANGED_COURSE_NAME)
                .param("description", CHANGED_COURSE_DESCRIPTION)
                .param("author.id", PERSON_ID_50006.toString())
                .param("actual", "false")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("<a href=\"/courses/course?id=51\" class=\"card-title text-dark\">"),
                        containsString("<h5>new or changed name</h5>"),
                        containsString("<p class=\"card-text\">new or changed description</p>")))
                );

        Course actualCourse = courseService.getById(COURSE_ID_51);
        Lecturer actualLecturer = lecturerService.getById(PERSON_ID_50006);

        assertThat(actualCourse.getName()).isEqualTo(CHANGED_COURSE_NAME);
        assertThat(actualCourse.getDescription()).isEqualTo(CHANGED_COURSE_DESCRIPTION);
        assertThat(actualCourse.getAuthor()).isEqualTo(actualLecturer);
        assertFalse(actualCourse.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testCreate() throws Exception {
        mockMvc.perform(get("/courses/course/new")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(containsString("<h1 class=\"header\">new course</h1>"),
                        containsString("<option value=\"50005\">Отряд Ковбоев</option>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAdd() throws Exception {
        mockMvc.perform(post("/courses/course/add")
                .param("name", CHANGED_COURSE_NAME)
                .param("description", CHANGED_COURSE_DESCRIPTION)
                .param("author.id", PERSON_ID_50005.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(containsString("<h1 class=\"text-center\">Courses</h1>"),
                        containsString("<h5>new or changed name</h5>"),
                        containsString("<a href=\"/lecturers/lecturer?id=50005\" class=\"card-link\">Отряд Ковбоев</a>")))
                );

        Course actualCourse = courseService.getById(NEW_COURSE_ID);
        Lecturer lecturer = lecturerService.getById(PERSON_ID_50005);

        assertNotNull(actualCourse);
        assertThat(actualCourse.getName()).isEqualTo(CHANGED_COURSE_NAME);
        assertThat(actualCourse.getDescription()).isEqualTo(CHANGED_COURSE_DESCRIPTION);
        assertThat(actualCourse.getAuthor()).isEqualTo(lecturer);
        assertTrue(actualCourse.isActive());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAssignGroup() throws Exception {
        Group additionalGroup = groupService.getById(GROUP_ID_503);

        List<Group> currentCourseGroups = courseService.getById(COURSE_ID_51).getGroups();
        assertThat(currentCourseGroups).doesNotContain(additionalGroup).isNotEmpty();

        mockMvc.perform(post("/courses/course/assign-group")
                .param("courseId", COURSE_ID_51.toString())
                .param("groupId", GROUP_ID_503.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"display-4\">math</h1>"),
                        containsString("<td><a href=\"/students/group?id=501\">AA-01</a></td>"),
                        containsString("<td><a href=\"/students/group?id=502\">BB-02</a></td>"),
                        containsString("<td><a href=\"/students/group?id=503\">CC-03</a></td>"),
                        containsString("<option value=\"\"></option>")))
                );

        List<Group> actualCourseGroups = courseService.getById(COURSE_ID_51).getGroups();
        assertThat(actualCourseGroups).contains(additionalGroup).isNotEmpty();
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testRemoveGroup() throws Exception {
        Group removableGroup = groupService.getById(GROUP_ID_501);

        List<Group> currentCourseGroups = courseService.getById(COURSE_ID_51).getGroups();
        assertThat(currentCourseGroups).contains(removableGroup).isNotEmpty();

        mockMvc.perform(post("/courses/course/remove-group")
                .param("courseId", COURSE_ID_51.toString())
                .param("groupId", GROUP_ID_501.toString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"display-4\">math</h1>"),
                        containsString("<td><a href=\"/students/group?id=502\">BB-02</a></td>"),
                        containsString("<option value=\"501\">AA-01</option>")))
                );

        List<Group> actualCourseGroups = courseService.getById(COURSE_ID_51).getGroups();
        assertThat(actualCourseGroups).doesNotContain(removableGroup).isNotEmpty();
    }
}
