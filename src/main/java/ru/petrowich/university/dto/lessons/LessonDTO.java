package ru.petrowich.university.dto.lessons;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "A lesson entity")
public class LessonDTO extends AbstractEntityDTO<Long> {

    @Schema(description = "Numeric internal identifier of the course", example = "101")
    private Integer courseId = null;

    @Schema(description = "A name of the course", example = "Mathematical analysis",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String courseName = null;

    @Schema(description = "Numeric internal identifier of the lecturer leading the lesson", example = "10001")
    private Integer lecturerId = null;

    @Schema(description = "A full name of the lecturer is teaching a the course", example = "Shamil Basayev",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String lecturerFullName = null;

    @Schema(description = "Date of the lesson, format yyyy-MM-dd", example = "2020-07-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date = null;

    @Schema(description = "Lesson start time, format HH:mm", example = "13:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime = null;

    @Schema(description = "Lesson end time, format HH:mm", example = "14:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime = null;

    @Schema(description = "A number of students assigned to the course", example = "30",
            accessMode = Schema.AccessMode.READ_ONLY)
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
