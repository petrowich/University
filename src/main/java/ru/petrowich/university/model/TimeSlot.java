package ru.petrowich.university.model;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.Objects;

@Entity(name = "TimeSlot")
@Table(name = "t_timeslots")
public class TimeSlot {
    @Id
    @SequenceGenerator(name="seq_timeslots", sequenceName="seq_timeslots", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_timeslots")
    @Column(name = "timeslot_id")
    private Integer id;

    @NotBlank(message = "timeslot name is empty")
    @Size(max=255, message = "timeslot name length is more than 255 characters")
    @Column(name = "timeslot_name")
    private String name;

    @NotNull(message = "timeslot start time is null")
    @Column(name = "timeslot_start_time")
    private LocalTime startTime;

    @NotNull(message = "timeslot end time is null")
    @Column(name = "timeslot_end_time")
    private LocalTime endTime;

    public Integer getId() {
        return id;
    }

    public TimeSlot setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TimeSlot setName(String name) {
        this.name = name;
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
