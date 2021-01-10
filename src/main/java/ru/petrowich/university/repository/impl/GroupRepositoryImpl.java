package ru.petrowich.university.repository.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.repository.GroupRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Group findById(Integer groupId) {
        LOGGER.debug("find group by groupId {}", groupId);
        return entityManager.find(Group.class, groupId);
    }

    @Override
    public void save(Group group) {
        LOGGER.debug("save group {}", group);
        entityManager.persist(group);
    }

    @Override
    public void update(Group group) {
        LOGGER.debug("merge group {}", group);
        entityManager.merge(group);
    }

    @Override
    public void delete(Group group) {
        LOGGER.debug("deactivate group {}", group);
        update(group.setActive(false));
    }

    @Override
    public List<Group> findAll() {
        LOGGER.debug("find all groups");
        return entityManager.createQuery("FROM Group", Group.class).getResultList();
    }

    @Override
    public List<Group> findByCourseId(Integer courseId) {
        LOGGER.debug("find groups by courseId {}", courseId);
        return entityManager.createQuery("SELECT g FROM Group AS g INNER JOIN g.courses AS c WHERE c.id = :courseId AND c.active = :active", Group.class)
                .setParameter("courseId", courseId)
                .setParameter("active", true)
                .getResultList();
    }

    @Override
    public List<Group> findByLessonId(Long lessonId) {
        LOGGER.debug("find groups by lessonId {}", lessonId);
        return entityManager.createQuery("SELECT g FROM Lesson AS ls INNER JOIN ls.course AS c INNER JOIN c.groups AS g WHERE ls.id = :lessonId AND g.active = :active", Group.class)
                .setParameter("lessonId", lessonId)
                .setParameter("active", true)
                .getResultList();
    }
}
