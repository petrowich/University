package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.repository.CourseRepository;
import ru.petrowich.university.repository.GroupRepository;
import ru.petrowich.university.service.CourseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CourseServiceImpl implements CourseService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Course getById(Integer id) {
        LOGGER.debug("getById {}", id);
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    public void add(Course course) {
        LOGGER.debug("add {}", course);
        courseRepository.save(course);
    }

    @Override
    public void update(Course course) {
        LOGGER.debug("update {}", course);
        courseRepository.update(course);
    }

    @Override
    public void delete(Course course) {
        LOGGER.debug("delete {}", course);
        courseRepository.delete(course);
    }

    @Override
    public List<Course> getAll() {
        LOGGER.debug("getAll");
        return courseRepository.findAll();
    }

    @Override
    public void assignGroupToCourse(Group group, Course course) {
        LOGGER.debug("assign Group {} to Course {}", group, course);

        Course currentCourse = courseRepository.findById(course.getId());
        Group currentGroup = groupRepository.findById(group.getId());
        List<Group> actualGroups = currentCourse.getGroups();

        if (!actualGroups.contains(group)) {
            actualGroups.add(currentGroup);
            courseRepository.update(currentCourse);
        }
    }

    @Override
    public void removeGroupFromCourse(Group group, Course course) {
        LOGGER.debug("remove Group {} from Course {}", group, course);

        Course currentCourse = courseRepository.findById(course.getId());

        List<Group> currentGroups = currentCourse.getGroups();
        currentGroups.remove(group);

        courseRepository.update(currentCourse);
    }

    @Override
    public void applyGroupsToCourse(List<Group> groups, Course course) {
        LOGGER.debug("apply {} groups to course {}", groups.size(), course);

        List<Group> actualGroups = groups.stream()
                .filter(Objects::nonNull)
                .map(group -> groupRepository.findById(group.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Course currentCourse = courseRepository.findById(course.getId());

        currentCourse.setGroups(actualGroups);

        courseRepository.update(currentCourse);
    }
}
