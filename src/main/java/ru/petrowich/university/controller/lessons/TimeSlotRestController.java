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
import ru.petrowich.university.dto.lessons.TimeSlotDTO;
import ru.petrowich.university.mapper.lesson.TimeSlotMapper;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@Tag(name = "Time slots", description = "operating scheduler time slots")
@RequestMapping("/api/lessons/timeslots/")
public class TimeSlotRestController {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final TimeSlotService timeSlotService;
    private final TimeSlotMapper timeSlotMapper;

    public TimeSlotRestController(TimeSlotService timeSlotService, TimeSlotMapper timeSlotMapper) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get time slot by id", description = "returns a single time slot by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the time slot",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeSlotDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The time slot id is not found",
                    content = @Content)
    })
    public ResponseEntity<TimeSlotDTO> getTimeSlot(@PathVariable("id") Integer timeSlotId) {
        LOGGER.info("processing request of getting timeSlot id={}", timeSlotId);

        if (timeSlotId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TimeSlot timeSlot = timeSlotService.getById(timeSlotId);

        if (timeSlot == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TimeSlotDTO timeSlotDTO = timeSlotMapper.toDto(timeSlot);

        return new ResponseEntity<>(timeSlotDTO, HttpStatus.OK);
    }


    @PostMapping("add")
    @Operation(summary = "create a new time slot",
            description = "adds a single time slot in the system, assigns a new internal id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Added the new time slot",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeSlotDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid time slot data supplied",
                    content = @Content)
    })
    public ResponseEntity<TimeSlotDTO> addTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
        LOGGER.info("processing request of creating new timeSlot");

        TimeSlot newTimeSlot = timeSlotMapper.toEntity(timeSlotDTO);
        TimeSlot actualTimeSlot = timeSlotService.add(newTimeSlot);
        TimeSlotDTO actualTimeSlotDTO = timeSlotMapper.toDto(actualTimeSlot);

        return new ResponseEntity<>(actualTimeSlotDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "get time slot by id", description = "overwrites a single time slot of supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Overwritten the time slot",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimeSlotDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The time slot id is not found",
                    content = @Content)
    })
    public ResponseEntity<TimeSlotDTO> updateTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO,
                                                      @PathVariable("id") Integer timeSlotId) {
        LOGGER.info("processing request of updating timeSlot id={}", timeSlotId);

        if (timeSlotId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TimeSlot persistentTimeSlot = timeSlotService.getById(timeSlotId);

        if (persistentTimeSlot == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDTO).setId(timeSlotId);
        TimeSlot actualTimeSlot = timeSlotService.update(timeSlot);
        TimeSlotDTO actualTimeSlotDTO = timeSlotMapper.toDto(actualTimeSlot);

        return new ResponseEntity<>(actualTimeSlotDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "delete time slot by id", description = "removes a single time slot by supplied id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Deactivated the time slot",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "The time slot id is not found",
                    content = @Content)
    })
    public ResponseEntity<TimeSlotDTO> deleteTimeSlot(@PathVariable("id") Integer timeSlotId) {
        LOGGER.info("processing request of deactivating timeSlot id={}", timeSlotId);

        if (timeSlotId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TimeSlot persistentTimeSlot = timeSlotService.getById(timeSlotId);

        if (persistentTimeSlot == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        timeSlotService.delete(persistentTimeSlot);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "get all of the time slots",
            description = "returns the full list of active time slots records in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the time slots",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TimeSlotDTO.class)))
            )
    })
    public ResponseEntity<List<TimeSlotDTO>> getAllTimeSlots() {
        LOGGER.info("processing request of listing timeSlots");

        List<TimeSlotDTO> timeSlotDTOs = this.timeSlotService.getAll().stream()
                .map(timeSlotMapper::toDto)
                .collect(Collectors.toList());

        if (timeSlotDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(timeSlotDTOs, HttpStatus.OK);
    }
}
