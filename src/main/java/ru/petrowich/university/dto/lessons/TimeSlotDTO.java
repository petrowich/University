package ru.petrowich.university.dto.lessons;

import ru.petrowich.university.dto.AbstractDTO;

import java.time.LocalTime;

public class TimeSlotDTO extends AbstractDTO {
    private Integer id = null;
    private String name;
    private LocalTime startTime = null;
    private LocalTime endTime = null;

    public Integer getId() {
        return id;
    }

    public TimeSlotDTO setId(Integer id) {
        this.id = id;
        return this;
    }

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
