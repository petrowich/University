package ru.petrowich.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrowich.university.model.Lecturer;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
}
