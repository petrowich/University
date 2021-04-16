package ru.petrowich.university.controller.lecturers;

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
import ru.petrowich.university.dto.lecturers.LecturerDTO;
import ru.petrowich.university.mapper.lecturers.LecturerMapper;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.service.LecturerService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@Tag(name = "Lecturers", description = "operating university lecturers")
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
    @Operation(summary = "get lecturer by id", description = "returns a single lecturer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the lecturer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LecturerDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The lecturer id is not found",
                    content = @Content)
    })
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
    @Operation(summary = "create a new lecturer",
            description = "adds a single lecturer in the system, assigns a new internal id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added the new lecturer", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LecturerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid lecturer data supplied", content = @Content)}
    )
    public ResponseEntity<LecturerDTO> addLecturer(@RequestBody LecturerDTO lecturerDTO) {
        LOGGER.info("processing request of creating new lecturer");

        Lecturer newLecturer = lecturerMapper.toEntity(lecturerDTO);
        Lecturer actualLecturer = lecturerService.add(newLecturer);
        LecturerDTO actualLecturerDTO = lecturerMapper.toDto(actualLecturer);

        return new ResponseEntity<>(actualLecturerDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "get lecturer by id", description = "overwrites a single lecturer of supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overwritten the lecturer", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LecturerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "The lecturer id is not found", content = @Content)
    })
    public ResponseEntity<LecturerDTO> updateLecturer(@RequestBody LecturerDTO lecturerDTO,
                                                      @PathVariable("id") Integer lecturerId) {
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
        LecturerDTO actualLecturerDTO = lecturerMapper.toDto(actualLecturer);

        return new ResponseEntity<>(actualLecturerDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "delete lecturer by id", description = "deactivates a single lecturer by supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deactivated the lecturer", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LecturerDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "The lecturer id is not found", content = @Content)
    })
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
    @Operation(summary = "get all of the lecturers",
            description = "returns the full list of active lecturers records in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the lecturers",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LecturerDTO.class)))
            )
    })
    public ResponseEntity<List<LecturerDTO>> getAllLecturers() {
        LOGGER.info("processing request of listing lecturers");

        List<LecturerDTO> lecturerDTOs = this.lecturerService.getAll().stream()
                .map(lecturerMapper::toDto)
                .collect(Collectors.toList());

        if (lecturerDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(lecturerDTOs, HttpStatus.OK);
    }
}
