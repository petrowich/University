package ru.petrowich.university.model;

import java.util.List;

public class Group {
    private Long id;
    private List<Student> students;
    private boolean active;

    public Long getId() {
        return id;
    }

    public Group setId(Long id) {
        this.id = id;
        return this;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Group setStudents(List<Student> students) {
        this.students = students;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Group setActive(boolean active) {
        this.active = active;
        return this;
    }
}
