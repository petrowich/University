package ru.petrowich.university.controller.lecturers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("")
    public String lecturers(Model model) {
        LOGGER.info("listing lecturers");
        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .sorted(Comparator.comparing(Lecturer::isActive).reversed())
                .collect(Collectors.toList());
        model.addAttribute("allLecturers", lecturers);
        LOGGER.debug("number of lecturers: {}", lecturers.size());

        return "lecturers/lecturers";
    }

    @GetMapping("/lecturer")
    public String lecturer(@RequestParam("id") Integer lecturerId, Model model) {
        LOGGER.info("getting lecturer id={}", lecturerId);
        Lecturer lecturer = lecturerService.getById(lecturerId);
        model.addAttribute("lecturer", lecturer);
        LOGGER.debug("lecturer: {} {}", lecturer.getId(), lecturer.getFullName());

        return "lecturers/lecturer";
    }

    @GetMapping("/lecturer/edit")
    public String editlecturer(@RequestParam("id") Integer lecturerId, Model model) {
        LOGGER.info("getting lecturer id={}", lecturerId);

        Lecturer lecturer = lecturerService.getById(lecturerId);
        model.addAttribute("lecturer", lecturer);

        LOGGER.debug("lecturer: {} {}", lecturer.getId(), lecturer.getFullName());

        return "lecturers/lecturer_editor";
    }

    @PostMapping("/lecturer/update")
    public String update(@RequestParam("id") Integer lecturerId, Lecturer lecturer, BindingResult result, Model model) {
        LOGGER.info("submit update of lecturer id={}", lecturerId);

        if (result.hasErrors()) {
            LOGGER.info("lecturer edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            lecturer.setId(lecturerId);
            return "lecturers/lecturer_editor";
        }

        lecturerService.update(lecturer);

        return lecturers(model);
    }

    @GetMapping("/lecturer/new")
    public String editLecturer(Model model) {
        LOGGER.info("creating lecturer");

        model.addAttribute("lecturer", new Lecturer());

        return "lecturers/lecturer_creator";
    }

    @PostMapping("/lecturer/add")
    public String add(Lecturer lecturer, BindingResult result, Model model) {
        LOGGER.info("add new lecturer");

        if (result.hasErrors()) {
            LOGGER.info("group edit form contains {} errors", result.getErrorCount());
            result.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "lecturers/lecturer_creator";
        }

        lecturerService.add(lecturer.setActive(true));

        return lecturers(model);
    }
}
