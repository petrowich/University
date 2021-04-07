package ru.petrowich.university.dto.lecturers;

import ru.petrowich.university.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

public class LecturerDTO extends AbstractDTO {
    private Integer id = null;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String comment = null;
    private transient List<LecturerCourseDTO> courses = new ArrayList<>();
    boolean active;

    public Integer getId() {
        return id;
    }

    public LecturerDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public LecturerDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public LecturerDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LecturerDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public LecturerDTO setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public LecturerDTO setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<LecturerCourseDTO> getCourses() {
        return courses;
    }

    public LecturerDTO setCourses(List<LecturerCourseDTO> courses) {
        this.courses = courses;
        return this;
    }
}
