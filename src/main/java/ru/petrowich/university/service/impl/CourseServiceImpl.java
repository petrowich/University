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
import java.util.Optional;
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
    public Course getById(Integer courseId) {
        LOGGER.debug("getById {}", courseId);

        if (courseId == null) {
            throw new NullPointerException();
        }

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.orElse(null);
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
        courseRepository.save(course);
    }

    @Override
    public void delete(Course course) {
        LOGGER.debug("delete {}", course);
        Optional<Course> optionalCourse = courseRepository.findById(course.getId());

        if (optionalCourse.isPresent()) {
            Course currentCourse = optionalCourse.get();
            currentCourse.setActive(false);
            courseRepository.save(course);
        }
    }

    @Override
    public List<Course> getAll() {
        LOGGER.debug("getAll");
        return courseRepository.findAll();
    }

    @Override
    public void assignGroupToCourse(Group group, Course course) {
        LOGGER.debug("assign Group {} to Course {}", group, course);
        Optional<Group> optionalCurrentGroup = groupRepository.findById(group.getId());
        Optional<Course> optionalCurrentCourse = courseRepository.findById(course.getId());

        if (optionalCurrentCourse.isPresent() && optionalCurrentGroup.isPresent()) {
            Group currentGroup = optionalCurrentGroup.get();
            Course currentCourse = optionalCurrentCourse.get();
            List<Group> currentGroups = currentCourse.getGroups();

            if (!currentGroups.contains(group)) {
                currentGroups.add(currentGroup);
                courseRepository.save(currentCourse);
            }
        }
    }

    @Override
    public void removeGroupFromCourse(Group group, Course course) {
        LOGGER.debug("remove Group {} from Course {}", group, course);
        Optional<Course> optionalCurrentCourse = courseRepository.findById(course.getId());

        if (optionalCurrentCourse.isPresent()) {
            Course currentCourse = optionalCurrentCourse.get();
            currentCourse.getGroups().remove(group);
            courseRepository.save(currentCourse);
        }
    }

    @Override
    public void applyGroupsToCourse(List<Group> groups, Course course) {
        LOGGER.debug("apply {} groups to course {}", groups.size(), course);
        Optional<Course> optionalCurrentCourse = courseRepository.findById(course.getId());

        if (optionalCurrentCourse.isPresent()) {
            List<Integer> groupIds = groups.stream()
                    .filter(Objects::nonNull)
                    .map(Group::getId)
                    .collect(Collectors.toList());
            List<Group> actualGroups = groupRepository.findAllById(groupIds);
            Course currentCourse = optionalCurrentCourse.get();
            currentCourse.setGroups(actualGroups);
            courseRepository.save(currentCourse);
        }
    }
}
