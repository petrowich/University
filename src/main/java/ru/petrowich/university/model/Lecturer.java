package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lecturer extends AbstractPerson {
    private List<Course> courses = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Lecturer lecturer = (Lecturer) object;

        return Objects.equals(this.getId(), lecturer.getId());
    }
}
