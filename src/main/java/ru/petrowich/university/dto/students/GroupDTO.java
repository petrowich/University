package ru.petrowich.university.dto.students;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A group entity")
public class GroupDTO extends AbstractEntityDTO<Integer> {

    @Schema(description = "A group name", example = "AA-01")
    private String name = null;

    @Schema(description = "Maximum number of student in the group", example = "30")
    private Integer capacity = null;

    @Schema(description = "Number of courses the group is assigned to", example = "5",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer numberOfAssignedCourses = null;

    @Schema(description = "A list of courses the group is assigned to",
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<GroupCourseDTO> courses = new ArrayList<>();

    @Schema(description = "Actual number of students of the group", example = "20",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer numberOfStudents = null;

    @Schema(description = "A list of students of the group",
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<GroupStudentDTO> students = new ArrayList<>();

    @Schema(description = "A status of the group record in a system",
            accessMode = Schema.AccessMode.READ_ONLY)
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
