package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.LecturerRepository;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LecturerServiceImpl implements LecturerService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LecturerRepository lecturerRepository;

    @Autowired
    public LecturerServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public Lecturer getById(Integer lecturerId) {
        LOGGER.debug("getById {}", lecturerId);
        return lecturerRepository.findById(lecturerId);
    }

    @Override
    public void add(Lecturer lecturer) {
        LOGGER.debug("add {}", lecturer);
        lecturerRepository.save(lecturer);
    }

    @Override
    public void update(Lecturer lecturer) {
        LOGGER.debug("update {}", lecturer);
        lecturerRepository.update(lecturer);
    }

    @Override
    public void delete(Lecturer lecturer) {
        LOGGER.debug("delete {}", lecturer);
        lecturerRepository.delete(lecturer);
    }

    @Override
    public List<Lecturer> getAll() {
        LOGGER.debug("getAll");
        return lecturerRepository.findAll();
    }
}
