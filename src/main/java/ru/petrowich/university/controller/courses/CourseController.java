package ru.petrowich.university.controller.courses;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.LecturerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private static final String ATTRIBUTE_COURSE = "course";
    private static final String ATTRIBUTE_COURSE_GROUPS = "courseGroups";
    private static final String ATTRIBUTE_REST_GROUPS = "restGroups";
    private static final String ATTRIBUTE_ALL_COURSES = "allCourses";
    private static final String ERROR_MSG_FORM_CONTAINS_ERRORS = "course form contains {} errors";

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
        model.addAttribute(ATTRIBUTE_ALL_COURSES, courses);
        LOGGER.debug("number of courses: {}", courses.size());

        return "courses/courses";
    }

    @GetMapping("/course")
    public String course(@RequestParam("id") Integer courseId, Model model) {
        LOGGER.info("getting course for view id={}", courseId);

        Course course = courseService.getById(courseId);
        model.addAttribute(ATTRIBUTE_COURSE, course);
        LOGGER.debug("received course: {} {}", course.getId(), course.getName());

        LOGGER.debug("filling groups of course id={}", courseId);
        List<Group> courseGroups = course.getGroups().stream()
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());

        model.addAttribute(ATTRIBUTE_COURSE_GROUPS, courseGroups);
        LOGGER.debug("received number of course groups: {}", courseGroups.size());

        LOGGER.debug("filling rest groups id={}", courseId);
        List<Group> restGroups = groupService.getAll().stream()
                .filter(Group::isActive)
                .filter(group -> !courseGroups.contains(group))
                .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_REST_GROUPS, restGroups);
        LOGGER.debug("received number of rest groups: {}", restGroups.size());

        return "courses/course";
    }

    @GetMapping("/course/edit")
    public String edit(@RequestParam("id") Integer courseId, Model model) {
        LOGGER.info("getting course for update by id={}", courseId);

        Course course = courseService.getById(courseId);
        model.addAttribute(ATTRIBUTE_COURSE, course);

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
    public String update(Course course, BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse) {
        LOGGER.info("submitting the changes of course id={}", course.getId());

        if (bindingResult.hasErrors()) {
            LOGGER.debug(ERROR_MSG_FORM_CONTAINS_ERRORS, bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return courses(model);
        }

        List<Group> groups = courseService.getById(course.getId()).getGroups();
        course.setGroups(groups);

        course.setAuthor(getActualAuthor(course.getAuthor()));

        courseService.update(course);

        return "redirect:/courses/";
    }

    @GetMapping("/course/new")
    public String create(Model model) {
        LOGGER.info("creating new course");

        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .filter(Lecturer::isActive)
                .sorted(Comparator.comparing(Lecturer::getFullName))
                .collect(Collectors.toList());

        model.addAttribute(ATTRIBUTE_COURSE, new Course());
        model.addAttribute("lecturers", lecturers);

        return "courses/course_creator";
    }

    @PostMapping("/course/add")
    public String add(Course course, BindingResult bindingResult) {
        LOGGER.info("adding new course");

        if (bindingResult.hasErrors()) {
            LOGGER.debug("course edit form contains {} errors", bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "courses/course_creator";
        }

        course.setAuthor(getActualAuthor(course.getAuthor()));

        courseService.add(course.setActive(true));

        return "redirect:/courses/";
    }

    @PostMapping("/course/assign-group")
    public String assignGroup(HttpServletRequest httpServletRequest) {
        LOGGER.info("assigning group to course");

        Integer courseId = Integer.valueOf(httpServletRequest.getParameter("courseId"));
        Course course = new Course().setId(courseId);

        Integer groupId = Integer.valueOf(httpServletRequest.getParameter("groupId"));
        Group group = new Group().setId(groupId);

        courseService.assignGroupToCourse(group, course);

        return String.format("redirect:/courses/course?id=%d",courseId);
    }

    @PostMapping("/course/remove-group")
    public String removeGroup(HttpServletRequest httpServletRequest) {
        LOGGER.info("removing group from course");

        Integer courseId = Integer.valueOf(httpServletRequest.getParameter("courseId"));
        Course course = new Course().setId(courseId);

        Integer groupId = Integer.valueOf(httpServletRequest.getParameter("groupId"));
        Group group = new Group().setId(groupId);

        courseService.removeGroupFromCourse(group, course);

        return String.format("redirect:/courses/course?id=%d",courseId);
    }

    private Lecturer getActualAuthor(Lecturer author) {
        if (author != null && author.getId() != null) {
            return lecturerService.getById(author.getId());
        }
        return null;
    }
}
