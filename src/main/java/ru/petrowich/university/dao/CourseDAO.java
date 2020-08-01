package ru.petrowich.university.dao;

import ru.petrowich.university.model.Course;

import java.util.List;

public interface CourseDAO extends GenericDAO<Course, Integer> {
    List<Course> getByAuthorId(Integer authorId);

    List<Course> getByStudentId(Integer studentId);

    List<Course> getByGroupId(Integer group_id);
}
