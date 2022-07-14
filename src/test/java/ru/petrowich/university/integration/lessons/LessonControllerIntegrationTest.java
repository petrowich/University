package ru.petrowich.university.integration.lessons;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.LessonService;
import ru.petrowich.university.service.TimeSlotService;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class LessonControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long NEW_LESSON_ID = 1000001L;
    private static final LocalDate NEW_LESSON_DATE = LocalDate.of(2025, 1, 1);
    private static final Integer COURSE_ID_53 = 53;
    private static final Integer TIME_SLOT_ID_2 = 2;
    private static final LocalTime TIME_SLOT_START_TIME_1 = LocalTime.of(9, 40);
    private static final LocalTime TIME_SLOT_END_TIME_1 = LocalTime.of(11, 10);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testLessons() throws Exception {
        mockMvc.perform(get("/lessons")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"text-center\">Lessons</h1>"),
                        containsString("<td>2020-06-01</td>"),
                        containsString("<td>08:00 - 09:30</td>"),
                        containsString("<td><a href=\"/courses/course?id=51\">math</a></td>"),
                        containsString("<td><a href=\"/lecturers/lecturer?id=50005\">Reinhard Genzel</a></td>"),
                        containsString("<td class=\"text-center\">3</td>"),
                        containsString("<td><a href=\"/lessons/lesson/edit?id=5000001\" class=\"btn btn-light fas fa-edit\"></a></td>"),
                        containsString("<td><form action=\"/lessons/lesson/delete?id=5000001\" method=\"post\">")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testCreate() throws Exception {
        mockMvc.perform(get("/lessons/lesson/new")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"header\">new lesson</h1>"),
                        containsString("<option value=\"52\">biology</option>"),
                        containsString("<option value=\"51\">math</option>"),
                        containsString("<option value=\"1\">first lesson</option>"),
                        containsString("<option value=\"2\">second lesson</option>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAdd() throws Exception {
        mockMvc.perform(post("/lessons/lesson/add")
                .param("course.id", "53")
                .param("date", "2025-01-01")
                .param("timeSlot.id", "2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons/"));

        mockMvc.perform(get("/lessons/")
                        .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"text-center\">Lessons</h1>"),
                        containsString("<td>2025-01-01</td>"),
                        containsString("<td><a href=\"/lessons/lesson/edit?id=1000001\" class=\"btn btn-light fas fa-edit\"></a></td>"),
                        containsString("<td><form action=\"/lessons/lesson/delete?id=1000001\" method=\"post\">")))
                );

        Lesson actualLesson = lessonService.getById(NEW_LESSON_ID);
        Course course = courseService.getById(COURSE_ID_53);
        TimeSlot timeSlot = timeSlotService.getById(TIME_SLOT_ID_2);

        assertNotNull(actualLesson);
        assertThat(actualLesson.getCourse()).isEqualTo(course);
        assertThat(actualLesson.getLecturer()).isEqualTo(course.getAuthor());
        assertThat(actualLesson.getDate()).isEqualTo(NEW_LESSON_DATE);
        assertThat(actualLesson.getTimeSlot()).isEqualTo(timeSlot);
        assertThat(actualLesson.getStartTime()).isEqualTo(TIME_SLOT_START_TIME_1);
        assertThat(actualLesson.getEndTime()).isEqualTo(TIME_SLOT_END_TIME_1);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testEdit() throws Exception {
        mockMvc.perform(get("/lessons/lesson/edit?id=5000001")
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"header\">Edit lesson</h1>"),
                        containsString("<input type=\"date\" class=\"form-control\" id=\"date\" name=\"date\" value=\"2020-06-01\">"),
                        containsString("<input type=\"time\" class=\"form-control\" id=\"startTime\" name=\"startTime\" value=\"08:00\">"),
                        containsString("<input type=\"time\" class=\"form-control\" id=\"endTime\" name=\"endTime\" value=\"09:30\">"),
                        containsString("<option value=\"51\" selected=\"selected\">math</option>"),
                        containsString("<option value=\"50005\" selected=\"selected\">Reinhard Genzel</option>")))
                );
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdate() throws Exception {
        mockMvc.perform(post("/lessons/lesson/update?id=5000001")
                .param("course.id", COURSE_ID_53.toString())
                .param("lecturer.id", "50006")
                .param("date", "2025-01-01")
                .param("startTime", "09:40")
                .param("endTime", "11:10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lessons/"));

        mockMvc.perform(get("/lessons/")
                        .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE))
                .andExpect(content().string(allOf(
                        containsString("<h1 class=\"text-center\">Lessons</h1>"),
                        containsString("<td>2025-01-01</td>"),
                        containsString("<td><a href=\"/lessons/lesson/edit?id=5000001\" class=\"btn btn-light fas fa-edit\"></a></td>"),
                        containsString("<td><form action=\"/lessons/lesson/delete?id=5000001\" method=\"post\">")))
                );

        Lesson actualLesson = lessonService.getById(LESSON_ID_5000001);
        Course course = courseService.getById(COURSE_ID_53);

        assertThat(actualLesson.getCourse()).isEqualTo(course);
        assertThat(actualLesson.getLecturer()).isEqualTo(course.getAuthor());
        assertThat(actualLesson.getDate()).isEqualTo(NEW_LESSON_DATE);
        assertThat(actualLesson.getStartTime()).isEqualTo(TIME_SLOT_START_TIME_1);
        assertThat(actualLesson.getEndTime()).isEqualTo(TIME_SLOT_END_TIME_1);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDelete() throws Exception {
        mockMvc.perform(post("/lessons/lesson/delete?id=5000001")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .contentType(MediaType.TEXT_HTML))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        Lesson actualLesson = lessonService.getById(LESSON_ID_5000001);
        assertNull(actualLesson);
    }
}
