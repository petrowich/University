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

    @Override
    public List<Student> findByGroupId(Integer groupId) {
        LOGGER.debug("find students by groupId {}", groupId);
        return entityManager.createQuery("SELECT s FROM Student AS s INNER JOIN s.group AS g WHERE g.id = :groupId AND g.active = :active", Student.class)
                .setParameter("groupId", groupId)
                .setParameter("active", true)
                .getResultList();
    }

    @Override
    public List<Student> findByCourseId(Integer courseId) {
        LOGGER.debug("find students by courseId {}", courseId);
        return entityManager.createQuery("SELECT DISTINCT s FROM Course AS c INNER JOIN c.groups AS g INNER JOIN g.students as s WHERE c.id = :courseId AND c.active = :active", Student.class)
                .setParameter("courseId", courseId)
                .setParameter("active", true)
                .getResultList();
    }

    @Override
    public List<Student> findByLessonId(Long lessonId) {
        LOGGER.debug("find students by courseId {}", lessonId);
        return entityManager.createQuery("SELECT DISTINCT s FROM Lesson AS ls INNER JOIN ls.course AS c INNER JOIN c.groups AS g INNER JOIN g.students AS s WHERE ls.id = :lessonId AND s.active = :active", Student.class)
                .setParameter("lessonId", lessonId)
                .setParameter("active", true)
                .getResultList();
    }
}
