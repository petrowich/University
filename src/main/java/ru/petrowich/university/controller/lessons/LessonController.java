package ru.petrowich.university.controller.lessons;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.LecturerService;
import ru.petrowich.university.service.LessonService;
import ru.petrowich.university.service.TimeSlotService;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private static final String ATTRIBUTE_LESSON = "lesson";
    private static final String ATTRIBUTE_TIMESLOTS = "timeSlots";
    private static final String ATTRIBUTE_COURSES = "courses";
    private static final String ATTRIBUTE_LECTURERS = "lecturers";
    private static final String ATTRIBUTE_ALL_LESSONS = "allLessons";
    private static final String ERROR_MSG_FORM_CONTAINS_ERRORS = "lesson form contains {} errors";

    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LessonService lessonService;
    private final TimeSlotService timeSlotService;
    private final CourseService courseService;
    private final LecturerService lecturerService;

    @Autowired
    public LessonController(LessonService lessonService, TimeSlotService timeSlotService, CourseService courseService, LecturerService lecturerService) {
        this.lessonService = lessonService;
        this.timeSlotService = timeSlotService;
        this.courseService = courseService;
        this.lecturerService = lecturerService;
    }

    @GetMapping("")
    public String lessons(Model model) {
        LOGGER.info("listing lessons");

        List<Lesson> lessons = lessonService.getAll().stream()
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_ALL_LESSONS, lessons);

        LOGGER.debug("number of Lessons: {}", lessons.size());

        return "lessons/lessons";
    }

    @GetMapping("/lesson/new")
    public String create(Model model) {
        LOGGER.info("creating lesson");

        List<Course> courses = courseService.getAll().stream()
                .filter(Course::isActive)
                .filter(course -> (course.getAuthor().getId() != null))
                .filter(course -> (course.getAuthor().isActive()))
                .sorted(Comparator.comparing(Course::getName))
                .collect(Collectors.toList());

        List<TimeSlot> timeSlots = timeSlotService.getAll().stream()
                .sorted(Comparator.comparing(TimeSlot::getStartTime))
                .collect(Collectors.toList());

        model.addAttribute(ATTRIBUTE_LESSON, new Lesson());
        model.addAttribute(ATTRIBUTE_TIMESLOTS, timeSlots);
        model.addAttribute(ATTRIBUTE_COURSES, courses);

        return "lessons/lesson_creator";
    }

    @PostMapping("/lesson/add")
    public String add(Lesson lesson, BindingResult result, Model model) {
        LOGGER.info("adding new lesson");

        if (result.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "lessons/lesson_creator";
        }

        TimeSlot timeSlot = timeSlotService.getById(lesson.getTimeSlot().getId());
        Course course = courseService.getById(lesson.getCourse().getId());

        lesson.setStartTime(timeSlot.getStartTime());
        lesson.setEndTime(timeSlot.getEndTime());
        lesson.setLecturer(course.getAuthor());

        lessonService.add(lesson);

        return lessons(model);
    }

    @GetMapping("/lesson/edit")
    public String edit(@RequestParam("id") Long lessonId, Model model) {
        LOGGER.info("getting lesson id={}", lessonId);

        Lesson lesson = lessonService.getById(lessonId);
        model.addAttribute(ATTRIBUTE_LESSON, lesson);

        List<Course> courses = courseService.getAll().stream()
                .filter(Course::isActive)
                .filter(course -> !course.getId().equals(lesson.getCourse().getId()))
                .sorted(Comparator.comparing(Course::getName))
                .collect(Collectors.toList());

        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .filter(Lecturer::isActive)
                .filter(lecturer -> !lecturer.getId().equals(lesson.getLecturer().getId()))
                .sorted(Comparator.comparing(Lecturer::getFirstName))
                .collect(Collectors.toList());

        model.addAttribute(ATTRIBUTE_COURSES, courses);
        model.addAttribute(ATTRIBUTE_LECTURERS, lecturers);

        LOGGER.info("opening lesson editor: {} {}", lesson.getId(), lesson);

        return "lessons/lesson_editor";
    }

    @PostMapping("/lesson/update")
    public String update(Lesson lesson, BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse) {
        LOGGER.info("submitting the changes of lesson id={}", lesson.getId());

        if (bindingResult.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return lessons(model);
        }

        if (lesson.getLecturer() != null && lesson.getLecturer().getId() == null) {
            lesson.setLecturer(null);
        }

        if (lesson.getTimeSlot() != null && lesson.getTimeSlot().getId() == null) {
            lesson.setTimeSlot(null);
        }

        lessonService.update(lesson);

        return lessons(model);
    }

    @PostMapping("/lesson/delete")
    public String delete(Lesson lesson, BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse) {
        LOGGER.info("deleting lesson id={}", lesson.getId());

        if (bindingResult.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return lessons(model);
        }

        lessonService.delete(lesson);

        return lessons(model);
    }
}
