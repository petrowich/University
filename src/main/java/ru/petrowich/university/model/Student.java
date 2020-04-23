package ru.petrowich.university.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends AbstractPerson {
    private Group group;
    private List<Course> courses = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

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

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons);
    }

    public Student setLessons(List<Lesson> lessons) {
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

        Student student = (Student) object;

        return Objects.equals(this.getId(), student.getId());
    }
}
