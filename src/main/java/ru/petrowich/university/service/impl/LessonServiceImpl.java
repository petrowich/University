package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.service.LessonService;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LessonServiceImpl implements LessonService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final Validator validator;
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(Validator validator, LessonRepository lessonRepository) {
        this.validator = validator;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson getById(Long lessonId) {
        LOGGER.debug("getById {}", lessonId);

        if (lessonId == null) {
            throw new IllegalArgumentException("null is passed instead valid lessonId");
        }

        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        return optionalLesson.orElse(null);
    }

    @Override
    public Lesson add(Lesson lesson) {
        LOGGER.debug("add {}", lesson);

        if (lesson == null) {
            throw new NullPointerException();
        }

        checkViolations(lesson);

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson update(Lesson lesson) {
        LOGGER.debug("update {}", lesson);

        if (lesson == null) {
            throw new IllegalArgumentException("null is passed instead lesson");
        }

        checkViolations(lesson);

        return lessonRepository.save(lesson);
    }

    @Override
    public void delete(Lesson lesson) {
        LOGGER.debug("delete {}", lesson);
        lessonRepository.delete(lesson);
    }

    @Override
    public List<Lesson> getAll() {
        LOGGER.debug("getAll");
        return lessonRepository.findAll();
    }

    private void checkViolations(Lesson lesson) {
        Set<ConstraintViolation<Lesson>> violations = validator.validate(lesson);

        if(!violations.isEmpty()) {
            violations.forEach(violation -> LOGGER.error(violation.getMessage()));
            throw new ConstraintViolationException(violations);
        }
    }
}
