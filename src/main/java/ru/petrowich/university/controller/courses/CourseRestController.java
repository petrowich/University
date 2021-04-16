package ru.petrowich.university.controller.courses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Courses", description = "operating university courses")
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
    @Operation(summary = "get course by id", description = "returns a single course by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the course",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The course id is not found",
                    content = @Content)
    })
    public ResponseEntity<CourseDTO> getCourse(@PathVariable("id") @Parameter(
            description = "The course internal numeric identifier",
            required = true) Integer courseId) {
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
    @Operation(summary = "create a new course",
            description = "adds a single course in the system, assigns a new internal id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Added the new course", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class)
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid course data supplied",
                    content = @Content)
    })
    public ResponseEntity<CourseDTO> addCourse(@RequestBody @Parameter(description = "The new course data",
            required = true) CourseDTO courseDTO) {
        LOGGER.info("processing request of creating new course");

        Course newCourse = courseMapper.toEntity(courseDTO);
        Course actualCourse = courseService.add(newCourse);
        CourseDTO actualCourseDTO = courseMapper.toDto(actualCourse);

        return new ResponseEntity<>(actualCourseDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "get course by id", description = "overwrites a single course of supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overwritten the course",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "The course id is not found",
                    content = @Content)
    })
    public ResponseEntity<CourseDTO> updateCourse(@RequestBody @Parameter(
            description = "The course data for update",
            required = true) CourseDTO courseDTO, @PathVariable("id") @Parameter(
            description = "Updating course internal numeric identifier",
            required = true) Integer courseId) {
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
    @Operation(summary = "delete course by id", description = "deactivates a single course by supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Deactivated the course",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CourseDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The course id is not found",
                    content = @Content)
    })
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable("id") @Parameter(
            description = "Deleting course internal numeric identifier", required = true) Integer courseId) {
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
    @Operation(summary = "get all of the courses",
            description = "returns the full list of active courses records in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the courses",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseDTO.class))
                    ))
    })
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
    @Operation(summary = "assign a group to the course",
            description = "assigns a group to the course by their ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "All of assigned groups to the course",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseGroupDTO.class)))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid group or course id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The group or course id is not found",
                    content = @Content)
    })
    public ResponseEntity<List<CourseGroupDTO>> assignGroup(@RequestBody @Parameter(
            description = "Assignment group and course ids",
            required = true) CourseGroupAssignmentDTO courseGroupAssignmentDTO) {
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
    @Operation(summary = "removes a group from the course",
            description = "cancels the assignment group to the course by their ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "All of assigned groups to the course",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourseGroupDTO.class)))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid group or course id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The group or course id is not found",
                    content = @Content)
    })
    public ResponseEntity<List<CourseGroupDTO>> removeGroup(@RequestBody @Parameter(
            description = "Removing group and course ids",
            required = true) CourseGroupAssignmentDTO courseGroupAssignmentDTO) {
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
