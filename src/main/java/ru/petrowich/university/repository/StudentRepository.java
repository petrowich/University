package ru.petrowich.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrowich.university.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
