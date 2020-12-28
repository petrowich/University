package ru.petrowich.university.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class HomeController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @GetMapping("/")
    public String home(Model model) {
        LOGGER.info("getting the home page");
        return "home";
    }
}
