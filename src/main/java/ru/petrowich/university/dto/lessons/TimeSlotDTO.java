package ru.petrowich.university.dto.lessons;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.time.LocalTime;

public class TimeSlotDTO extends AbstractEntityDTO<Integer> {
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime = null;

    public String getName() {
        return name;
    }

    public TimeSlotDTO setName(String name) {
        this.name = name;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public TimeSlotDTO setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public TimeSlotDTO setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }
}
