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
import ru.petrowich.university.dto.lessons.TimeSlotDTO;
import ru.petrowich.university.mapper.lesson.TimeSlotMapper;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.service.TimeSlotService;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
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
    public ResponseEntity<TimeSlotDTO> addTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
        LOGGER.info("processing request of creating new timeSlot");

        TimeSlot newTimeSlot = timeSlotMapper.toEntity(timeSlotDTO);
        TimeSlot actualTimeSlot = timeSlotService.add(newTimeSlot);
        TimeSlotDTO actualTimeSlotDTO = timeSlotMapper.toDto(actualTimeSlot);

        return new ResponseEntity<>(actualTimeSlotDTO, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<TimeSlotDTO> updateTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO, @PathVariable("id") Integer timeSlotId) {
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
    public ResponseEntity<TimeSlotDTO> deleteTimeSlot(@PathVariable("id") Integer timeSlotId) {
        LOGGER.info("processing request of deactivating timeSlot id={}", timeSlotId);

        LOGGER.info("delete timeSlot");

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
