package ru.petrowich.university.service;

import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;

import java.util.List;

public interface CourseService extends GenericService<Course, Integer> {
    List<Course> getByAuthorId(Integer authorId);

    List<Course> getByStudentId(Integer studentId);

    List<Course> getByGroupId(Integer groupId);

    void assignGroupToCourse(Group group, Course course);

    void removeGroupFromCourse(Group group, Course course);

    void applyGroupsToCourse(List<Group> groups, Course course);
}
