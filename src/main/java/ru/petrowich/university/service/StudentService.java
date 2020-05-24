package ru.petrowich.university.service;

import ru.petrowich.university.model.Student;

import java.util.List;

public interface StudentService extends GenericService<Student, Integer> {
    List<Student> getByGroupId(Integer groupId);

    List<Student> getByCourseId(Integer courseId);

    List<Student> getByLessonId(Long lessonId);
}
