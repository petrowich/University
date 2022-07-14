package ru.petrowich.university.dto.lecturers;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A lecturer entity")
public class LecturerDTO extends AbstractEntityDTO<Integer> {

    @Schema(description = "A lecturer first name", example = "Shamil")
    private String firstName = null;

    @Schema(description = "A lecturer first name", example = "Basayev")
    private String lastName = null;

    @Schema(description = "An email of the lecturer", example = "giorgio.parisi@university.edu")
    private String email = null;

    @Schema(description = "An additional notes about lecturer", example = "bla bla bla")
    private String comment = null;

    @Schema(description = "A list of courses taught by the lecturer", accessMode = Schema.AccessMode.READ_ONLY)
    private transient List<LecturerCourseDTO> courses = new ArrayList<>();

    @Schema(description = "A status of the lecturer record in a system", accessMode = Schema.AccessMode.READ_ONLY)
    boolean active;

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
