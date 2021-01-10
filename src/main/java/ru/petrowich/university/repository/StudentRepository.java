package ru.petrowich.university.repository;

import ru.petrowich.university.model.Student;

import java.util.List;

public interface StudentRepository extends GenericRepository<Student, Integer> {
    List<Student> findByGroupId(Integer groupId);

    List<Student> findByCourseId(Integer courseId);

    List<Student> findByLessonId(Long lessonId);
}
