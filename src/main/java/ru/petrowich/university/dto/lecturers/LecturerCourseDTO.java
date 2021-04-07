package ru.petrowich.university.dto.lecturers;

import ru.petrowich.university.dto.AbstractDTO;

public class LecturerCourseDTO extends AbstractDTO {
    private Integer id = null;
    private String name = null;

    public Integer getId() {
        return id;
    }

    public LecturerCourseDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public LecturerCourseDTO setName(String name) {
        this.name = name;
        return this;
    }
}
