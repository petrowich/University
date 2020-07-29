package ru.petrowich.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.impl.CourseServiceImpl;

@Import({AppConfiguration.class})
public class University {
    private static final ApplicationContext context = new AnnotationConfigApplicationContext(University.class);

    public static void main(String[] args) {
        CourseService courseService = context.getBean(CourseServiceImpl.class);

        System.out.println(courseService.getAll());
    }
}
