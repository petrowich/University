package ru.petrowich.university.dto.students;

import ru.petrowich.university.dto.AbstractDTO;

public class GroupStudentDTO extends AbstractDTO {
    private Integer id = null;
    private String fullName = null;

    public Integer getId() {
        return id;
    }

    public GroupStudentDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public GroupStudentDTO setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
}
