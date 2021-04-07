package ru.petrowich.university.dto.lessons;

import ru.petrowich.university.dto.AbstractDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class LessonDTO extends AbstractDTO {
    private Long id = null;
    private Integer courseId = null;
    private String courseName = null;
    private Integer lecturerId = null;
    private String lecturerFullName = null;
    private LocalDate date = null;
    private LocalTime startTime = null;
    private LocalTime endTime = null;
    private Integer numberOfAttendees = null;

    public Long getId() {
        return id;
    }

    public LessonDTO setId(Long id) {
        this.id = id;
        return this;
    }

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
