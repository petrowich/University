package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;

public class Lecturer extends AbstractPerson {
    private List<Course> courses = new ArrayList<>();
    private List<Class> classes = new ArrayList<>();

    public List<Course> getCourses() {
        return courses;
    }

    public Lecturer setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public Lecturer setClasses(List<Class> classes) {
        this.classes = classes;
        return this;
    }
}
