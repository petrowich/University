package ru.petrowich.university.repository.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.repository.LessonRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class LessonRepositoryImpl implements LessonRepository {

    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Lesson findById(Long lessonId) {
        LOGGER.debug("find lesson by lessonId {}", lessonId);
        return entityManager.find(Lesson.class, lessonId);
    }

    @Override
    public void save(Lesson lesson) {
        LOGGER.debug("persist lesson {}", lesson);
        entityManager.persist(lesson);
    }

    @Override
    public void update(Lesson lesson) {
        LOGGER.debug("merge lesson {}", lesson);
        entityManager.merge(lesson);
    }

    @Override
    public void delete(Lesson lesson) {
        LOGGER.debug("delete lesson {}", lesson);

        if (lesson != null && !entityManager.contains(lesson)) {
            LOGGER.debug("entityManager does not contain lesson, find lesson {}", lesson);
            lesson = entityManager.find(Lesson.class, lesson.getId());
        }

        entityManager.remove(lesson);
    }

    @Override
    public List<Lesson> findAll() {
        LOGGER.debug("find all lessona");
        return entityManager.createQuery("FROM Lesson", Lesson.class).getResultList();
    }

    @Override
    public List<Lesson> findByLecturerId(Integer lecturerId) {
        LOGGER.debug("find lesson by lecturerId {}", lecturerId);
        return entityManager.createQuery("SELECT ls FROM Lesson AS ls INNER JOIN ls.lecturer AS lc WHERE lc.id = :lecturerId AND lc.active = :active", Lesson.class)
                .setParameter("lecturerId", lecturerId)
                .setParameter("active", true)
                .getResultList();
    }

    @Override
    public List<Lesson> findByStudentId(Integer studentId) {
        LOGGER.debug("find lesson by studentId {}", studentId);
        return entityManager.createQuery("SELECT ls FROM Lesson AS ls INNER JOIN ls.course As c INNER JOIN c.groups AS g INNER JOIN g.students as s WHERE s.id = :studentId AND s.active = :active", Lesson.class)
                .setParameter("studentId", studentId)
                .setParameter("active", true)
                .getResultList();
    }
}
