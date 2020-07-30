package ru.petrowich.university.controller.courses;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.service.CourseService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class CoursesController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private CourseService courseService;

    @Autowired
    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        LOGGER.info("listing courses");
        List<Course> courses = courseService.getAll().stream()
                .sorted(Comparator.comparing(Course::isActive).reversed())
                .collect(Collectors.toList());
        model.addAttribute("allCourses", courses);
        LOGGER.info("number of courses: {}", courses.size());

        return "courses/courses";
    }
}
