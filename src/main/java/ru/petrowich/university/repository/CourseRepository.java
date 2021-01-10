package ru.petrowich.university.repository;

import ru.petrowich.university.model.Course;

import java.util.List;

public interface CourseRepository extends GenericRepository<Course, Integer>{
    List<Course> findByAuthorId(Integer authorId);

    List<Course> findByStudentId(Integer studentId);

    List<Course> findByGroupId(Integer groupId);
}
