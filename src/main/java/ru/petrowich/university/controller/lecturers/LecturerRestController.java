package ru.petrowich.university.controller.lecturers;

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
import ru.petrowich.university.dto.lecturers.LecturerDTO;
import ru.petrowich.university.mapper.lecturers.LecturerMapper;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/lecturers/")
public class LecturerRestController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final LecturerService lecturerService;
    private final LecturerMapper lecturerMapper;

    public LecturerRestController(LecturerService lecturerService, LecturerMapper lecturerMapper) {
        this.lecturerService = lecturerService;
        this.lecturerMapper = lecturerMapper;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LecturerDTO> getLecturer(@PathVariable("id") Integer lecturerId) {
        LOGGER.info("processing request of getting lecturer id={}", lecturerId);

        if (lecturerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lecturer lecturer = lecturerService.getById(lecturerId);

        if (lecturer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LecturerDTO lecturerDTO = lecturerMapper.toDto(lecturer);

        return new ResponseEntity<>(lecturerDTO, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<LecturerDTO> addLecturer(@RequestBody LecturerDTO lecturerDTO) {
        LOGGER.info("processing request of creating new lecturer");

        Lecturer newLecturer = lecturerMapper.toEntity(lecturerDTO);
        Lecturer actualLecturer = lecturerService.add(newLecturer);
        LecturerDTO actualLecturerDTO= lecturerMapper.toDto(actualLecturer);

        return new ResponseEntity<>(actualLecturerDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<LecturerDTO> updateLecturer(@RequestBody LecturerDTO lecturerDTO, @PathVariable("id") Integer lecturerId) {
        LOGGER.info("processing request of updating lecturer id={}", lecturerId);

        if (lecturerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lecturer persistentLecturer = lecturerService.getById(lecturerId);

        if (persistentLecturer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Lecturer lecturer = lecturerMapper.toEntity(lecturerDTO).setId(lecturerId);
        Lecturer actualLecturer = lecturerService.update(lecturer);
        LecturerDTO actualLecturerDTO= lecturerMapper.toDto(actualLecturer);

        return new ResponseEntity<>(actualLecturerDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<LecturerDTO> deleteLecturer(@PathVariable("id") Integer lecturerId) {
        LOGGER.info("processing request of deactivating lecturer id={}", lecturerId);

        if (lecturerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Lecturer persistentLecturer = lecturerService.getById(lecturerId);

        if (persistentLecturer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        lecturerService.delete(persistentLecturer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LecturerDTO>> getAllLecturers() {
        LOGGER.info("processing request of listing courses");

        List<LecturerDTO> lecturerDTOs = this.lecturerService.getAll().stream()
                .map(lecturerMapper::toDto)
                .collect(Collectors.toList());

        if (lecturerDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(lecturerDTOs, HttpStatus.OK);
    }
}
