package ru.petrowich.university.controller.students;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/students")
public class GroupController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final GroupService groupService;
    private final CourseService courseService;

    @Autowired
    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @GetMapping("/group")
    public String course(@RequestParam("groupId") Integer groupId, Model model) {
        LOGGER.info("getting group id={}", groupId);
        Group group = groupService.getById(groupId);
        model.addAttribute("group", group);
        LOGGER.info("group: {} {}", group.getId(), group.getName());

        LOGGER.info("listing lecturers of group id={}", groupId);
        List<Course> courses = courseService.getByGroupId(groupId);
        model.addAttribute("courses", courses);
        LOGGER.info("number of courses: {}", courses.size());

        return "students/group";
    }
}
