package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends AbstractPerson {
    private Group group = new Group();
    private List<Course> courses = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    public Student setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Student setActive(boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public Student setFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public Student setLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public Student setEmail(String email) {
        super.setEmail(email);
        return this;
    }

    @Override
    public Student setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public Student setGroup(Group group) {
        this.group = group;
        return this;
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public Student setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    public Student setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }
}
