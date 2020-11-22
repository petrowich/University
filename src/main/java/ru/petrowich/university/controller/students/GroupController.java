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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public String course(@RequestParam("id") Integer groupId, Model model) {
        LOGGER.info("getting group id={}", groupId);
        Group group = groupService.getById(groupId);
        model.addAttribute("group", group);
        LOGGER.debug("group: {} {}", group.getId(), group.getName());

        LOGGER.info("listing lecturers of group id={}", groupId);
        List<Course> courses = courseService.getByGroupId(groupId);
        model.addAttribute("courses", courses);
        LOGGER.debug("number of courses: {}", courses.size());

        return "students/group";
    }

    @GetMapping("/groups")
    public String groups(Model model) {
        LOGGER.info("listing groups");

        Comparator<Group> groupComparator = Comparator.comparing(Group::isActive).reversed()
                .thenComparing(Group::getName);

        List<Group> groups = groupService.getAll().stream()
                .sorted(groupComparator)
                .collect(Collectors.toList());
        model.addAttribute("allGroups", groups);

        LOGGER.debug("number of groups: {}", groups.size());

        return "students/groups";
    }

    @GetMapping("/group/edit")
    public String editGroup(@RequestParam("id") Integer groupId, Model model) {
        LOGGER.info("getting group id={}", groupId);

        Group group = groupService.getById(groupId);
        model.addAttribute("group", group);

        LOGGER.debug("student: {} {}", group.getId(), group.getName());

        return "students/group_editor";
    }

    @PostMapping("/group/update")
    public String update(@RequestParam("id") Integer groupId, Group group, BindingResult result, Model model) {
        LOGGER.info("submit update of student id={}", groupId);

        if (result.hasErrors()) {
            LOGGER.info("group edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            group.setId(groupId);
            return "students/group_editor";
        }

        groupService.update(group);

        return groups(model);
    }

    @GetMapping("/group/new")
    public String createGroup(Model model) {
        LOGGER.debug("creating group");

        model.addAttribute("group", new Group());

        return "students/group_creator";
    }

    @PostMapping("/group/add")
    public String add(Group group, BindingResult result, Model model) {
        LOGGER.info("add new group");

        if (result.hasErrors()) {
            LOGGER.info("group edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "students/group_creator";
        }

        groupService.add(group.setActive(true));

        return groups(model);
    }
}
