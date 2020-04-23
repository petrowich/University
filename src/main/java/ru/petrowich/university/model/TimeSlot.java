package ru.petrowich.university.model;

import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        TimeSlot timeSlot = (TimeSlot) object;

        return Objects.equals(this.getId(), timeSlot.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
