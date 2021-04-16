package ru.petrowich.university.dto.students;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A student entity")
public class StudentDTO extends AbstractEntityDTO<Integer> {

    @Schema(description = "A student first name", example = "Rulon")
    private String firstName = null;

    @Schema(description = "A student last name", example = "Oboev")
    private String lastName = null;

    @Schema(description = "An email of the lecturer", example = "rulon.oboev@university.edu")
    private String email = null;

    @Schema(description = "An additional notes about student", example = "bla bla bla")
    private String comment = null;

    @Schema(description = "Numeric internal identifier of the student group", example = "101")
    private Integer groupId = null;

    @Schema(description = "A  name of the student group", example = "Shamil Basayev",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String groupName = null;

    @Schema(description = "A number of courses assigned to the student", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer numberOfAssignedCourses = null;

    @Schema(description = "A list of courses assigned to the student",
            accessMode = Schema.AccessMode.READ_ONLY)
    private transient List<StudentCourseDTO> courses = new ArrayList<>();

    @Schema(description = "A status of the student record in a system",
            accessMode = Schema.AccessMode.READ_ONLY)
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
