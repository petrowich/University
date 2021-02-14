package ru.petrowich.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.petrowich.university.model.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
}
