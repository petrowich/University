package ru.petrowich.university.dto.students;

import ru.petrowich.university.dto.AbstractDTO;

public class StudentCourseDTO extends AbstractDTO {
    private Integer id = null;
    private String name = null;

    public Integer getId() {
        return id;
    }

    public StudentCourseDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StudentCourseDTO setName(String name) {
        this.name = name;
        return this;
    }
}
