package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentDAO studentDAO;
    private final GroupDAO groupDAO;
    private final CourseDAO courseDAO;
    private final LessonDAO lessonDAO;

    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO, GroupDAO groupDAO, CourseDAO courseDAO, LessonDAO lessonDAO) {
        this.studentDAO = studentDAO;
        this.groupDAO = groupDAO;
        this.courseDAO = courseDAO;
        this.lessonDAO = lessonDAO;
    }

    @Override
    public Student getById(Integer studentId) {
        LOGGER.debug("getById {}", studentId);
        Student student = studentDAO.getById(studentId);

        if (student != null) {
            fillWithGroup(student);
            student.setCourses(courseDAO.getByStudentId(studentId));
            student.setLessons(lessonDAO.getByStudentId(studentId));
        }

        return student;
    }

    @Override
    public void add(Student student) {
        LOGGER.debug("add {}", student);
        studentDAO.add(student);
    }

    @Override
    public void update(Student student) {
        LOGGER.debug("update {}", student);
        studentDAO.update(student);
    }

    @Override
    public void delete(Student student) {
        LOGGER.debug("delete {}", student);
        studentDAO.delete(student);
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("getAll");
        List<Student> students = studentDAO.getAll().stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    @Override
    public List<Student> getByGroupId(Integer groupId) {
        LOGGER.debug("getByGroupId {}", groupId);
        List<Student> students = studentDAO.getByGroupId(groupId).stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    @Override
    public List<Student> getByCourseId(Integer courseId) {
        LOGGER.debug("getByCourseId {}", courseId);
        List<Student> students = studentDAO.getByCourseId(courseId).stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    @Override
    public List<Student> getByLessonId(Long lessonId) {
        LOGGER.debug("getByLessonId {}", lessonId);
        List<Student> students = studentDAO.getByLessonId(lessonId).stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    private void fillWithGroup(Student student) {
        Integer groupId = student.getGroup().getId();

        if (groupId == null) {
            return;
        }

        Group group = groupDAO.getById(groupId);

        if (group != null) {
            student.setGroup(group);
        }
    }

    private void fillWithGroups(List<Student> students) {
        Map<Integer, Group> groupMap = new HashMap<>();

        students.stream().map(lesson -> lesson.getGroup().getId())
                .distinct()
                .forEach((Integer groupId) -> {
                    if (groupId != null)
                        groupMap.put(groupId, groupDAO.getById(groupId));
                });

        students.forEach((Student student) -> {
            Integer groupId = student.getGroup().getId();

            if (groupId != null) {
                student.setGroup(groupMap.get(groupId));
            }
        });
    }
}
