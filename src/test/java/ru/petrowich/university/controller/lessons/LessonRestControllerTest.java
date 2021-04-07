package ru.petrowich.university.controller.lessons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.petrowich.university.dto.lessons.LessonDTO;
import ru.petrowich.university.mapper.lesson.LessonMapper;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.service.LessonService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class LessonRestControllerTest {
    private static final Long NEW_LESSON_ID = 9999999L;
    private static final Long EXISTENT_LESSON_ID_5000001 = 5000001L;
    private static final Long EXISTENT_LESSON_ID_5000002 = 5000002L;
    private static final Long EXISTENT_LESSON_ID_5000003 = 5000003L;
    private static final Long NONEXISTENT_LESSON_ID = 9999999L;
    private static final Integer EXISTENT_COURSE_ID_51 = 51;
    private static final Integer EXISTENT_COURSE_ID_52 = 52;
    private static final Integer EXISTENT_COURSE_ID_53 = 53;
    private static final String EXISTENT_COURSE_NAME_51 = "math";
    private static final String EXISTENT_COURSE_NAME_52 = "biology";
    private static final String EXISTENT_COURSE_NAME_53 = "physics";
    private static final Integer EXISTENT_GROUP_ID_501 = 501;
    private static final Integer EXISTENT_GROUP_ID_502 = 502;
    private static final Integer EXISTENT_GROUP_ID_503 = 503;
    private static final String EXISTENT_GROUP_NAME_501 = "AA-01";
    private static final String EXISTENT_GROUP_NAME_502 = "BB-02";
    private static final String EXISTENT_GROUP_NAME_503 = "CC-03";
    private static final Integer EXISTENT_PERSON_ID_50001 = 50001;
    private static final Integer EXISTENT_PERSON_ID_50002 = 50002;
    private static final Integer EXISTENT_PERSON_ID_50003 = 50003;
    private static final Integer EXISTENT_PERSON_ID_50004 = 50004;
    private static final Integer EXISTENT_PERSON_ID_50005 = 50005;
    private static final Integer EXISTENT_PERSON_ID_50006 = 50006;
    private static final String EXISTENT_PERSON_FIRST_NAME_50005 = "Отряд";
    private static final String EXISTENT_PERSON_FIRST_NAME_50006 = "Ушат";
    private static final String EXISTENT_PERSON_LAST_NAME_50005 = "Ковбоев";
    private static final String EXISTENT_PERSON_LAST_NAME_50006 = "Помоев";
    private static final Integer TIME_SLOT_ID_1 = 1;
    private static final Integer TIME_SLOT_ID_2 = 2;
    private static final Integer TIME_SLOT_ID_3 = 3;
    private static final LocalDate EXISTENT_LESSON_DATE_5000001 = LocalDate.of(2020, 6, 1);
    private static final LocalDate EXISTENT_LESSON_DATE_5000002 = LocalDate.of(2020, 6, 1);
    private static final LocalDate EXISTENT_LESSON_DATE_5000003 = LocalDate.of(2020, 6, 1);
    private static final LocalDate ANOTHER_LESSON_DATE = LocalDate.of(2020, 7, 1);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000001 = LocalTime.of(8, 0);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000002 = LocalTime.of(9, 40);
    private static final LocalTime EXISTENT_LESSON_START_TIME_5000003 = LocalTime.of(11, 20);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000001 = LocalTime.of(9, 30);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000002 = LocalTime.of(11, 10);
    private static final LocalTime EXISTENT_LESSON_END_TIME_5000003 = LocalTime.of(12, 50);

    private AutoCloseable autoCloseable;
    private final ModelMapper modelMapper = new ModelMapper();
    private final LessonMapper lessonMapper = new LessonMapper(modelMapper);

    private final Lecturer lecturer1 = new Lecturer().setId(EXISTENT_PERSON_ID_50005)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50005).setLastName(EXISTENT_PERSON_LAST_NAME_50005)
            .setActive(true);

    private final Lecturer lecturer2 = new Lecturer().setId(EXISTENT_PERSON_ID_50006)
            .setFirstName(EXISTENT_PERSON_FIRST_NAME_50006).setLastName(EXISTENT_PERSON_LAST_NAME_50006)
            .setActive(true);

    private final Student student1 = new Student().setId(EXISTENT_PERSON_ID_50001).setActive(true);
    private final Student student2 = new Student().setId(EXISTENT_PERSON_ID_50002).setActive(true);
    private final Student student3 = new Student().setId(EXISTENT_PERSON_ID_50003).setActive(true);
    private final Student student4 = new Student().setId(EXISTENT_PERSON_ID_50004).setActive(true);

    private final List<Student> studentList1 = new ArrayList<>(asList(student1, student2, student3));
    private final List<Student> studentList2 = new ArrayList<>(asList(student3, student4));

    private final Group group1 = new Group().setId(EXISTENT_GROUP_ID_501).setName(EXISTENT_GROUP_NAME_501).setStudents(studentList1).setActive(true);
    private final Group group2 = new Group().setId(EXISTENT_GROUP_ID_502).setName(EXISTENT_GROUP_NAME_502).setStudents(studentList2).setActive(true);
    private final Group group3 = new Group().setId(EXISTENT_GROUP_ID_503).setName(EXISTENT_GROUP_NAME_503).setActive(true);

    private final List<Group> groupList1 = new ArrayList<>(asList(group1, group2));
    private final List<Group> groupList2 = new ArrayList<>(singletonList(group3));

    private final Course course1 = new Course().setId(EXISTENT_COURSE_ID_51).setName(EXISTENT_COURSE_NAME_51).setGroups(groupList1).setActive(true);
    private final Course course2 = new Course().setId(EXISTENT_COURSE_ID_52).setName(EXISTENT_COURSE_NAME_52).setGroups(groupList2).setActive(true);
    private final Course course3 = new Course().setId(EXISTENT_COURSE_ID_53).setName(EXISTENT_COURSE_NAME_53).setActive(false);

    private final TimeSlot timeSlot1 = new TimeSlot().setId(TIME_SLOT_ID_1).setStartTime(EXISTENT_LESSON_START_TIME_5000001).setEndTime(EXISTENT_LESSON_END_TIME_5000001);
    private final TimeSlot timeSlot2 = new TimeSlot().setId(TIME_SLOT_ID_2).setStartTime(EXISTENT_LESSON_START_TIME_5000002).setEndTime(EXISTENT_LESSON_END_TIME_5000002);
    private final TimeSlot timeSlot3 = new TimeSlot().setId(TIME_SLOT_ID_3).setStartTime(EXISTENT_LESSON_START_TIME_5000003).setEndTime(EXISTENT_LESSON_END_TIME_5000003);

    private final Lesson newLesson = new Lesson().setCourse(course1).setLecturer(lecturer1).setTimeSlot(timeSlot1)
            .setDate(EXISTENT_LESSON_DATE_5000001).setStartTime(EXISTENT_LESSON_START_TIME_5000001).setEndTime(EXISTENT_LESSON_END_TIME_5000002);

    private final Lesson lesson1 = new Lesson().setId(EXISTENT_LESSON_ID_5000001).setCourse(course1).setLecturer(lecturer1).setTimeSlot(timeSlot1)
            .setDate(EXISTENT_LESSON_DATE_5000001).setStartTime(EXISTENT_LESSON_START_TIME_5000001).setEndTime(EXISTENT_LESSON_END_TIME_5000002);

    private final Lesson lesson2 = new Lesson().setId(EXISTENT_LESSON_ID_5000002).setCourse(course2).setLecturer(lecturer2).setTimeSlot(timeSlot2)
            .setDate(EXISTENT_LESSON_DATE_5000002).setStartTime(EXISTENT_LESSON_START_TIME_5000002).setEndTime(EXISTENT_LESSON_END_TIME_5000002);

    private final Lesson lesson3 = new Lesson().setId(EXISTENT_LESSON_ID_5000003).setCourse(course3).setLecturer(lecturer2).setTimeSlot(timeSlot3)
            .setDate(EXISTENT_LESSON_DATE_5000003).setStartTime(EXISTENT_LESSON_START_TIME_5000003).setEndTime(EXISTENT_LESSON_END_TIME_5000003);

    @Mock
    LessonService mockLessonService;

    @Mock
    LessonMapper mockLessonMapper;

    @InjectMocks
    LessonRestController lessonRestController;

    @BeforeEach
    private void beforeEach() {
        autoCloseable = openMocks(this);
    }

    @AfterEach
    public void afterEach() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetLessonShouldReturnOK() {
        LessonDTO lessonDTO = lessonMapper.toDto(lesson1);

        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson1);
        when(mockLessonMapper.toDto(lesson1)).thenReturn(lessonDTO);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.getLesson(EXISTENT_LESSON_ID_5000001);

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonMapper, times(1)).toDto(lesson1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(lessonDTO);
    }

    @Test
    void testGetLessonShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<LessonDTO> responseEntity = lessonRestController.getLesson(null);

        verify(mockLessonService, times(0)).getById(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetLessonShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.getLesson(NONEXISTENT_LESSON_ID);

        verify(mockLessonService, times(1)).getById(NONEXISTENT_LESSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testAddLessonShouldReturnCreated() {
        LessonDTO newLessonDTO = lessonMapper.toDto(newLesson);
        Lesson expectedLesson = lessonMapper.toEntity(newLessonDTO).setId(NEW_LESSON_ID);

        when(mockLessonMapper.toEntity(newLessonDTO)).thenReturn(newLesson);
        when(mockLessonService.add(newLesson)).thenReturn(expectedLesson);

        LessonDTO expectedLessonDTO = lessonMapper.toDto(expectedLesson);
        when(mockLessonMapper.toDto(expectedLesson)).thenReturn(expectedLessonDTO);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.addLesson(newLessonDTO);

        verify(mockLessonMapper, times(1)).toEntity(newLessonDTO);
        verify(mockLessonService, times(1)).add(newLesson);
        verify(mockLessonMapper, times(1)).toDto(expectedLesson);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLessonDTO);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    void testUpdateLessonShouldReturnOK() {
        LessonDTO lessonDTO = lessonMapper.toDto(lesson1);
        Lesson expectedLesson = lessonMapper.toEntity(lessonDTO).setDate(ANOTHER_LESSON_DATE);

        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson1);
        when(mockLessonMapper.toEntity(lessonDTO)).thenReturn(lesson1);
        when(mockLessonService.update(lesson1)).thenReturn(expectedLesson);

        LessonDTO expectedLessonDTO = lessonMapper.toDto(expectedLesson);
        when(mockLessonMapper.toDto(expectedLesson)).thenReturn(expectedLessonDTO);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.updateLesson(lessonDTO, EXISTENT_LESSON_ID_5000001);

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonMapper, times(1)).toEntity(lessonDTO);
        verify(mockLessonService, times(1)).update(lesson1);
        verify(mockLessonMapper, times(1)).toDto(expectedLesson);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isEqualTo(expectedLessonDTO);
    }

    @Test
    void testUpdateLessonShouldReturnBadRequestWhenNullIdPassed() {
        LessonDTO lessonDTO = lessonMapper.toDto(lesson1);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.updateLesson(lessonDTO, null);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testUpdateLessonShouldReturnNotFoundWhenNonexistentIdPassed() {
        LessonDTO lessonDTO = lessonMapper.toDto(lesson1);

        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.updateLesson(lessonDTO, NONEXISTENT_LESSON_ID);

        verify(mockLessonService, times(1)).getById(NONEXISTENT_LESSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testDeleteLessonShouldReturnOK() {
        when(mockLessonService.getById(EXISTENT_LESSON_ID_5000001)).thenReturn(lesson1);

        ResponseEntity<LessonDTO> responseEntity = lessonRestController.deleteLesson(EXISTENT_LESSON_ID_5000001);

        verify(mockLessonService, times(1)).getById(EXISTENT_LESSON_ID_5000001);
        verify(mockLessonService, times(1)).delete(lesson1);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testDeleteLessonShouldReturnBadRequestWhenNullIdPassed() {
        ResponseEntity<LessonDTO> responseEntity = lessonRestController.deleteLesson(null);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testDeleteLessonShouldReturnNotFoundWhenNonexistentIdPassed() {
        when(mockLessonService.getById(NONEXISTENT_LESSON_ID)).thenReturn(null);
        ResponseEntity<LessonDTO> responseEntity = lessonRestController.deleteLesson(NONEXISTENT_LESSON_ID);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testGetAllLessonsShouldReturnOK() {
        LessonDTO lessonDTO1 = lessonMapper.toDto(lesson1);
        LessonDTO lessonDTO2 = lessonMapper.toDto(lesson2);
        LessonDTO lessonDTO3 = lessonMapper.toDto(lesson3);

        List<Lesson> lessons = new ArrayList<>(asList(lesson1, lesson2, lesson3));

        when(mockLessonService.getAll()).thenReturn(lessons);
        when(mockLessonMapper.toDto(lesson1)).thenReturn(lessonDTO1);
        when(mockLessonMapper.toDto(lesson2)).thenReturn(lessonDTO2);
        when(mockLessonMapper.toDto(lesson3)).thenReturn(lessonDTO3);

        ResponseEntity<List<LessonDTO>> responseEntity = lessonRestController.getAllLessons();

        verify(mockLessonService, times(1)).getAll();
        verify(mockLessonMapper, times(1)).toDto(lesson1);
        verify(mockLessonMapper, times(1)).toDto(lesson2);
        verify(mockLessonMapper, times(1)).toDto(lesson3);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

        List<LessonDTO> expectedLessonDTOList = new ArrayList<>(asList(lessonDTO1, lessonDTO2, lessonDTO3));
        assertThat(expectedLessonDTOList).containsExactlyInAnyOrderElementsOf(responseEntity.getBody());
    }
}
