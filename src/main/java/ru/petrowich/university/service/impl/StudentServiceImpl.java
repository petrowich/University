package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.repository.StudentRepository;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.service.StudentService;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final Validator validator = buildDefaultValidatorFactory().getValidator();
    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getById(Integer studentId) {
        LOGGER.debug("getById {}", studentId);

        if (studentId == null) {
            throw new NullPointerException();
        }

        Optional<Student> studentOptional = studentRepository.findById(studentId);
        return studentOptional.orElse(null);
    }

    @Override
    public void add(Student student) {
        LOGGER.debug("add {}", student);

        if (student == null) {
            throw new NullPointerException();
        }

        if(checkViolations(student)){
            throw new IllegalArgumentException();
        }

        studentRepository.save(student);
    }

    @Override
    public void update(Student student) {
        LOGGER.debug("update {}", student);

        if (student == null) {
            throw new NullPointerException();
        }

        if(checkViolations(student)){
            throw new IllegalArgumentException();
        }

        studentRepository.save(student);
    }

    @Override
    public void delete(Student student) {
        LOGGER.debug("delete {}", student);
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());

        if (optionalStudent.isPresent()) {
            Student currentStudent = optionalStudent.get();
            currentStudent.setActive(false);
            studentRepository.save(student);
        }
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("getAll");
        return studentRepository.findAll();
    }

    private boolean checkViolations(Student student) {
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        violations.forEach(violation -> LOGGER.error(violation.getMessage()));
        return !violations.isEmpty();
    }
}
