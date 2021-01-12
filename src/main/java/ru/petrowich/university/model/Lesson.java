package ru.petrowich.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Lesson")
@Table(name = "t_lessons")
public class Lesson implements Comparable<Lesson> {

    @Id
    @SequenceGenerator(name = "seq_lessons", sequenceName = "seq_lessons", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lessons")
    @Column(name = "lesson_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course = new Course();

    @ManyToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "person_id")
    private Lecturer lecturer = new Lecturer();

    @Column(name = "lesson_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "timeslot_id", referencedColumnName = "timeslot_id")
    private TimeSlot timeSlot = new TimeSlot();

    @Column(name = "lesson_start_time")
    private LocalTime startTime;

    @Column(name = "lesson_end_time")
    private LocalTime endTime;

    @Transient
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
        course.getGroups().forEach(group -> students.addAll(group.getStudents()));
        return this;
    }

    public Lecturer getLecturer() {
        if (lecturer == null) {
            return new Lecturer();
        }

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

    public List<Student> getStudents() {
        students = new ArrayList<>();
        course.getGroups().forEach(group -> students.addAll(group.getStudents()));
        return students;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Lesson lesson) {
        int compare = this.date.compareTo(lesson.date);

        if (compare != 0) {
            return compare;
        }

        compare = this.startTime.compareTo(lesson.startTime);

        if (compare != 0) {
            return compare;
        }

        compare = lesson.endTime.compareTo(this.endTime);

        if (compare != 0) {
            return compare;
        }

        return this.id.compareTo(lesson.id);
    }
}
