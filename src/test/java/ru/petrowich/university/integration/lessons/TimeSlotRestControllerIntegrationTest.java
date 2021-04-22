package ru.petrowich.university.integration.lessons;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.dto.lessons.LessonDTO;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import javax.transaction.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class TimeSlotRestControllerIntegrationTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";

    private static final Integer TIME_SLOT_ID_1 = 1;
    private static final Integer NEW_TIME_SLOT_ID = 9;
    private static final Integer NONEXISTENT_TIME_SLOT_ID = 0;
    private static final String TIME_SLOT_NAME_1 = "first lesson";
    private static final String NEW_TIME_SLOT_NAME = "new time slot";
    private static final LocalTime TIME_SLOT_START_TIME_1 = LocalTime.of(8, 00);
    private static final LocalTime TIME_SLOT_END_TIME_1 = LocalTime.of(9, 30);
    private static final LocalTime NEW_TIME_SLOT_START_TIME = LocalTime.of(19, 40);
    private static final LocalTime NEW_TIME_SLOT_END_TIME = LocalTime.of(20, 10);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeSlotService timeSlotService;

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetTimeSlotShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/lessons/timeslots/{id}", TIME_SLOT_ID_1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(TIME_SLOT_ID_1))
                .andExpect(jsonPath("$.name").value(TIME_SLOT_NAME_1))
                .andExpect(jsonPath("$.startTime").value(TIME_SLOT_START_TIME_1.toString()))
                .andExpect(jsonPath("$.endTime").value(TIME_SLOT_END_TIME_1.toString()));
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(get("/api/lessons/timeslots/{id}", NONEXISTENT_TIME_SLOT_ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testAddTimeSlotShouldReturnCreated() throws Exception {
        String newTimeSlotJSON = "{\"name\":\"" + NEW_TIME_SLOT_NAME + "\""
                + ",\"startTime\":\"" + NEW_TIME_SLOT_START_TIME + "\""
                + ",\"endTime\":\"" + NEW_TIME_SLOT_END_TIME + "\""
                + "}";

        mockMvc.perform(post("/api/lessons/timeslots/add")
                .content(newTimeSlotJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(NEW_TIME_SLOT_ID));

        TimeSlot actualTimeSlot = timeSlotService.getById(NEW_TIME_SLOT_ID);
        assertNotNull(actualTimeSlot);
        assertThat(actualTimeSlot.getName()).isEqualTo(NEW_TIME_SLOT_NAME);
        assertThat(actualTimeSlot.getStartTime()).isEqualTo(NEW_TIME_SLOT_START_TIME);
        assertThat(actualTimeSlot.getEndTime()).isEqualTo(NEW_TIME_SLOT_END_TIME);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateTimeSlotShouldReturnOK() throws Exception {
        String timeSlotJSON = "{\"name\":\"" + NEW_TIME_SLOT_NAME + "\""
                + ",\"startTime\":\"" + NEW_TIME_SLOT_START_TIME + "\""
                + ",\"endTime\":\"" + NEW_TIME_SLOT_END_TIME + "\""
                + "}";

        TimeSlot currentTimeSlot = timeSlotService.getById(TIME_SLOT_ID_1);

        assertThat(currentTimeSlot.getName()).isEqualTo(TIME_SLOT_NAME_1);
        assertThat(currentTimeSlot.getStartTime()).isEqualTo(TIME_SLOT_START_TIME_1);
        assertThat(currentTimeSlot.getEndTime()).isEqualTo(TIME_SLOT_END_TIME_1);

        mockMvc.perform(put("/api/lessons/timeslots/update/{id}", TIME_SLOT_ID_1)
                .content(timeSlotJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(TIME_SLOT_ID_1))
                .andExpect(jsonPath("$.name").value(NEW_TIME_SLOT_NAME))
                .andExpect(jsonPath("$.startTime").value(NEW_TIME_SLOT_START_TIME.toString()))
                .andExpect(jsonPath("$.endTime").value(NEW_TIME_SLOT_END_TIME.toString()));

        TimeSlot actualTimeSlot = timeSlotService.getById(TIME_SLOT_ID_1);

        assertThat(actualTimeSlot.getName()).isEqualTo(NEW_TIME_SLOT_NAME);
        assertThat(actualTimeSlot.getStartTime()).isEqualTo(NEW_TIME_SLOT_START_TIME);
        assertThat(actualTimeSlot.getEndTime()).isEqualTo(NEW_TIME_SLOT_END_TIME);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testUpdateTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        String timeSlotJSON = "{\"id\":" + NONEXISTENT_TIME_SLOT_ID + "}";

        mockMvc.perform(put("/api/lessons/timeslots/update/{id}", NONEXISTENT_TIME_SLOT_ID)
                .content(timeSlotJSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteTimeSlotShouldReturnOK() throws Exception {
        mockMvc.perform(delete("/api/lessons/timeslots/delete/{id}", TIME_SLOT_ID_1))
                .andDo(print())
                .andExpect(status().isOk());

        TimeSlot actualTimeSlot = timeSlotService.getById(TIME_SLOT_ID_1);
        assertNull(actualTimeSlot);
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteTimeSlotShouldReturnNotFoundWhenNonexistentIdPassed() throws Exception {
        mockMvc.perform(delete("/api/lessons/timeslots/delete/{id}", NONEXISTENT_TIME_SLOT_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testGetAllTimeSlotShouldReturnOK() throws Exception {
        mockMvc.perform(get("/api/lessons/timeslots/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(TIME_SLOT_ID_1))
                .andExpect(jsonPath("$.*", hasSize(8)));
    }
}
