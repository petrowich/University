package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends AbstractPerson {
    private Group group;
    private List<Course> courses = new ArrayList<>();
    private List<Class> classes = new ArrayList<>();

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public Student setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }

    public List<Class> getClasses() {
        return new ArrayList<>(classes);
    }

    public Student setClasses(List<Class> classes) {
        this.classes = classes;
        return this;
    }
}
