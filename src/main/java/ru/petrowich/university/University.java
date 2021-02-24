package ru.petrowich.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({AppConfiguration.class})
public class University extends SpringBootServletInitializer {
     public static void main(String[] args) {
         SpringApplication.run(University.class, args);
    }
}
