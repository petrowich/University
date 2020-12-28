package ru.petrowich.university.controller.lessons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.LecturerService;
import ru.petrowich.university.service.LessonService;
import ru.petrowich.university.service.TimeSlotService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class LessonControllerTest {
    private AutoCloseable autoCloseable;
    private MockMvc mockMvc;

    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final Integer COURSE_ID_53 = 53;
    private static final Integer COURSE_ID_54 = 54;
    private static final Integer COURSE_ID_55 = 55;
    private static final Integer PERSON_ID_50001 = 50001;
    private static final Integer PERSON_ID_50002 = 50002;
    private static final Integer PERSON_ID_50003 = 50003;
    private static final Integer PERSON_ID_50004 = 50004;
    private static final Integer TIME_SLOT_ID_1 = 1;
    private static final Integer TIME_SLOT_ID_2 = 2;
    private static final LocalTime TIME_SLOT_START_TIME_8 = LocalTime.of(8, 0);
    private static final LocalTime TIME_SLOT_START_TIME_9 = LocalTime.of(9, 0);
    private static final LocalTime TIME_SLOT_END_TIME_8 = LocalTime.of(10, 30);

    @Mock
    LessonService mockLessonService;

    @Mock
    LecturerService mockLecturerService;

    @Mock
    CourseService mockCourseService;

    @Mock
    TimeSlotService mockTimeSlotService;

    @InjectMocks
    LessonController lessonController;

    @BeforeEach
    private void openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(lessonController).build();
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testLessons() throws Exception {
        List<Lesson> expectedLessons = new ArrayList<>();

        String expectedViewName = "lessons/lessons";

        when(mockLessonService.getAll()).thenReturn(expectedLessons);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("allLessons", expectedLessons))
                .andExpect(view().name(expectedViewName));

        verify(mockLessonService, times(1)).getAll();
    }

    @Test
    void testCreate() throws Exception {
        Lesson expectedLesson = new Lesson();

        Lecturer activeLecturer = new Lecturer().setId(PERSON_ID_50001).setActive(true);
        Lecturer inactiveLecturer = new Lecturer().setId(PERSON_ID_50002).setActive(false);

        Course firstCourse = new Course().setId(COURSE_ID_51).setActive(true).setAuthor(activeLecturer).setName("z");
        Course secondCourse = new Course().setId(COURSE_ID_52).setActive(false).setAuthor(activeLecturer);
        Course thirdCourse = new Course().setId(COURSE_ID_53).setActive(true).setAuthor(inactiveLecturer);
        Course fourthCourse = new Course().setId(COURSE_ID_54).setActive(true).setAuthor(new Lecturer());
        Course fifthCourse = new Course().setId(COURSE_ID_55).setActive(true).setAuthor(activeLecturer).setName("a");

        List<Course> courses = asList(firstCourse, secondCourse, thirdCourse, fourthCourse, fifthCourse);
        List<Course> expectedCourses = asList(fifthCourse, firstCourse);

        TimeSlot firstTimeSlot = new TimeSlot().setId(TIME_SLOT_ID_1).setStartTime(TIME_SLOT_START_TIME_9);
        TimeSlot secondTimeSlot = new TimeSlot().setId(TIME_SLOT_ID_2).setStartTime(TIME_SLOT_START_TIME_8);

        List<TimeSlot> expectedTimeSlots = asList(secondTimeSlot, firstTimeSlot);

        String expectedViewName = "lessons/lesson_creator";

        when(mockCourseService.getAll()).thenReturn(courses);
        when(mockTimeSlotService.getAll()).thenReturn(expectedTimeSlots);

        mockMvc.perform(get("/lessons/lesson/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("lesson", expectedLesson))
                .andExpect(model().attribute("timeSlots", expectedTimeSlots))
                .andExpect(model().attribute("courses", expectedCourses))
                .andExpect(view().name(expectedViewName));

        verify(mockCourseService, times(1)).getAll();
        verify(mockTimeSlotService, times(1)).getAll();
    }

    @Test
    void testAdd() throws Exception {
        TimeSlot timeSlot = new TimeSlot().setId(TIME_SLOT_ID_1).setEndTime(TIME_SLOT_START_TIME_8).setEndTime(TIME_SLOT_END_TIME_8);
        Course course = new Course().setId(COURSE_ID_51);

        Lesson expectedLesson = new Lesson().setTimeSlot(timeSlot).setCourse(course);

        String expectedViewName = "lessons/lessons";

        when(mockTimeSlotService.getById(TIME_SLOT_ID_1)).thenReturn(timeSlot);
        when(mockCourseService.getById(COURSE_ID_51)).thenReturn(course);

        mockMvc.perform(post("/lessons/lesson/add").flashAttr("lesson", expectedLesson).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockTimeSlotService, times(1)).getById(TIME_SLOT_ID_1);
        verify(mockCourseService, times(1)).getById(COURSE_ID_51);
        verify(mockLessonService, times(1)).add(expectedLesson);
    }

    @Test
    void testEdit() throws Exception {
        Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50001).setActive(true).setLastName("2");
        Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50002).setActive(false);
        Lecturer thirdLecturer = new Lecturer().setId(PERSON_ID_50003).setActive(true).setFirstName("3");
        Lecturer fourthLecturer = new Lecturer().setId(PERSON_ID_50004).setActive(true).setFirstName("1");

        Course firstCourse = new Course().setId(COURSE_ID_51).setActive(true).setAuthor(firstLecturer).setName("2");
        Course secondCourse = new Course().setId(COURSE_ID_52).setActive(false).setAuthor(secondLecturer);
        Course thirdCourse = new Course().setId(COURSE_ID_53).setActive(true).setAuthor(firstLecturer).setName("1");

        List<Course> courses = asList(firstCourse, secondCourse, thirdCourse);
        when(mockCourseService.getAll()).thenReturn(courses);

        List<Lecturer> lecturers = asList(firstLecturer, secondLecturer, thirdLecturer, fourthLecturer);
        when(mockLecturerService.getAll()).thenReturn(lecturers);

        Lesson lesson = new Lesson().setId(LESSON_ID_5000001).setLecturer(firstLecturer);
        when(mockLessonService.getById(LESSON_ID_5000001)).thenReturn(lesson);

        List<Lecturer> expectedLecturers = asList(fourthLecturer, thirdLecturer);
        List<Course> expectedCourses = asList(thirdCourse, firstCourse);

        String expectedViewName = "lessons/lesson_editor";

        mockMvc.perform(get("/lessons/lesson/edit").param("id", String.valueOf(LESSON_ID_5000001)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("courses", expectedCourses))
                .andExpect(model().attribute("lecturers", expectedLecturers))
                .andExpect(view().name(expectedViewName));

        verify(mockLessonService, times(1)).getById(LESSON_ID_5000001);
    }

    @Test
    void testUpdate() throws Exception {
        Lesson expectedLesson = new Lesson().setId(LESSON_ID_5000001);

        String expectedViewName = "lessons/lessons";

        mockMvc.perform(post("/lessons/lesson/update").flashAttr("lesson", expectedLesson).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockLessonService, times(1)).update(expectedLesson);
    }

    @Test
    void testUpdateShouldReturnBadRequestWhenResultHasErrors() throws Exception {
        String expectedViewName = "lessons/lessons";

        mockMvc.perform(post("/lessons/lesson/update")
                .param("id", "wrong parameter value")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(expectedViewName));
    }

    @Test
    void delete() throws Exception {
        Lesson expectedLesson = new Lesson().setId(LESSON_ID_5000001);

        String expectedViewName = "lessons/lessons";

        mockMvc.perform(post("/lessons/lesson/delete").flashAttr("lesson", expectedLesson).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));

        verify(mockLessonService, times(1)).delete(expectedLesson);
    }

    @Test
    void testDeleteShouldReturnBadRequestWhenResultHasErrors() throws Exception {
        String expectedViewName = "lessons/lessons";

        mockMvc.perform(post("/lessons/lesson/delete")
                .param("id", "wrong parameter value")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(view().name(expectedViewName));
    }
}
