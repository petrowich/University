package ru.petrowich.university.dao;

import ru.petrowich.university.model.Student;

import java.util.List;

public interface StudentDAO extends GenericDAO<Student, Integer> {
    List<Student> getByGroupId(Integer groupId);

    List<Student> getByCourseId(Integer courseId);

    List<Student> getByLessonId(Long lessonId);
}