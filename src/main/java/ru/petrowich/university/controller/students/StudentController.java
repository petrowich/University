package ru.petrowich.university.controller.students;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/students")
public class StudentController {
    private static final String ATTRIBUTE_STUDENT = "student";
    private static final String ATTRIBUTE_GROUPS = "groups";
    private static final String ATTRIBUTE_ALL_STUDENTS = "allStudents";
    private static final String ERROR_MSG_FORM_CONTAINS_ERRORS = "student form contains {} errors";

    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentService studentService;
    private final GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("")
    public String students(Model model) {
        LOGGER.info("listing students");

        Comparator<Student> studentComparator = Comparator.comparing(Student::isActive).reversed()
                .thenComparing(Student::getFullName);

        List<Student> students = studentService.getAll();

        List<Student> students1 =
                students.stream()
                        .sorted(studentComparator)
                        .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_ALL_STUDENTS, students1);

        LOGGER.debug("number of students: {}", students.size());

        return "students/students";
    }

    @GetMapping("/student")
    public String student(@RequestParam("id") Integer studentId, Model model) {
        LOGGER.info("getting student id={}", studentId);

        Student student = studentService.getById(studentId);
        model.addAttribute(ATTRIBUTE_STUDENT, student);

        LOGGER.debug("student: {} {}", student.getId(), student.getFullName());

        return "students/student";
    }

    @GetMapping("/student/edit")
    public String editStudent(@RequestParam("id") Integer studentId, Model model) {
        LOGGER.info("getting student id={}", studentId);

        Student student = studentService.getById(studentId);
        model.addAttribute(ATTRIBUTE_STUDENT, student);

        List<Group> groups = groupService.getAll().stream()
                .filter(Group::isActive)
                .filter(group -> !group.equals(student.getGroup()))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());

        if (student.getGroup().getId() != null) {
            groups.add(new Group());
        }

        model.addAttribute(ATTRIBUTE_GROUPS, groups);

        LOGGER.debug("student: {} {}", student.getId(), student.getFullName());

        return "students/student_editor";
    }

    @PostMapping("/student/update")
    public String update(Student student, BindingResult result, Model model, HttpServletResponse httpServletResponse) {
        LOGGER.info("submitting the changes of student id={}", student.getId());

        if (result.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return students(model);
        }

        if (student.getGroup() != null && student.getGroup().getId() == null) {
            student.setGroup(null);
        }

        studentService.update(student);

        return students(model);
    }

    @GetMapping("/student/new")
    public String edit(Model model) {
        LOGGER.info("creating student");

        model.addAttribute(ATTRIBUTE_STUDENT, new Student());

        List<Group> groups = groupService.getAll().stream()
                .filter(Group::isActive)
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());

        model.addAttribute(ATTRIBUTE_GROUPS, groups);

        return "students/student_creator";
    }

    @PostMapping("/student/add")
    public String add(Student student, BindingResult result, Model model) {
        LOGGER.info("adding new student");

        if (result.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "students/student_creator";
        }

        studentService.add(student.setActive(true));

        return students(model);
    }
}
