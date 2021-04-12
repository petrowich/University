package ru.petrowich.university.dto.students;

import ru.petrowich.university.dto.AbstractEntityDTO;

import java.util.ArrayList;
import java.util.List;

public class StudentDTO extends AbstractEntityDTO<Integer> {
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String comment = null;
    private Integer groupId = null;
    private String groupName = null;
    private Integer numberOfAssignedCourses = null;
    private transient List<StudentCourseDTO> courses = new ArrayList<>();
    boolean active;

    public String getFirstName() {
        return firstName;
    }

    public StudentDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public StudentDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public StudentDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public StudentDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public StudentDTO setGroupId(Integer groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getGroupName() {
        return groupName;
    }

    public StudentDTO setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public Integer getNumberOfAssignedCourses() {
        return numberOfAssignedCourses;
    }

    public void setNumberOfAssignedCourses(Integer numberOfAssignedCourses) {
        this.numberOfAssignedCourses = numberOfAssignedCourses;
    }

    public List<StudentCourseDTO> getCourses() {
        return courses;
    }

    public StudentDTO setCourses(List<StudentCourseDTO> courses) {
        this.courses = courses;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public StudentDTO setActive(boolean active) {
        this.active = active;
        return this;
    }
}
