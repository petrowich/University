package ru.petrowich.university.repository.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.repository.StudentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class StudentRepositoryImpl implements StudentRepository {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Student findById(Integer studentId) {
        LOGGER.debug("find student by studentId {}", studentId);
        return entityManager.find(Student.class, studentId);
    }

    @Override
    public void save(Student student) {
        LOGGER.debug("persist student {}", student);
        entityManager.persist(student);
    }

    @Override
    public void update(Student student) {
        LOGGER.debug("merge student {}", student);
        entityManager.merge(student);
    }

    @Override
    public void delete(Student student) {
        LOGGER.debug("deactivate student {}", student);
        update(student.setActive(false));
    }

    @Override
    public List<Student> findAll() {
        LOGGER.debug("find all students");
        return entityManager.createQuery("FROM Student AS s", Student.class).getResultList();
    }
}
