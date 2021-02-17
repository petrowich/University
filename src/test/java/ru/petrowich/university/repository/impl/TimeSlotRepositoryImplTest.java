package ru.petrowich.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.petrowich.university.AppTestConfiguration;
import ru.petrowich.university.University;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.repository.TimeSlotRepository;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = {University.class, AppTestConfiguration.class})
@ActiveProfiles("test")
@Transactional
class TimeSlotRepositoryImplTest {
    private static final String POPULATE_DB_SQL = "classpath:populateDbTest.sql";
    private static final Integer NONEXISTENT_TIME_SLOT_ID = 0;
    private static final String NEW_TIME_SLOT_NAME = "ninth lesson";
    private static final LocalTime NEW_TIME_SLOT_START_TIME = LocalTime.of(21, 40);
    private static final LocalTime NEW_TIME_SLOT_END_TIME = LocalTime.of(23, 20);
    private static final Integer EXISTENT_TIME_SLOT_ID_1 = 1;
    private static final Integer EXISTENT_TIME_SLOT_ID_2 = 2;
    private static final Integer EXISTENT_TIME_SLOT_ID_3 = 3;
    private static final Integer EXISTENT_TIME_SLOT_ID_4 = 4;
    private static final Integer EXISTENT_TIME_SLOT_ID_5 = 5;
    private static final Integer EXISTENT_TIME_SLOT_ID_6 = 6;
    private static final Integer EXISTENT_TIME_SLOT_ID_7 = 7;
    private static final Integer EXISTENT_TIME_SLOT_ID_8 = 8;
    private static final String EXISTENT_TIME_SLOT_NAME_1 = "first lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_2 = "second lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_3 = "third lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_4 = "fourth lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_5 = "fifth lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_6 = "sixth lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_7 = "seventh lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_8 = "eighth lesson";
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_1 = LocalTime.of(8, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_2 = LocalTime.of(9, 40);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_3 = LocalTime.of(11, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_4 = LocalTime.of(13, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_5 = LocalTime.of(15, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_6 = LocalTime.of(16, 40);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_7 = LocalTime.of(18, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_8 = LocalTime.of(20, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_1 = LocalTime.of(9, 30);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_2 = LocalTime.of(11, 10);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_3 = LocalTime.of(12, 50);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_4 = LocalTime.of(14, 50);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_5 = LocalTime.of(16, 30);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_6 = LocalTime.of(18, 10);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_7 = LocalTime.of(19, 50);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_8 = LocalTime.of(21, 30);
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private LessonRepository lessonRepository;
    
    @Test
    void testFindByIdShouldReturnExistentLesson() {
        TimeSlot expected = new TimeSlot()
                .setId(EXISTENT_TIME_SLOT_ID_1)
                .setName(EXISTENT_TIME_SLOT_NAME_1)
                .setStartTime(EXISTENT_TIME_SLOT_START_TIME_1)
                .setEndTime(EXISTENT_TIME_SLOT_END_TIME_1);

        TimeSlot actual = timeSlotRepository.findById(EXISTENT_TIME_SLOT_ID_1).orElse(new TimeSlot());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testFindByIdShouldShouldReturnNullWhenNonexistentIdPassed() {
        Optional<TimeSlot> actual = timeSlotRepository.findById(NONEXISTENT_TIME_SLOT_ID);
        assertFalse(actual.isPresent(), "null is expected when nonexistent timeslot id passed");
    }

    @Test
    void testFindByIdShouldShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> timeSlotRepository.findById(null), "InvalidDataAccessApiUsageException throw is expected");
    }

    @Test
    void testSaveShouldAddNewTimeSlot() {
        TimeSlot expected = new TimeSlot()
                .setName(NEW_TIME_SLOT_NAME)
                .setStartTime(NEW_TIME_SLOT_START_TIME)
                .setEndTime(NEW_TIME_SLOT_END_TIME);

        timeSlotRepository.save(expected);
        assertNotNull(expected.getId(), "add() should set new id to the timeslot, new id cannot be null");

        TimeSlot actual = timeSlotRepository.findById(expected.getId()).orElse(new TimeSlot());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testSaveShouldThrowInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> timeSlotRepository.save(null), "save null should throw InvalidDataAccessApiUsageException");
    }

    @Test
    void testUpdateShouldUpdateExistentTimeSlot() {
        TimeSlot actual = timeSlotRepository.findById(EXISTENT_TIME_SLOT_ID_1).orElse(new TimeSlot());

        actual.setName(NEW_TIME_SLOT_NAME)
                .setStartTime(NEW_TIME_SLOT_START_TIME)
                .setEndTime(NEW_TIME_SLOT_END_TIME);

        timeSlotRepository.save(actual);

        TimeSlot expected = timeSlotRepository.findById(EXISTENT_TIME_SLOT_ID_1).orElse(new TimeSlot());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testUpdateShouldThrowNInvalidDataAccessApiUsageExceptionWhenNullPassed() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> timeSlotRepository.save(null), "update null should throw InvalidDataAccessApiUsageException");
    }

    @Test
    @Sql(POPULATE_DB_SQL)
    void testDeleteShouldDeleteTimeSlotWhenExistentTimeSlotPassed() {
        TimeSlot timeSlot = new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1);
        timeSlotRepository.delete(timeSlot);

        Optional<TimeSlot> actual = timeSlotRepository.findById(EXISTENT_TIME_SLOT_ID_1);
        assertFalse(actual.isPresent(), "null is expected when deleted timeslot id passed");

        Lesson lesson = lessonRepository.findById(EXISTENT_LESSON_ID_5000001).orElse(new Lesson());
        assertNotNull(lesson, "timeslot deletion should not delete its lessons");
    }

    @Test
    void testGetAllShouldReturnAllTimeSlotList() {
        List<TimeSlot> expected = new ArrayList<>();
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1).setName(EXISTENT_TIME_SLOT_NAME_1).setStartTime(EXISTENT_TIME_SLOT_START_TIME_1).setEndTime(EXISTENT_TIME_SLOT_END_TIME_1));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_2).setName(EXISTENT_TIME_SLOT_NAME_2).setStartTime(EXISTENT_TIME_SLOT_START_TIME_2).setEndTime(EXISTENT_TIME_SLOT_END_TIME_2));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_3).setName(EXISTENT_TIME_SLOT_NAME_3).setStartTime(EXISTENT_TIME_SLOT_START_TIME_3).setEndTime(EXISTENT_TIME_SLOT_END_TIME_3));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_4).setName(EXISTENT_TIME_SLOT_NAME_4).setStartTime(EXISTENT_TIME_SLOT_START_TIME_4).setEndTime(EXISTENT_TIME_SLOT_END_TIME_4));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_5).setName(EXISTENT_TIME_SLOT_NAME_5).setStartTime(EXISTENT_TIME_SLOT_START_TIME_5).setEndTime(EXISTENT_TIME_SLOT_END_TIME_5));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_6).setName(EXISTENT_TIME_SLOT_NAME_6).setStartTime(EXISTENT_TIME_SLOT_START_TIME_6).setEndTime(EXISTENT_TIME_SLOT_END_TIME_6));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_7).setName(EXISTENT_TIME_SLOT_NAME_7).setStartTime(EXISTENT_TIME_SLOT_START_TIME_7).setEndTime(EXISTENT_TIME_SLOT_END_TIME_7));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_8).setName(EXISTENT_TIME_SLOT_NAME_8).setStartTime(EXISTENT_TIME_SLOT_START_TIME_8).setEndTime(EXISTENT_TIME_SLOT_END_TIME_8));

        List<TimeSlot> actual = timeSlotRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
