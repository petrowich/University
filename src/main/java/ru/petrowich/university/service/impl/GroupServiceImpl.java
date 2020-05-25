package ru.petrowich.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.GroupService;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO, StudentDAO studentDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
    }

    @Override
    public Group getById(Integer groupId) {
        Group group = groupDAO.getById(groupId);

        if (group != null) {
            group.setStudents(studentDAO.getByGroupId(groupId));
        }

        return group;
    }

    @Override
    public void add(Group group) {
        groupDAO.add(group);
    }

    @Override
    public void update(Group group) {
        groupDAO.update(group);
    }

    @Override
    public void delete(Group group) {
        groupDAO.delete(group);
    }

    @Override
    public List<Group> getAll() {
        List<Group> groups = groupDAO.getAll();
        groups.forEach(group -> group.setStudents(studentDAO.getByGroupId(group.getId())));
        return groups;
    }

    @Override
    public List<Group> getByCourseId(Integer courseId) {
        List<Group> groups = groupDAO.getByCourseId(courseId);
        groups.forEach(group -> group.setStudents(studentDAO.getByGroupId(group.getId())));
        return groups;
    }
}
