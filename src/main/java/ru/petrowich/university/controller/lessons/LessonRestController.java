package ru.petrowich.university.controller.lessons;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
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
import ru.petrowich.university.dto.lessons.LessonDTO;
import ru.petrowich.university.mapper.lesson.LessonMapper;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.service.LessonService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@Tag(name = "Lessons", description = "operating university lessons")
@RequestMapping("/api/lessons/")
public class LessonRestController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    public LessonRestController(LessonService lessonService, LessonMapper lessonMapper) {
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get lesson by id", description = "returns a single lesson by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the lesson", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "The lesson id is not found", content = @Content)
    })
    public ResponseEntity<LessonDTO> getLesson(@PathVariable("id") Long lessonId) {
        LOGGER.info("processing request of getting lesson id={}", lessonId);

        if (lessonId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lesson lesson = lessonService.getById(lessonId);

        if (lesson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        return new ResponseEntity<>(lessonDTO, HttpStatus.OK);
    }

    @PostMapping("add")
    @Operation(summary = "create a new lesson", description = "adds a single lesson in the system, assigns a new internal id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added the new lesson", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid lesson data supplied", content = @Content)
    })
    public ResponseEntity<LessonDTO> addLesson(@RequestBody LessonDTO lessonDTO) {
        LOGGER.info("processing request of creating new lesson");

        Lesson newLesson = lessonMapper.toEntity(lessonDTO);
        Lesson actualLesson = lessonService.add(newLesson);
        LessonDTO actualLessonDTO = lessonMapper.toDto(actualLesson);

        return new ResponseEntity<>(actualLessonDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "get lesson by id", description = "overwrites a single lesson of supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overwritten the lesson", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LessonDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "The lesson id is not found", content = @Content)
    })
    public ResponseEntity<LessonDTO> updateLesson(@RequestBody LessonDTO lessonDTO, @PathVariable("id") Long lessonId) {
        LOGGER.info("processing request of updating lesson id={}", lessonId);

        if (lessonId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lesson persistentLesson = lessonService.getById(lessonId);

        if (persistentLesson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson.setId(lessonId);
        Lesson actualLesson = lessonService.update(lesson);
        LessonDTO actualLessonDTO = lessonMapper.toDto(actualLesson);

        return new ResponseEntity<>(actualLessonDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "delete lesson by id", description = "removes a single group by supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deactivated the lesson", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "The lesson id is not found", content = @Content)
    })
    public ResponseEntity<LessonDTO> deleteLesson(@PathVariable("id") Long lessonId) {
        LOGGER.info("processing request of deactivating lesson id={}", lessonId);

        if (lessonId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lesson persistentLesson = lessonService.getById(lessonId);

        if (persistentLesson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        lessonService.delete(persistentLesson);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get all of the lessons", description = "returns the full list of active lessons records in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the lessons",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LessonDTO.class)))
            )
    })
    public ResponseEntity<List<LessonDTO>> getAllLessons() {
        LOGGER.info("processing request of listing lessons");

        List<LessonDTO> lessonDTOs = this.lessonService.getAll().stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());

        if (lessonDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(lessonDTOs, HttpStatus.OK);
    }
}
