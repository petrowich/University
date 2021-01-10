package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.repository.GroupRepository;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.repository.StudentRepository;
import ru.petrowich.university.service.LessonService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LessonServiceImpl implements LessonService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Lesson getById(Long lessonId) {
        LOGGER.debug("getById {}", lessonId);
        Lesson lesson = lessonRepository.findById(lessonId);
        fillTransients(lesson);
        return lesson;
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
        List<Lesson> lessons = lessonRepository.findAll();
        lessons.forEach(this::fillTransients);
        return lessons;
    }

    @Override
    public List<Lesson> getByLecturerId(Integer lecturerId) {
        LOGGER.debug("getByLecturer {}", lecturerId);
        List<Lesson> lessons = lessonRepository.findByLecturerId(lecturerId);
        lessons.forEach(this::fillTransients);
        return lessons;
    }

    @Override
    public List<Lesson> getByStudentId(Integer studentId) {
        LOGGER.debug("getByStudent {}", studentId);
        List<Lesson> lessons = lessonRepository.findByStudentId(studentId);
        lessons.forEach(this::fillTransients);
        return lessons;
    }

    private void fillTransients(Lesson lesson) {
        if (lesson != null) {
            lesson.setGroups(groupRepository.findByLessonId(lesson.getId()));
            lesson.setStudents(studentRepository.findByLessonId(lesson.getId()));
        }
    }
}
