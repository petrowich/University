package ru.petrowich.university.controller.lecturers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class LecturersController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LecturerService lecturerService;

    @Autowired
    public LecturersController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("/lecturers")
    public String courses(Model model) {
        LOGGER.info("listing lecturers");
        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .sorted(Comparator.comparing(Lecturer::isActive).reversed())
                .collect(Collectors.toList());
        model.addAttribute("allLecturers", lecturers);
        LOGGER.debug("number of lecturers: {}", lecturers.size());

        return "lecturers/lecturers";
    }
}
