package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group {
    private Integer id;
    private String name;
    private List<Student> students = new ArrayList<>();
    private boolean active;

    public Integer getId() {
        return id;
    }

    public Group setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Group group = (Group) object;

        return Objects.equals(this.getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
