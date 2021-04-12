package ru.petrowich.university.dto.courses;

import ru.petrowich.university.dto.AbstractDTO;

public class CourseGroupDTO extends AbstractDTO {
    private Integer id = null;
    private String name = null;

    public Integer getId() {
        return id;
    }

    public CourseGroupDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CourseGroupDTO setName(String name) {
        this.name = name;
        return this;
    }
}
