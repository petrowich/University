package ru.petrowich.university.service;

import ru.petrowich.university.dao.GenericDAO;
import ru.petrowich.university.model.Course;

import java.util.List;

public interface CourseService extends GenericDAO<Course, Integer> {
    List<Course> getByAuthorId(Integer authorId);

    List<Course> getByStudentId(Integer studentId);

    List<Course> getByGroupId(Integer groupId);
}
