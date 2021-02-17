package ru.petrowich.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrowich.university.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
