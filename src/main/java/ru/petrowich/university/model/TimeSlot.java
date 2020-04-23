package ru.petrowich.university.model;

import java.time.LocalTime;

public class TimeSlot {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;

    public Long getId() {
        return id;
    }

    public TimeSlot setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public TimeSlot setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public TimeSlot setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }
}
