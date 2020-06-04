package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.TimeSlotDAO;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private TimeSlotDAO timeSlotDAO;

    @Autowired
    public TimeSlotServiceImpl(TimeSlotDAO timeSlotDAO) {
        this.timeSlotDAO = timeSlotDAO;
    }

    @Override
    public TimeSlot getById(Integer timeSlotId) {
        LOGGER.info("getById {}", timeSlotId);
        return timeSlotDAO.getById(timeSlotId);
    }

    @Override
    public void add(TimeSlot timeSlot) {
        LOGGER.info("add {}", timeSlot);
        timeSlotDAO.add(timeSlot);
    }

    @Override
    public void update(TimeSlot timeSlot) {
        LOGGER.info("update {}", timeSlot);
        timeSlotDAO.update(timeSlot);
    }

    @Override
    public void delete(TimeSlot timeSlot) {
        LOGGER.info("delete {}", timeSlot);
        timeSlotDAO.delete(timeSlot);
    }

    @Override
    public List<TimeSlot> getAll() {
        LOGGER.info("getAll");
        return timeSlotDAO.getAll();
    }
}
