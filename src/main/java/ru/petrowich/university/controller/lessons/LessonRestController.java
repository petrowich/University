package ru.petrowich.university.controller.lessons;

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
    public ResponseEntity<LessonDTO> addLesson(@RequestBody LessonDTO lessonDTO) {
        LOGGER.info("processing request of creating new lesson");

        Lesson newLesson = lessonMapper.toEntity(lessonDTO);
        Lesson actualLesson = lessonService.add(newLesson);
        LessonDTO actualLessonDTO = lessonMapper.toDto(actualLesson);

        return new ResponseEntity<>(actualLessonDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@RequestBody LessonDTO lessonDTO, @PathVariable("id") Long lessonId) {
        LOGGER.info("processing request of updating lesson id={}", lessonId);

        if (lessonId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lesson persistentLesson = lessonService.getById(lessonId);

        if (persistentLesson == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Lesson lesson = lessonMapper.toEntity(lessonDTO).setId(lessonId);
        Lesson actualLesson = lessonService.update(lesson);
        LessonDTO actualLessonDTO = lessonMapper.toDto(actualLesson);

        return new ResponseEntity<>(actualLessonDTO, HttpStatus.OK);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<LessonDTO> deleteLesson(@PathVariable("id") Long lessonId) {
        LOGGER.info("processing request of deactivating lesson id={}", lessonId);

        LOGGER.info("delete lesson");

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
