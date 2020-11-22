package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.CourseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CourseServiceImpl implements CourseService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private LecturerDAO lecturerDAO;

    @Autowired
    public CourseServiceImpl(CourseDAO courseDAO, GroupDAO groupDAO, LecturerDAO lecturerDAO) {
        this.lecturerDAO = lecturerDAO;
        this.groupDAO = groupDAO;
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

    @Override
    public void assignGroupToCourse(Group group, Course course) {
        LOGGER.info("assign Group {} to Course {}", group, course);
        courseDAO.assignGroupToCourse(group, course);
    }

    @Override
    public void removeGroupFromCourse(Group group, Course course) {
        LOGGER.info("remove Group {} from Course {}", group, course);
        courseDAO.removeGroupFromCourse(group, course);
    }

    @Override
    public void applyGroupsToCourse(List<Group> groups, Course course) {
        LOGGER.info("apply {} groups to course {}", groups.size(), course);
        List<Group> currentGroups = groupDAO.getByCourseId(course.getId());

        List<Group> additionalGroups = groups.stream()
                .filter(group -> !currentGroups.contains(group))
                .collect(Collectors.toList());

        LOGGER.info("assign {} groups to Course {}", additionalGroups.size(), course);
        courseDAO.assignGroupsToCourse(additionalGroups, course);

        List<Group> excessGroups = currentGroups.stream()
                .filter(group -> !groups.contains(group))
                .collect(Collectors.toList());

        LOGGER.info("remove {} groups from course {}", excessGroups.size(), course);
        courseDAO.removeGroupsFromCourse(excessGroups, course);
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
