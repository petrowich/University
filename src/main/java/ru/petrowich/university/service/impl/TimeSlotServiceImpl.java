package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.TimeSlotRepository;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final Validator validator;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotServiceImpl(Validator validator, TimeSlotRepository timeSlotRepository) {
        this.validator = validator;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public TimeSlot getById(Integer timeSlotId) {
        LOGGER.debug("getById {}", timeSlotId);

        if (timeSlotId == null) {
            throw new IllegalArgumentException("null is passed instead valid timeSlotId");
        }

        Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.findById(timeSlotId);
        return optionalTimeSlot.orElse(null);
    }

    @Override
    public TimeSlot add(TimeSlot timeSlot) {
        LOGGER.debug("add {}", timeSlot);

        if (timeSlot == null) {
            throw new IllegalArgumentException("null is passed instead timeSlot");
        }

        checkViolations(timeSlot);

        return timeSlotRepository.save(timeSlot);
    }

    @Override
    public TimeSlot update(TimeSlot timeSlot) {
        LOGGER.debug("update {}", timeSlot);

        if (timeSlot == null) {
            throw new NullPointerException();
        }

        checkViolations(timeSlot);

        return timeSlotRepository.save(timeSlot);
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

    private void checkViolations(TimeSlot timeSlot) {
        Set<ConstraintViolation<TimeSlot>> violations = validator.validate(timeSlot);

        if(!violations.isEmpty()) {
            violations.forEach(violation -> LOGGER.error(violation.getMessage()));
            throw new ConstraintViolationException(violations);
        }
    }
}
