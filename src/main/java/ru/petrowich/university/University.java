package ru.petrowich.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.petrowich.university.dao.DataBaseConfiguration;

import org.slf4j.Logger;
import ru.petrowich.university.service.GroupService;
import ru.petrowich.university.service.impl.GroupServiceImpl;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@ComponentScan({"ru.petrowich.university.service","ru.petrowich.university.dao","ru.petrowich.university.util"})
@Import({DataBaseConfiguration.class})
public class University {
    private static final Logger LOGGER = getLogger(University.class.getSimpleName());
    private static final ApplicationContext context = new AnnotationConfigApplicationContext(University.class);

    public static void main(String[] args) {
        GroupService groupService = (GroupServiceImpl) context.getBean(GroupServiceImpl.class);
    }
}
