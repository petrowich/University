package ru.petrowich.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.TimeSlotDAO;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.List;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private TimeSlotDAO timeSlotDAO;

    @Autowired
    public TimeSlotServiceImpl(TimeSlotDAO timeSlotDAO) {
        this.timeSlotDAO = timeSlotDAO;
    }

    @Override
    public TimeSlot getById(Integer timeSlotId) {
        return timeSlotDAO.getById(timeSlotId);
    }

    @Override
    public void add(TimeSlot timeSlot) {
        timeSlotDAO.add(timeSlot);
    }

    @Override
    public void update(TimeSlot timeSlot) {
        timeSlotDAO.update(timeSlot);
    }

    @Override
    public void delete(TimeSlot timeSlot) {
        timeSlotDAO.delete(timeSlot);
    }

    @Override
    public List<TimeSlot> getAll() {
        return timeSlotDAO.getAll();
    }
}
