package ru.petrowich.university.service;

import ru.petrowich.university.model.Lesson;

import java.util.List;

public interface LessonService extends GenericService<Lesson, Long> {

    List<Lesson> getByLecturerId(Integer lecturerId);

    List<Lesson> getByStudentId(Integer studentId);
}
