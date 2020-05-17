package ru.petrowich.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"ru.petrowich.university.dao","ru.petrowich.university.util"})
@Import({DataBaseConfiguration.class})
public class University {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(University.class);
    }
}
