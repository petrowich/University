package ru.petrowich.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrowich.university.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
