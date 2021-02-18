package ru.petrowich.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrowich.university.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}
