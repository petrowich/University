package ru.petrowich.university.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lesson implements Comparable<Lesson> {
    private Long id;
    private Course course = new Course();
    private Lecturer lecturer = new Lecturer();
    private LocalDate date;
    private TimeSlot timeSlot = new TimeSlot();
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Group> groups = new ArrayList<>();
    private List<Student> students = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Lesson setId(Long id) {
        this.id = id;
        return this;
    }

    public Course getCourse() {
        return course;
    }

    public Lesson setCourse(Course course) {
        this.course = course;
        return this;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public Lesson setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Lesson setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public Lesson setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Lesson setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Lesson setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Lesson setGroups(List<Group> groups) {
        this.groups = groups;
        return this;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Lesson setStudents(List<Student> students) {
        this.students = students;
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

        Lesson lesson = (Lesson) object;

        return Objects.equals(id, lesson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Lesson lesson){
        int compare = this.date.compareTo(lesson.date);

        if (compare!=0) {
            return compare;
        }

        compare = this.startTime.compareTo(lesson.startTime);

        if (compare!=0) {
            return compare;
        }

        compare = lesson.endTime.compareTo(this.endTime);

        if (compare!=0) {
            return compare;
        }

        return this.id.compareTo(lesson.id);
    }
}
