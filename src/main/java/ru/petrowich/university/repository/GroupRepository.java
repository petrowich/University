package ru.petrowich.university.repository;

import ru.petrowich.university.model.Group;

import java.util.List;

public interface GroupRepository extends GenericRepository<Group, Integer> {
    List<Group> findByCourseId(Integer courseId);

    List<Group> findByLessonId(Long lessonId);
}
