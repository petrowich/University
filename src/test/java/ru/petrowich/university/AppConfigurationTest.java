package ru.petrowich.university;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"ru.petrowich.university.dao", "ru.petrowich.university.util"})
@Import({DataBaseConfigurationTest.class})
public class AppConfigurationTest {
}
