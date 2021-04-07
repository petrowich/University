package ru.petrowich.university.dto.students;

import ru.petrowich.university.dto.AbstractDTO;

public class GroupCourseDTO extends AbstractDTO {
    private Integer id = null;
    private String name = null;
    private Integer authorId = null;
    private String authorFullName = null;

    public Integer getId() {
        return id;
    }

    public GroupCourseDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupCourseDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }
}
