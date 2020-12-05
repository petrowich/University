package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lecturer extends AbstractPerson {
    private List<Course> courses = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    public Lecturer setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Lecturer setActive(boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public Lecturer setFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public Lecturer setLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public Lecturer setEmail(String email) {
        super.setEmail(email);
        return this;
    }

    @Override
    public Lecturer setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Lecturer setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public Lecturer setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }
}
