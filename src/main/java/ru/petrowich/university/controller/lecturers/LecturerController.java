package ru.petrowich.university.controller.lecturers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/lecturers")
public class LecturerController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LecturerService lecturerService;

    @Autowired
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("/lecturer")
    public String course(@RequestParam("lecturerId") Integer lecturerId, Model model) {
        populateLecturer(lecturerId, model);
        return "lecturers/lecturer";
    }

    private void populateLecturer(Integer courseId, Model model) {
        LOGGER.info("getting lecturer id={}", courseId);
        Lecturer lecturer = lecturerService.getById(courseId);
        model.addAttribute("lecturer", lecturer);
        LOGGER.info("lecturer: {} {}", lecturer.getId(), lecturer.getFullName());
    }
}
