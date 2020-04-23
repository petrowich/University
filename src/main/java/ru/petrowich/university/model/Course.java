package ru.petrowich.university.model;

public class Course {
    private Long id;
    private Lecturer author;
    private String description;
    private boolean active;

    public Long getId() {
        return id;
    }

    public Course setId(Long id) {
        this.id = id;
        return this;
    }

    public Lecturer getAuthor() {
        return author;
    }

    public Course setAuthor(Lecturer author) {
        this.author = author;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Course setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Course setActive(boolean active) {
        this.active = active;
        return this;
    }
}
