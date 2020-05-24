package ru.petrowich.university.service;

import ru.petrowich.university.dao.GenericDAO;
import ru.petrowich.university.model.Group;

import java.util.List;

public interface GroupService extends GenericDAO<Group, Integer> {
    List<Group> getByCourseId(Integer courseId);
}
