package ru.petrowich.university.dto.courses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractEntityDTO;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "A course entity")
public class CourseDTO extends AbstractEntityDTO<Integer> {

    @Schema(description = "A name of the course", example = "Mathematical analysis")
    private String name = null;

    @Schema(description = "A short description of the course", example = "Mathematical analysis for economic students")
    private String description = null;

    @Schema(description = "Numeric internal identifier of the lecturer is teaching the course", example = "10001")
    private Integer authorId = null;

    @Schema(description = "A full name of the lecturer is teaching a the course", example = "Shamil Basayev", accessMode = Schema.AccessMode.READ_ONLY)
    private String authorFullName = null;

    @Schema(description = "A number of groups assigned to the course", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer numberOfAssignedGroups = null;

    @Schema(description = "A list of groups assigned to course", accessMode = Schema.AccessMode.READ_ONLY)
    private transient List<CourseGroupDTO> groups = new ArrayList<>();

    @Schema(description = "A status of the course record in a system", accessMode = Schema.AccessMode.READ_ONLY)
    boolean active;

    public String getName() {
        return name;
    }

    public CourseDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CourseDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public CourseDTO setAuthorId(Integer authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public CourseDTO setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public CourseDTO setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Integer getNumberOfAssignedGroups() {
        return numberOfAssignedGroups;
    }

    public CourseDTO setNumberOfAssignedGroups(Integer numberOfAssignedGroups) {
        this.numberOfAssignedGroups = numberOfAssignedGroups;
        return this;
    }

    public List<CourseGroupDTO> getGroups() {
        return groups;
    }

    public CourseDTO setGroups(List<CourseGroupDTO> groups) {
        this.groups = groups;
        return this;
    }
}
