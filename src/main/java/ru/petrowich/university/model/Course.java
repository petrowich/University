package ru.petrowich.university.model;

import java.util.Objects;

public class Course {
    private Integer id;
    private String name;
    private String description;
    private Lecturer author = new Lecturer();
    private boolean active;

    public Integer getId() {
        return id;
    }

    public Course setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Course setDescription(String description) {
        this.description = description;
        return this;
    }

    public Lecturer getAuthor() {
        return author;
    }

    public Course setAuthor(Lecturer author) {
        this.author = author;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Course setActive(boolean active) {
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

        Course course = (Course) object;

        return Objects.equals(this.getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
