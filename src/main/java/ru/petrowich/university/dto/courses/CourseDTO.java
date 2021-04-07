package ru.petrowich.university.dto.courses;

import ru.petrowich.university.dto.AbstractDTO;

import java.util.ArrayList;
import java.util.List;

public class CourseDTO extends AbstractDTO {
    private Integer id = null;
    private String name = null;
    private String description = null;
    private Integer authorId = null;
    private String authorFullName = null;
    private Integer numberOfAssignedGroups = null;
    private transient List<CourseGroupDTO> groups = new ArrayList<>();
    boolean active;

    public Integer getId() {
        return id;
    }

    public CourseDTO setId(Integer id) {
        this.id = id;
        return this;
    }

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
