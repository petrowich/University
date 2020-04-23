package ru.petrowich.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Class {
    private Long id;
    private Course course;
    private Lecturer lecturer;
    private LocalDate date;
    private TimeSlot timeSlot;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Group> groups = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Class setId(Long id) {
        this.id = id;
        return this;
    }

    public Course getCourse() {
        return course;
    }

    public Class setCourse(Course course) {
        this.course = course;
        return this;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public Class setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Class setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public Class setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Class setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Class setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Class setGroups(List<Group> groups) {
        this.groups = groups;
        return this;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Class setStudents(List<Student> students) {
        this.students = students;
        return this;
    }
}
