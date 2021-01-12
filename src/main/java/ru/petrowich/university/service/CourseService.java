package ru.petrowich.university.service;

import org.springframework.transaction.annotation.Transactional;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;

import java.util.List;

public interface CourseService extends GenericService<Course, Integer> {
    @Transactional
    void assignGroupToCourse(Group group, Course course);

    @Transactional
    void removeGroupFromCourse(Group group, Course course);

    @Transactional
    void applyGroupsToCourse(List<Group> groups, Course course);
}
