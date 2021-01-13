package ru.petrowich.university.service;

import org.springframework.transaction.annotation.Transactional;
import ru.petrowich.university.model.TimeSlot;

@Transactional
public interface TimeSlotService extends GenericService<TimeSlot, Integer> {
}
