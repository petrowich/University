package ru.petrowich.university.integration.lessons;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.LecturerService;
import ru.petrowich.university.service.LessonService;
import ru.petrowich.university.service.TimeSlotService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
class LessonRestControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long NEW_LESSON_ID = 1000001L;
    private static final Long NONEXISTENT_LESSON_ID = 9999999L;
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_53 = 53;
    private static final Integer TIME_SLOT_ID_1 = 1;
    private static final Integer TIME_SLOT_ID_2 = 2;
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;
    private static final LocalDate LESSON_DATE_5000001 = LocalDate.of(2020, 6, 1);
    private static final LocalDate NEW_LESSON_DATE = LocalDate.of(2025, 1, 1);
    private static final LocalTime LESSON_START_TIME_5000001 = LocalTime.of(8, 00);
    private static final LocalTime LESSON_END_TIME_5000001 = LocalTime.of(9, 30);
    private static final LocalTime NEW_LESSON_START_TIME = LocalTime.of(9, 40);
    private static final LocalTime NEW_LESSON_END_TIME = LocalTime.of(11, 10);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetLessonShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/lessons/{id}", LESSON_ID_5000001)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(LESSON_ID_5000001))
                .andExpect(jsonPath("$.courseId").value(COURSE_ID_51));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetLessonShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(get("/api/lessons/{id}", NONEXISTENT_LESSON_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAddLessonShouldReturnCreated() throws Exception {
        String newLessonJSON = "{\"courseId\":" + COURSE_ID_53
                + ",\"lecturerId\":" + PERSON_ID_50005
                + ",\"date\":\"" + NEW_LESSON_DATE + "\""
                + ",\"timeSlotId\":" + TIME_SLOT_ID_2
                + ",\"startTime\":\"" + NEW_LESSON_START_TIME + "\""
                + ",\"endTime\":\"" + NEW_LESSON_END_TIME + "\""
                + "}";

        mockMvc.perform(post("/api/lessons/add")
                .content(newLessonJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_LESSON_ID));

        Lesson actualLesson = lessonService.getById(NEW_LESSON_ID);
        Course course = courseService.getById(COURSE_ID_53);
        Lecturer lecturer = lecturerService.getById(PERSON_ID_50005);
        TimeSlot timeSlot = timeSlotService.getById(TIME_SLOT_ID_2);

        assertNotNull(actualLesson);
        assertThat(actualLesson.getCourse()).isEqualTo(course);
        assertThat(actualLesson.getLecturer()).isEqualTo(lecturer);
        assertThat(actualLesson.getDate()).isEqualTo(NEW_LESSON_DATE);
        assertThat(actualLesson.getTimeSlot()).isEqualTo(timeSlot);
        assertThat(actualLesson.getStartTime()).isEqualTo(NEW_LESSON_START_TIME);
        assertThat(actualLesson.getEndTime()).isEqualTo(NEW_LESSON_END_TIME);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateLessonShouldReturnOK() throws Exception {
        String lessonJSON = "{\"courseId\":" + COURSE_ID_53
                + ",\"lecturerId\":" + PERSON_ID_50006
                + ",\"date\":\"" + NEW_LESSON_DATE + "\""
                + ",\"timeSlotId\":" + TIME_SLOT_ID_2
                + ",\"startTime\":\"" + NEW_LESSON_START_TIME + "\""
                + ",\"endTime\":\"" + NEW_LESSON_END_TIME + "\""
                + "}";

        Lesson currentLesson = lessonService.getById(LESSON_ID_5000001);
        Course currentCourse = courseService.getById(COURSE_ID_51);
        Lecturer currentLecturer = lecturerService.getById(PERSON_ID_50005);
        TimeSlot currentTimeSlot = timeSlotService.getById(TIME_SLOT_ID_1);

        assertThat(currentLesson.getCourse()).isEqualTo(currentCourse);
        assertThat(currentLesson.getLecturer()).isEqualTo(currentLecturer);
        assertThat(currentLesson.getDate()).isEqualTo(LESSON_DATE_5000001);
        assertThat(currentLesson.getTimeSlot()).isEqualTo(currentTimeSlot);
        assertThat(currentLesson.getStartTime()).isEqualTo(LESSON_START_TIME_5000001);
        assertThat(currentLesson.getEndTime()).isEqualTo(LESSON_END_TIME_5000001);

        mockMvc.perform(put("/api/lessons/update/{id}", LESSON_ID_5000001)
                .content(lessonJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(LESSON_ID_5000001))
                .andExpect(jsonPath("$.courseId").value(COURSE_ID_53))
                .andExpect(jsonPath("$.lecturerId").value(PERSON_ID_50006))
                .andExpect(jsonPath("$.date").value(NEW_LESSON_DATE.toString()))
                .andExpect(jsonPath("$.timeSlotId").value(TIME_SLOT_ID_2))
                .andExpect(jsonPath("$.startTime").value(NEW_LESSON_START_TIME.toString()))
                .andExpect(jsonPath("$.endTime").value(NEW_LESSON_END_TIME.toString()));

        Lesson actualLesson = lessonService.getById(LESSON_ID_5000001);
        Course course = courseService.getById(COURSE_ID_53);
        Lecturer lecturer = lecturerService.getById(PERSON_ID_50006);
        TimeSlot timeSlot = timeSlotService.getById(TIME_SLOT_ID_2);

        assertThat(actualLesson.getCourse()).isEqualTo(course);
        assertThat(actualLesson.getLecturer()).isEqualTo(lecturer);
        assertThat(actualLesson.getDate()).isEqualTo(NEW_LESSON_DATE);
        assertThat(actualLesson.getTimeSlot()).isEqualTo(timeSlot);
        assertThat(actualLesson.getStartTime()).isEqualTo(NEW_LESSON_START_TIME);
        assertThat(actualLesson.getEndTime()).isEqualTo(NEW_LESSON_END_TIME);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateLessonShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String lessonJSON = "{\"id\":" + NONEXISTENT_LESSON_ID + "}";

        mockMvc.perform(put("/api/lessons/update/{id}", NONEXISTENT_LESSON_ID)
                .content(lessonJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteLessonShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/lessons/delete/{id}", LESSON_ID_5000001))
                .andDo(print())
                .andExpect(status().isOk());

        Lesson actualLesson = lessonService.getById(LESSON_ID_5000001);
        assertNull(actualLesson);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteLessonShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(delete("/api/lessons/delete/{id}", NONEXISTENT_LESSON_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetAllLessonsShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/lessons/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(LESSON_ID_5000001))
                .andExpect(jsonPath("$.*", hasSize(5)));
    }
}
