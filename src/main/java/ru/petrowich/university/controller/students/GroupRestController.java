package ru.petrowich.university.controller.students;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.petrowich.university.dto.students.GroupDTO;
import ru.petrowich.university.mapper.students.GroupMapper;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.GroupService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/students/groups/")
public class GroupRestController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    @Autowired
    public GroupRestController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDTO> getGroup(@PathVariable("id") Integer groupId) {
        LOGGER.info("processing request of getting group id={}", groupId);

        if (groupId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group group = groupService.getById(groupId);

        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GroupDTO groupDTO = groupMapper.toDto(group);

        return new ResponseEntity<>(groupDTO, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<GroupDTO> addGroup(@RequestBody GroupDTO groupDTO) {
        LOGGER.info("processing request of creating new group");

        Group newGroup = groupMapper.toEntity(groupDTO);
        Group actualGroup = groupService.add(newGroup);
        GroupDTO actualGroupDTO = groupMapper.toDto(actualGroup);

        return new ResponseEntity<>(actualGroupDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@RequestBody GroupDTO groupDTO, @PathVariable("id") Integer groupId) {
        LOGGER.info("processing request of updating group id={}", groupId);

        if (groupId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group persistentGroup = groupService.getById(groupId);

        if (persistentGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Group group = groupMapper.toEntity(groupDTO).setId(groupId);
        Group actualGroup = groupService.update(group);
        GroupDTO actualGroupDTO = groupMapper.toDto(actualGroup);

        return new ResponseEntity<>(actualGroupDTO, HttpStatus.OK);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<GroupDTO> deleteGroup(@PathVariable("id") Integer groupId) {
        LOGGER.info("processing request of deactivating group id={}", groupId);

        if (groupId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Group persistentGroup = groupService.getById(groupId);

        if (persistentGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        groupService.delete(persistentGroup);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        LOGGER.info("processing request of listing groups");

        List<GroupDTO> groupDTOs = this.groupService.getAll().stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());

        if (groupDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(groupDTOs, HttpStatus.OK);
    }
}
