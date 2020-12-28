package ru.petrowich.university.dao;

import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;

import java.util.List;

public interface CourseDAO extends GenericDAO<Course, Integer> {
    List<Course> getByAuthorId(Integer authorId);

    List<Course> getByStudentId(Integer studentId);

    List<Course> getByGroupId(Integer groupId);

    void assignGroupToCourse(Group group, Course course);

    void removeGroupFromCourse(Group group, Course course);

    void assignGroupsToCourse(List<Group> groups, Course course);

    void removeGroupsFromCourse(List<Group> groups, Course course);
}
