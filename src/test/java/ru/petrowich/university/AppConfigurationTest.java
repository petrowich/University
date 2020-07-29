package ru.petrowich.university;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Configuration
@ComponentScan({"ru.petrowich.university.dao", "ru.petrowich.university.service", "ru.petrowich.university.util"})
@Import({DataBaseConfigurationTest.class})
@SpringJUnitConfig(classes = {DataBaseConfigurationTest.class})
public class AppConfigurationTest {
}
