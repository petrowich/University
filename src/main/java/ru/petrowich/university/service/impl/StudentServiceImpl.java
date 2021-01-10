package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.CourseRepository;
import ru.petrowich.university.repository.LessonRepository;
import ru.petrowich.university.repository.StudentRepository;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Student getById(Integer studentId) {
        LOGGER.debug("getById {}", studentId);
        Student student = studentRepository.findById(studentId);
        fillTransients(student);
        return student;
    }

    @Override
    public void add(Student student) {
        LOGGER.debug("add {}", student);
        studentRepository.save(student);
    }

    @Override
    public void update(Student student) {
        LOGGER.debug("update {}", student);
        studentRepository.update(student);
    }

    @Override
    public void delete(Student student) {
        LOGGER.debug("delete {}", student);
        studentRepository.delete(student);
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("getAll");
        List<Student> students = studentRepository.findAll();
        students.forEach(this::fillTransients);
        return students;
    }

    @Override
    public List<Student> getByGroupId(Integer groupId) {
        LOGGER.debug("getByGroupId {}", groupId);
        List<Student> students = studentRepository.findByGroupId(groupId);
        students.forEach(this::fillTransients);
        return students;
    }

    @Override
    public List<Student> getByCourseId(Integer courseId) {
        LOGGER.debug("getByCourseId {}", courseId);
        List<Student> students = studentRepository.findByCourseId(courseId);
        students.forEach(this::fillTransients);
        return students;
    }

    @Override
    public List<Student> getByLessonId(Long lessonId) {
        LOGGER.debug("getByLessonId {}", lessonId);
        List<Student> students = studentRepository.findByLessonId(lessonId);
        students.forEach(this::fillTransients);
        return students;
    }

    private void fillTransients(Student student) {
        if (student != null) {
            student.setCourses(courseRepository.findByStudentId(student.getId()));
            student.setLessons(lessonRepository.findByStudentId(student.getId()));
        }
    }
}
