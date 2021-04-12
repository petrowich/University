package ru.petrowich.university.dto.students;

import ru.petrowich.university.dto.AbstractEntityDTO;

import java.util.ArrayList;
import java.util.List;

public class GroupDTO extends AbstractEntityDTO<Integer> {
    private String name = null;
    private Integer capacity = null;
    private Integer numberOfAssignedCourses = null;
    private List<GroupCourseDTO> courses = new ArrayList<>();
    private Integer numberOfStudents = null;
    private List<GroupStudentDTO> students = new ArrayList<>();
    private boolean active;

    public String getName() {
        return name;
    }

    public GroupDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public GroupDTO setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public Integer getNumberOfAssignedCourses() {
        return numberOfAssignedCourses;
    }

    public GroupDTO setNumberOfAssignedCourses(Integer numberOfAssignedCourses) {
        this.numberOfAssignedCourses = numberOfAssignedCourses;
        return this;
    }

    public List<GroupCourseDTO> getCourses() {
        return courses;
    }

    public GroupDTO setCourses(List<GroupCourseDTO> courses) {
        this.courses = courses;
        return this;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public GroupDTO setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
        return this;
    }

    public List<GroupStudentDTO> getStudents() {
        return students;
    }

    public GroupDTO setStudents(List<GroupStudentDTO> students) {
        this.students = students;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public GroupDTO setActive(boolean active) {
        this.active = active;
        return this;
    }
}
