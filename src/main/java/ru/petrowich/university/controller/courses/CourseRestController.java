package ru.petrowich.university.controller.courses;

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
import ru.petrowich.university.dto.courses.CourseDTO;
import ru.petrowich.university.dto.courses.CourseGroupAssignmentDTO;
import ru.petrowich.university.dto.courses.CourseGroupDTO;
import ru.petrowich.university.mapper.courses.CourseGroupMapper;
import ru.petrowich.university.mapper.courses.CourseMapper;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.service.CourseService;
import ru.petrowich.university.service.GroupService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/courses/")
public class CourseRestController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final CourseService courseService;
    private final GroupService groupService;
    private final CourseMapper courseMapper;
    private final CourseGroupMapper courseGroupMapper;

    @Autowired
    public CourseRestController(CourseService courseService, GroupService groupService, CourseMapper courseMapper, CourseGroupMapper courseGroupMapper) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.courseMapper = courseMapper;
        this.courseGroupMapper = courseGroupMapper;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseDTO> getCourse(@PathVariable("id") Integer courseId) {
        LOGGER.info("processing request of getting course id={}", courseId);

        if (courseId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Course course = courseService.getById(courseId);

        if (course == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CourseDTO courseDTO = courseMapper.toDto(course);

        return new ResponseEntity<>(courseDTO, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<CourseDTO> addCourse(@RequestBody CourseDTO courseDTO) {
        LOGGER.info("processing request of creating new course");

        Course newCourse = courseMapper.toEntity(courseDTO);
        Course actualCourse = courseService.add(newCourse);
        CourseDTO actualCourseDTO = courseMapper.toDto(actualCourse);

        return new ResponseEntity<>(actualCourseDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody CourseDTO courseDTO, @PathVariable("id") Integer courseId) {
        LOGGER.info("processing request of updating course id={}", courseId);

        if (courseId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Course persistentCourse = courseService.getById(courseId);

        if (persistentCourse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseMapper.toEntity(courseDTO).setId(courseId);
        Course actualCourse = courseService.update(course);
        CourseDTO actualCourseDTO = courseMapper.toDto(actualCourse);

        return new ResponseEntity<>(actualCourseDTO, HttpStatus.OK);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable("id") Integer courseId) {
        LOGGER.info("processing request of deactivating course id={}", courseId);

        if (courseId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Course persistentCourse = courseService.getById(courseId);

        if (persistentCourse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        courseService.delete(persistentCourse);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        LOGGER.info("processing request of listing courses");

        List<CourseDTO> courseDTOs = this.courseService.getAll().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());

        if (courseDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(courseDTOs, HttpStatus.OK);
    }

    @PutMapping("assign-group")
    public ResponseEntity<List<CourseGroupDTO>> assignGroup(@RequestBody CourseGroupAssignmentDTO courseGroupAssignmentDTO) {
        LOGGER.info("processing request of assigning group to course");

        if (courseGroupAssignmentDTO.getCourseId() == null || courseGroupAssignmentDTO.getGroupId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Course persistentCourse = courseService.getById(courseGroupAssignmentDTO.getCourseId());
        Group persistentGroup = groupService.getById(courseGroupAssignmentDTO.getGroupId());

        if (persistentCourse == null || persistentGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        courseService.assignGroupToCourse(persistentGroup, persistentCourse);
        Course actualCourse = courseService.getById(persistentCourse.getId());
        List<CourseGroupDTO> courseGroupDTOList = actualCourse.getGroups().stream().map(courseGroupMapper::toDto).collect(Collectors.toList());

        return new ResponseEntity<>(courseGroupDTOList, HttpStatus.OK);
    }

    @PutMapping("remove-group")
    public ResponseEntity<List<CourseGroupDTO>> removeGroup(@RequestBody CourseGroupAssignmentDTO courseGroupAssignmentDTO) {
        LOGGER.info("processing request of assigning group to course");

        if (courseGroupAssignmentDTO.getCourseId() == null || courseGroupAssignmentDTO.getGroupId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Course persistentCourse = courseService.getById(courseGroupAssignmentDTO.getCourseId());
        Group persistentGroup = groupService.getById(courseGroupAssignmentDTO.getGroupId());

        if (persistentCourse == null || persistentGroup == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        courseService.removeGroupFromCourse(persistentGroup, persistentCourse);
        Course actualCourse = courseService.getById(persistentCourse.getId());
        List<CourseGroupDTO> courseGroupDTOList = actualCourse.getGroups().stream().map(courseGroupMapper::toDto).collect(Collectors.toList());

        return new ResponseEntity<>(courseGroupDTOList, HttpStatus.OK);
    }
}
