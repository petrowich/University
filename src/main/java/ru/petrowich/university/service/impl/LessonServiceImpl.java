package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.service.LessonService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LessonServiceImpl implements LessonService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Lesson getById(Long lessonId) {
        LOGGER.debug("getById {}", lessonId);
        return lessonRepository.findById(lessonId);
    }

    @Override
    public void add(Lesson lesson) {
        LOGGER.debug("add {}", lesson);
        lessonRepository.save(lesson);
    }

    @Override
    public void update(Lesson lesson) {
        LOGGER.debug("update {}", lesson);
        lessonRepository.update(lesson);
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
}
