package ru.petrowich.university.dao;

import ru.petrowich.university.model.Lesson;

import java.util.List;

public interface LessonDAO extends GenericDAO<Lesson, Long> {
    List<Lesson> getByLecturerId(Integer lecturerId);

    List<Lesson> getByStudentId(Integer studentIdId);
}
