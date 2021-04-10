package ru.petrowich.university.dto.lessons;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class LessonDTO extends AbstractEntityDTO<Long> {
    private Integer courseId = null;
    private String courseName = null;
    private Integer lecturerId = null;
    private String lecturerFullName = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate date = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime = null;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime = null;

    private Integer numberOfAttendees = null;

    public Integer getCourseId() {
        return courseId;
    }

    public LessonDTO setCourseId(Integer courseId) {
        this.courseId = courseId;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public LessonDTO setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public LessonDTO setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
        return this;
    }

    public String getLecturerFullName() {
        return lecturerFullName;
    }

    public LessonDTO setLecturerFullName(String lecturerFullName) {
        this.lecturerFullName = lecturerFullName;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public LessonDTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LessonDTO setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LessonDTO setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Integer getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public LessonDTO setNumberOfAttendees(Integer numberOfAttendees) {
        this.numberOfAttendees = numberOfAttendees;
        return this;
    }
}
