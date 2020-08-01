package ru.petrowich.university;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.petrowich.university.dao.DataBaseConfiguration;

@Configuration
@ComponentScan({
        "ru.petrowich.university.service",
        "ru.petrowich.university.dao",
        "ru.petrowich.university.util"
})
@Import({DataBaseConfiguration.class})
public class AppConfiguration {
}
