package ru.petrowich.university.repository;

import ru.petrowich.university.model.Lesson;

import java.util.List;

public interface LessonRepository extends GenericRepository<Lesson, Long> {
    List<Lesson> findByLecturerId(Integer lecturerId);

    List<Lesson> findByStudentId(Integer studentIdId);
}
