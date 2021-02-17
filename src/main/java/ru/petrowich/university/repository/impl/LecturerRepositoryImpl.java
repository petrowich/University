package ru.petrowich.university.repository.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.repository.LecturerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class LecturerRepositoryImpl implements LecturerRepository {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Lecturer findById(Integer lecturerId) {
        LOGGER.debug("find lecturer by lecturerId {}", lecturerId);
        return entityManager.find(Lecturer.class, lecturerId);
    }

    @Override
    public void save(Lecturer lecturer) {
        LOGGER.debug("save lecturer {}", lecturer);
        entityManager.persist(lecturer);
    }

    @Override
    public void update(Lecturer lecturer) {
        LOGGER.debug("update lecturer {}", lecturer);
        entityManager.merge(lecturer);
    }

    @Override
    public void delete(Lecturer lecturer) {
        LOGGER.debug("deactivate lecturer {}", lecturer);
        update(lecturer.setActive(false));
    }

    @Override
    public List<Lecturer> findAll() {
        LOGGER.debug("find all lecturer");
        return entityManager.createQuery("FROM Lecturer", Lecturer.class).getResultList();
    }
}
