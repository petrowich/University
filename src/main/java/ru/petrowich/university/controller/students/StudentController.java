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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/students")
public class StudentController {
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

        List<Student> students = studentService.getAll().stream()
                .sorted(studentComparator)
                .collect(Collectors.toList());
        model.addAttribute("allStudents", students);

        LOGGER.debug("number of students: {}", students.size());

        return "students/students";
    }

    @GetMapping("/student")
    public String student(@RequestParam("id") Integer studentId, Model model) {
        LOGGER.info("getting student id={}", studentId);
        Student student = studentService.getById(studentId);
        model.addAttribute("student", student);
        LOGGER.debug("student: {} {}", student.getId(), student.getFullName());

        return "students/student";
    }

    @GetMapping("/student/edit")
    public String editStudent(@RequestParam("id") Integer studentId, Model model) {
        LOGGER.info("getting student id={}", studentId);

        Student student = studentService.getById(studentId);
        model.addAttribute("student", student);

        List<Group> groups = groupService.getAll().stream()
                .filter(Group::isActive)
                .filter(group -> !group.equals(student.getGroup()))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());

        if (student.getGroup().getId()!=null) {
            groups.add(new Group());
        }

        model.addAttribute("groups", groups);

        LOGGER.debug("student: {} {}", student.getId(), student.getFullName());

        return "students/student_editor";
    }

    @PostMapping("/student/update")
    public String update(@RequestParam("id") Integer studentId, Student student, BindingResult result, Model model) {
        LOGGER.info("submit update of student id={}", studentId);

        if (result.hasErrors()) {
            LOGGER.info("student edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            student.setId(studentId);
            return "students/student_editor";
        }

        studentService.update(student);

        return students(model);
    }

    @GetMapping("/student/new")
    public String editStudent(Model model) {
        LOGGER.info("creating student");

        model.addAttribute("student", new Student());

        List<Group> groups = groupService.getAll().stream()
                .filter(Group::isActive)
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());

        model.addAttribute("groups", groups);

        return "students/student_creator";
    }

    @PostMapping("/student/add")
    public String add(Student student, BindingResult result, Model model) {
        LOGGER.info("add new student");

        if (result.hasErrors()) {
            LOGGER.info("group edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "students/student_creator";
        }

        studentService.add(student.setActive(true));

        return students(model);
    }
}
