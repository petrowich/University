package ru.petrowich.university.service.impl;

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

@Service
public class StudentServiceImpl implements StudentService {
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
        studentDAO.add(student);
    }

    @Override
    public void update(Student student) {
        studentDAO.update(student);
    }

    @Override
    public void delete(Student student) {
        studentDAO.delete(student);
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = studentDAO.getAll().stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    @Override
    public List<Student> getByGroupId(Integer groupId) {
        List<Student> students = studentDAO.getByGroupId(groupId).stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    @Override
    public List<Student> getByCourseId(Integer courseId) {
        List<Student> students = studentDAO.getByCourseId(courseId).stream()
                .map(student -> student.setCourses(courseDAO.getByStudentId(student.getId())))
                .map(student -> student.setLessons(lessonDAO.getByStudentId(student.getId())))
                .collect(toList());

        fillWithGroups(students);

        return students;
    }

    @Override
    public List<Student> getByLessonId(Long lessonId) {
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
