package ru.petrowich.university;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({
        "ru.petrowich.university.service",
        "ru.petrowich.university.repository",
        "ru.petrowich.university.model",
        "ru.petrowich.university.util"
})
@Import({HibernateConfiguration.class})
public class AppConfiguration {
}
