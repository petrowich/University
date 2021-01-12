package ru.petrowich.university.repository.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.repository.CourseRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Course findById(Integer courseId) {
        LOGGER.debug("find course by courseId {}", courseId);
        return entityManager.find(Course.class, courseId);
    }

    @Override
    public void save(Course course) {
        LOGGER.debug("persist course {}", course);
        entityManager.persist(course);
    }

    @Override
    public void update(Course course) {
        LOGGER.debug("merge course {}", course);
        entityManager.merge(course);
    }

    @Override
    public void delete(Course course) {
        LOGGER.debug("deactivate course {}", course);
        update(course.setActive(false));
    }

    @Override
    public List<Course> findAll() {
        LOGGER.debug("find all courses");
        return entityManager.createQuery("FROM Course", Course.class)
                .getResultList();
    }
}
