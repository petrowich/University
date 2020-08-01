package ru.petrowich.university.controller.lessons;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.service.LessonService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class LessonsController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LessonService lessonService;

    @Autowired
    public LessonsController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/lessons")
    public String courses(Model model) {
        LOGGER.info("listing Lessons");
        List<Lesson> lessons = lessonService.getAll().stream()
                .sorted()
                .collect(Collectors.toList());;
        model.addAttribute("allLessons", lessons);
        LOGGER.debug("number of Lessons: {}", lessons.size());

        return "lessons/lessons";
    }
}
