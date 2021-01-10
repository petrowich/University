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

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/lecturers")
public class LecturerController {
    private static final String ATTRIBUTE_LECTURER = "lecturer";
    private static final String ATTRIBUTE_ALL_LECTURERS = "allLecturers";
    private static final String ERROR_MSG_FORM_CONTAINS_ERRORS = "lecturer form contains {} errors";

    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LecturerService lecturerService;

    @Autowired
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("")
    public String lecturers(Model model) {
        LOGGER.info("listing lecturers");

        Comparator<Lecturer> lecturerComparator = Comparator.comparing(Lecturer::isActive).reversed()
                .thenComparing(Lecturer::getFullName);

        List<Lecturer> lecturers = lecturerService.getAll().stream()
                .sorted(lecturerComparator)
                .collect(Collectors.toList());
        model.addAttribute(ATTRIBUTE_ALL_LECTURERS, lecturers);

        LOGGER.debug("number of lecturers: {}", lecturers.size());

        return "lecturers/lecturers";
    }

    @GetMapping("/lecturer")
    public String lecturer(@RequestParam("id") Integer lecturerId, Model model) {
        LOGGER.info("getting lecturer id={}", lecturerId);

        Lecturer lecturer = lecturerService.getById(lecturerId);
        model.addAttribute(ATTRIBUTE_LECTURER, lecturer);
        LOGGER.debug("lecturer: {} {}", lecturer.getId(), lecturer.getFullName());

        return "lecturers/lecturer";
    }

    @GetMapping("/lecturer/edit")
    public String edit(@RequestParam("id") Integer lecturerId, Model model) {
        LOGGER.info("getting lecturer id={}", lecturerId);

        Lecturer lecturer = lecturerService.getById(lecturerId);
        model.addAttribute(ATTRIBUTE_LECTURER, lecturer);
        LOGGER.debug("lecturer: {} {}", lecturer.getId(), lecturer.getFullName());

        return "lecturers/lecturer_editor";
    }

    @PostMapping("/lecturer/update")
    public String update(Lecturer lecturer, BindingResult bindingResult, Model model, HttpServletResponse httpServletResponse) {
        LOGGER.info("submitting the changes of lecturer id={}", lecturer.getId());

        if (bindingResult.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return lecturers(model);
        }

        lecturerService.update(lecturer);

        return lecturers(model);
    }

    @GetMapping("/lecturer/new")
    public String create(Model model) {
        LOGGER.info("creating lecturer");

        model.addAttribute(ATTRIBUTE_LECTURER, new Lecturer());

        return "lecturers/lecturer_creator";
    }

    @PostMapping("/lecturer/add")
    public String add(Lecturer lecturer, BindingResult bindingResult, Model model) {
        LOGGER.info("add new lecturer");

        if (bindingResult.hasErrors()) {
            LOGGER.info(ERROR_MSG_FORM_CONTAINS_ERRORS, bindingResult.getErrorCount());
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.info(objectError.getDefaultMessage()));
            return "lecturers/lecturer_creator";
        }

        lecturerService.add(lecturer.setActive(true));

        return lecturers(model);
    }
}
