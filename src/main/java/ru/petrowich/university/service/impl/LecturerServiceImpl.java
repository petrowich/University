package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.LecturerRepository;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LecturerServiceImpl implements LecturerService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final Validator validator;
    private final LecturerRepository lecturerRepository;

    @Autowired
    public LecturerServiceImpl(Validator validator, LecturerRepository lecturerRepository) {
        this.validator = validator;
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public Lecturer getById(Integer lecturerId) {
        LOGGER.debug("getById {}", lecturerId);

        if (lecturerId == null) {
            throw new NullPointerException();
        }

        Optional<Lecturer> optionalLecturer = lecturerRepository.findById(lecturerId);
        return optionalLecturer.orElse(null);
    }

    @Override
    public void add(Lecturer lecturer) {
        LOGGER.debug("add {}", lecturer);

        if (lecturer == null) {
            throw new NullPointerException();
        }

        checkViolations(lecturer);

        lecturerRepository.save(lecturer);
    }

    @Override
    public void update(Lecturer lecturer) {
        LOGGER.debug("update {}", lecturer);

        if (lecturer == null) {
            throw new NullPointerException();
        }

        checkViolations(lecturer);

        lecturerRepository.save(lecturer);
    }

    @Override
    public void delete(Lecturer lecturer) {
        LOGGER.debug("delete {}", lecturer);
        Optional<Lecturer> optionalLecturer = lecturerRepository.findById(lecturer.getId());

        if(optionalLecturer.isPresent()) {
            Lecturer currentLecturer = optionalLecturer.get();
            currentLecturer.setActive(false);
            lecturerRepository.save(currentLecturer);
        }
    }

    @Override
    public List<Lecturer> getAll() {
        LOGGER.debug("getAll");
        return lecturerRepository.findAll();
    }

    private void checkViolations(Lecturer lecturer) {
        Set<ConstraintViolation<Lecturer>> violations = validator.validate(lecturer);

        if(!violations.isEmpty()) {
            violations.forEach(violation -> LOGGER.error(violation.getMessage()));
            throw new ConstraintViolationException(violations);
        }
    }
}
