package ru.petrowich.university.controller.students;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student")
    public String course(@RequestParam("studentId") Integer studentId, Model model) {
        LOGGER.info("getting student id={}", studentId);
        Student student = studentService.getById(studentId);
        model.addAttribute("student", student);
        LOGGER.info("student: {} {}", student.getId(), student.getFullName());

        return "students/student";
    }
}
