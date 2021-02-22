package ru.petrowich.university.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.repository.GroupRepository;
import ru.petrowich.university.service.GroupService;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;
import static javax.validation.Validation.buildDefaultValidatorFactory;

@Service
public class GroupServiceImpl implements GroupService {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final Validator validator = buildDefaultValidatorFactory().getValidator();
    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group getById(Integer groupId) {
        LOGGER.debug("getById {}", groupId);

        if (groupId == null) {
            throw new NullPointerException();
        }

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.orElse(null);
    }

    @Override
    public void add(Group group) {
        LOGGER.debug("add {}", group);

        if (group == null) {
            throw new NullPointerException();
        }

        if(checkViolations(group)){
            throw new IllegalArgumentException();
        }

        groupRepository.save(group);
    }

    @Override
    public void update(Group group) {
        LOGGER.debug("update {}", group);

        if (group == null) {
            throw new NullPointerException();
        }

        if(checkViolations(group)){
            throw new IllegalArgumentException();
        }

        groupRepository.save(group);
    }

    @Override
    public void delete(Group group) {
        LOGGER.debug("delete {}", group);
        Optional<Group> optionalGroup = groupRepository.findById(group.getId());

        if(optionalGroup.isPresent()) {
            Group currentGroup = optionalGroup.get();
            currentGroup.setActive(false);
            groupRepository.save(currentGroup);
        }
    }

    @Override
    public List<Group> getAll() {
        LOGGER.debug("getAll");
        return groupRepository.findAll();
    }

    private boolean checkViolations(Group group) {
        Set<ConstraintViolation<Group>> violations = validator.validate(group);
        violations.forEach(violation -> LOGGER.error(violation.getMessage()));
        return !violations.isEmpty();
    }
}
