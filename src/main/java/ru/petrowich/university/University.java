package ru.petrowich.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class University extends SpringBootServletInitializer {
     public static void main(String[] args) {
         SpringApplication.run(University.class, args);
    }
}
