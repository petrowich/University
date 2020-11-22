package ru.petrowich.university.controller.courses;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.LecturerService;


import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final CourseService courseService;
    private final GroupService groupService;
    private final LecturerService lecturerService;

    @Autowired
    public CourseController(CourseService courseService, GroupService groupService, LecturerService lecturerService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.lecturerService = lecturerService;
    }

    @GetMapping("")
    public String courses(Model model) {
        LOGGER.info("listing courses");
        List<Course> courses = courseService.getAll().stream()
                .sorted(Comparator.comparing(Course::isActive).reversed())
                .collect(Collectors.toList());
        model.addAttribute("allCourses", courses);
        LOGGER.debug("number of courses: {}", courses.size());

        return "courses/courses";
    }

    @GetMapping("/course")
    public String course(@RequestParam("id") Integer courseId, Model model) {
        LOGGER.info("getting course id={}", courseId);
        Course course = courseService.getById(courseId);
        model.addAttribute("course", course);
        LOGGER.debug("course: {} {}", course.getId(), course.getName());

        LOGGER.info("listing groups of course id={}", courseId);
        List<Group> courseGroups = groupService.getByCourseId(courseId);
        model.addAttribute("courseGroups", courseGroups);
        LOGGER.debug("number of course groups: {}", courseGroups.size());

        LOGGER.info("listing rest groups id={}", courseId);
        List<Group> restGroups = groupService.getAll().stream()
                .filter(Group::isActive)
                .filter(group -> !courseGroups.contains(group))
                .collect(Collectors.toList());
        model.addAttribute("restGroups", restGroups);
        LOGGER.debug("number of rest groups: {}", restGroups.size());

        return "courses/course";
    }

    @GetMapping("/course/edit")
    public String editCourse(@RequestParam("id") Integer courseId, Model model) {
        LOGGER.info("getting course id={}", courseId);
        Course course = courseService.getById(courseId);
        model.addAttribute("course", course);

        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .filter(Lecturer::isActive)
                .filter(lecturer -> !lecturer.getId().equals(course.getAuthor().getId()))
                .sorted(Comparator.comparing(Lecturer::getFullName))
                .collect(Collectors.toList());

        if (course.getAuthor().getId() != null) {
            lecturers.add(new Lecturer());
        }

        model.addAttribute("lecturers", lecturers);

        LOGGER.debug("opening course editor: {} {}", course.getId(), course.getName());
        return "courses/course_editor";
    }

    @PostMapping("/course/update")
    public String update(Course course, BindingResult result, Model model) {
        LOGGER.info("submit update of course id={}", course.getId());

        if (result.hasErrors()) {
            LOGGER.info("course edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            course.setId(course.getId());
            return "courses/course_editor";
        }

        courseService.update(course);

        return courses(model);
    }

    @GetMapping("/course/new")
    public String newCourse(Model model) {
        LOGGER.info("creating course");

        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .filter(Lecturer::isActive)
                .sorted(Comparator.comparing(Lecturer::getFullName))
                .collect(Collectors.toList());

        model.addAttribute("course", new Course());
        model.addAttribute("lecturers", lecturers);

        return "courses/course_creator";
    }

    @PostMapping("/course/add")
    public String add(Course course, BindingResult result, Model model) {
        LOGGER.info("add new course");

        if (result.hasErrors()) {
            LOGGER.info("course edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "courses/course_creator";
        }

        courseService.add(course.setActive(true));

        return courses(model);
    }

    @PostMapping("/course/assign-group")
    public String assignGroup(Course course, BindingResult result, HttpServletRequest httpServletRequest, Model model) {
        LOGGER.info("assign group to course");

        Integer courseId = Integer.valueOf(httpServletRequest.getParameter("courseId"));
        course.setId(courseId);

        Integer groupId = Integer.valueOf(httpServletRequest.getParameter("groupId"));
        Group group = groupService.getById(groupId);

        courseService.assignGroupToCourse(group, course);

        return course(courseId, model);
    }

    @PostMapping("/course/remove-group")
    public String removeGroup(Course course, BindingResult result, HttpServletRequest httpServletRequest, Model model) {
        LOGGER.info("remove group from course");

        Integer courseId = Integer.valueOf(httpServletRequest.getParameter("courseId"));
        course.setId(courseId);

        Integer groupId = Integer.valueOf(httpServletRequest.getParameter("groupId"));
        Group group = groupService.getById(groupId);

        courseService.removeGroupFromCourse(group, course);

        return course(courseId, model);
    }
}
