package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.StudentRepository;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getById(Integer studentId) {
        LOGGER.debug("getById {}", studentId);

        if (studentId == null) {
            return null;
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        return studentOptional.orElse(null);
    }

    @Override
    public void add(Student student) {
        LOGGER.debug("add {}", student);
        studentRepository.save(student);
    }

    @Override
    public void update(Student student) {
        LOGGER.debug("update {}", student);
        studentRepository.save(student);
    }

    @Override
    public void delete(Student student) {
        LOGGER.debug("delete {}", student);
        Optional<Student> studentOptional = studentRepository.findById(student.getId());

        if (studentOptional.isPresent()) {
            Student currentStudent = studentOptional.get();
            currentStudent.setActive(false);
            studentRepository.delete(student);
        }
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("getAll");
        return studentRepository.findAll();
    }
}
