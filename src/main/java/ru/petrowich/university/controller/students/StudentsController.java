package ru.petrowich.university.controller.students;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class StudentsController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentService studentService;

    @Autowired
    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String courses(Model model) {
        LOGGER.info("listing students");
        List<Student> students = studentService.getAll().stream()
                .sorted(Comparator.comparing(Student::isActive).reversed())
                .collect(Collectors.toList());
        model.addAttribute("allStudents", students);
        LOGGER.debug("number of students: {}", students.size());

        return "students/students";
    }
}
