package ru.petrowich.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {
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
        Lecturer lecturer = lecturerDAO.getById(lecturerId);

        if (lecturer != null) {
            lecturer.setCourses(courseDAO.getByAuthorId(lecturerId));
            lecturer.setLessons(lessonDAO.getByLecturerId(lecturerId));
        }

        return lecturer;
    }

    @Override
    public void add(Lecturer lecturer) {
        lecturerDAO.add(lecturer);
    }

    @Override
    public void update(Lecturer lecturer) {
        lecturerDAO.update(lecturer);
    }

    @Override
    public void delete(Lecturer lecturer) {
        lecturerDAO.delete(lecturer);
    }

    @Override
    public List<Lecturer> getAll() {
        List<Lecturer> lecturers = lecturerDAO.getAll();

        lecturers.forEach((Lecturer lecturer) -> {
            Integer lecturerId = lecturer.getId();
            lecturer.setCourses(courseDAO.getByAuthorId(lecturerId));
            lecturer.setLessons(lessonDAO.getByLecturerId(lecturerId));
        });

        return lecturers;
    }
}
