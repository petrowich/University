package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class LecturerServiceImpl implements LecturerService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private LecturerDAO lecturerDAO;
    private CourseDAO courseDAO;
    private LessonDAO lessonDAO;

    @Autowired
    public LecturerServiceImpl(LecturerDAO lecturerDAO, CourseDAO courseDAO, LessonDAO lessonDAO) {
        this.lecturerDAO = lecturerDAO;
        this.courseDAO = courseDAO;
        this.lessonDAO = lessonDAO;
    }

    @Override
    public Lecturer getById(Integer lecturerId) {
        LOGGER.info("getById {}", lecturerId);
        Lecturer lecturer = lecturerDAO.getById(lecturerId);

        if (lecturer != null) {
            lecturer.setCourses(courseDAO.getByAuthorId(lecturerId));
            lecturer.setLessons(lessonDAO.getByLecturerId(lecturerId));
        }

        return lecturer;
    }

    @Override
    public void add(Lecturer lecturer) {
        LOGGER.info("add {}", lecturer);
        lecturerDAO.add(lecturer);
    }

    @Override
    public void update(Lecturer lecturer) {
        LOGGER.info("update {}", lecturer);
        lecturerDAO.update(lecturer);
    }

    @Override
    public void delete(Lecturer lecturer) {
        LOGGER.info("delete {}", lecturer);
        lecturerDAO.delete(lecturer);
    }

    @Override
    public List<Lecturer> getAll() {
        LOGGER.info("getAll");
        List<Lecturer> lecturers = lecturerDAO.getAll();

        lecturers.forEach((Lecturer lecturer) -> {
            Integer lecturerId = lecturer.getId();
            lecturer.setCourses(courseDAO.getByAuthorId(lecturerId));
            lecturer.setLessons(lessonDAO.getByLecturerId(lecturerId));
        });

        return lecturers;
    }
}
