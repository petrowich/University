package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.TimeSlotRepository;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public TimeSlot getById(Integer timeSlotId) {
        LOGGER.debug("getById {}", timeSlotId);
        return timeSlotRepository.findById(timeSlotId);
    }

    @Override
    public void add(TimeSlot timeSlot) {
        LOGGER.debug("add {}", timeSlot);
        timeSlotRepository.save(timeSlot);
    }

    @Override
    public void update(TimeSlot timeSlot) {
        LOGGER.debug("update {}", timeSlot);
        timeSlotRepository.update(timeSlot);
    }

    @Override
    public void delete(TimeSlot timeSlot) {
        LOGGER.debug("delete {}", timeSlot);
        timeSlotRepository.delete(timeSlot);
    }

    @Override
    public List<TimeSlot> getAll() {
        LOGGER.debug("getAll");
        return timeSlotRepository.findAll();
    }
}
