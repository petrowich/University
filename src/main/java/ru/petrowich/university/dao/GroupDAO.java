package ru.petrowich.university.dao;

import ru.petrowich.university.model.Group;

import java.util.List;

public interface GroupDAO extends GenericDAO<Group, Integer> {
    List<Group> getByCourseId(Integer courseId);
    List<Group> getByLessonId(Long lessonId);
}
