package ru.petrowich.university.controller.students;

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
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/students")
public class GroupController {
    private static final String ATTRIBUTE_GROUP = "group";
    private static final String ATTRIBUTE_COURSES = "courses";
    private static final String ATTRIBUTE_ALL_GROUPS = "allGroups";
    private static final String ERROR_MSG_FORM_CONTAINS_ERRORS = "student form contains {} errors";

    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final GroupService groupService;
    private final CourseService courseService;

    @Autowired
    public GroupController(GroupService groupService, CourseService courseService) {
        this.groupService = groupService;
        this.courseService = courseService;
    }

    @GetMapping("/groups")
    public String groups(Model model) {
        LOGGER.info("listing groups");

        Comparator<Group> groupComparator = Comparator.comparing(Group::isActive).reversed()
                .thenComparing(Group::getName);

        List<Group> groups = groupService.getAll().stream()
                .sorted(groupComparator)
                .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_ALL_GROUPS, groups);

        LOGGER.debug("number of groups: {}", groups.size());

        return "students/groups";
    }

    @GetMapping("/group")
    public String group(@RequestParam("id") Integer groupId, Model model) {
        LOGGER.info("getting group id={}", groupId);
        Group group = groupService.getById(groupId);
        model.addAttribute(ATTRIBUTE_GROUP, group);
        LOGGER.debug("group: {} {}", group.getId(), group.getName());

        LOGGER.info("listing lecturers of group id={}", groupId);
        List<Course> courses = courseService.getByGroupId(groupId).stream()
                .sorted(Comparator.comparing(Course::getName))
                .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_COURSES, courses);
        LOGGER.debug("number of courses: {}", courses.size());

        return "students/group";
    }

    @GetMapping("/group/edit")
    public String edit(@RequestParam("id") Integer groupId, Model model) {
        LOGGER.info("getting group id={}", groupId);

        Group group = groupService.getById(groupId);
        model.addAttribute(ATTRIBUTE_GROUP, group);

        LOGGER.debug("student: {} {}", group.getId(), group.getName());

        return "students/group_editor";
    }

    @PostMapping("/group/update")
    public String update(Group group, BindingResult result, Model model, HttpServletResponse httpServletResponse) {
        LOGGER.info("submit update of student id={}", group.getId());

        if (result.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return groups(model);
        }

        groupService.update(group);

        return groups(model);
    }

    @GetMapping("/group/new")
    public String create(Model model) {
        LOGGER.debug("creating group");

        model.addAttribute(ATTRIBUTE_GROUP, new Group());

        return "students/group_creator";
    }

    @PostMapping("/group/add")
    public String add(Group group, BindingResult result, Model model) {
        LOGGER.info("add new group");

        if (result.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "students/group_creator";
        }

        groupService.add(group.setActive(true));

        return groups(model);
    }
}
