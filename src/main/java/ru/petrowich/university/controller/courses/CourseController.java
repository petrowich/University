package ru.petrowich.university.controller.courses;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final CourseService courseService;
    private final GroupService groupService;

    @Autowired
    public CourseController(CourseService courseService, GroupService groupService) {
        this.courseService = courseService;
        this.groupService = groupService;
    }

    @GetMapping("/course")
    public String course(@RequestParam("courseId") Integer courseId, Model model) {
        LOGGER.info("getting course id={}", courseId);
        Course course = courseService.getById(courseId);
        model.addAttribute("course", course);
        LOGGER.info("course: {} {}", course.getId(), course.getName());

        LOGGER.info("listing groups of course id={}", courseId);
        List<Group> groups = groupService.getByCourseId(courseId);
        model.addAttribute("groups", groups);
        LOGGER.info("number of groups: {}", groups.size());

        return "courses/course";
    }
}
