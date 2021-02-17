package ru.petrowich.university.repository.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.repository.TimeSlotRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class TimeSlotRepositoryImpl implements TimeSlotRepository {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TimeSlot findById(Integer timeSlotId) {
        LOGGER.debug("find timeslot by timeSlotId {}", timeSlotId);
        return entityManager.find(TimeSlot.class, timeSlotId);
    }

    @Override
    public void save(TimeSlot timeSlot) {
        LOGGER.debug("persist timeslot {}", timeSlot);
        entityManager.persist(timeSlot);
    }

    @Override
    public void update(TimeSlot timeSlot) {
        LOGGER.debug("merge timeslot {}", timeSlot);
        entityManager.merge(timeSlot);
    }

    @Override
    public void delete(TimeSlot timeSlot) {
        LOGGER.debug("delete timeslot {}", timeSlot);

        if (!entityManager.contains(timeSlot)) {
            LOGGER.debug("entityManager does not contain timeslot, find timeslot {}", timeSlot);
            timeSlot = entityManager.find(TimeSlot.class, timeSlot.getId());
        }

        entityManager.remove(timeSlot);
    }

    @Override
    public List<TimeSlot> findAll() {
        LOGGER.debug("find all timeslots");
        return entityManager.createQuery("FROM TimeSlot", TimeSlot.class).getResultList();
    }
}
