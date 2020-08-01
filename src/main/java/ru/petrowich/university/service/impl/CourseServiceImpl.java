package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CourseServiceImpl implements CourseService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private CourseDAO courseDAO;
    private LecturerDAO lecturerDAO;

    @Autowired
    public CourseServiceImpl(CourseDAO courseDAO, LecturerDAO lecturerDAO) {
        this.lecturerDAO = lecturerDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public Course getById(Integer id) {
        LOGGER.info("getById {}", id);
        Course course = courseDAO.getById(id);

        if (course != null) {
            fillWithAuthor(course);
        }

        return course;
    }

    @Override
    public void add(Course course) {
        LOGGER.info("add {}", course);
        courseDAO.add(course);
    }

    @Override
    public void update(Course course) {
        LOGGER.info("update {}", course);
        courseDAO.update(course);
    }

    @Override
    public void delete(Course course) {
        LOGGER.info("delete {}", course);
        courseDAO.delete(course);
    }

    @Override
    public List<Course> getAll() {
        LOGGER.info("getAll");
        List<Course> courses = courseDAO.getAll();
        fillWithAuthors(courses);
        return courses;
    }

    @Override
    public List<Course> getByAuthorId(Integer authorId) {
        LOGGER.info("getByAuthorId {}", authorId);
        List<Course> courses = courseDAO.getByAuthorId(authorId);
        fillWithAuthors(courses);
        return courses;
    }

    @Override
    public List<Course> getByStudentId(Integer studentId) {
        LOGGER.info("getByStudentId {}", studentId);
        List<Course> courses = courseDAO.getByStudentId(studentId);
        fillWithAuthors(courses);
        return courses;
    }

    @Override
    public List<Course> getByGroupId(Integer groupId) {
        LOGGER.info("getByGroupId {}", groupId);
        List<Course> courses = courseDAO.getByGroupId(groupId);
        fillWithAuthors(courses);
        return courses;
    }

    private void fillWithAuthor(Course course) {
        Integer lecturerId = course.getAuthor().getId();

        if (lecturerId == null) {
            return;
        }

        Lecturer lecturer = lecturerDAO.getById(course.getAuthor().getId());

        if (lecturer != null) {
            course.setAuthor(lecturer);
        }
    }


    private void fillWithAuthors(List<Course> courses) {
        Map<Integer, Lecturer> lecturerMap = new HashMap<>();

        courses.stream()
                .map(course -> course.getAuthor().getId())
                .distinct()
                .forEach((Integer lecturerId) -> {
                    if (lecturerId != null) {
                        lecturerMap.put(lecturerId, lecturerDAO.getById(lecturerId));
                    }
                });

        courses.forEach((Course course) -> {
            Integer courseId = course.getAuthor().getId();

            if (courseId != null) {
                course.setAuthor(lecturerMap.get(courseId));
            }
        });
    }
}
