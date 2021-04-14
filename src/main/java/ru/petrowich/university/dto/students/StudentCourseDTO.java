package ru.petrowich.university.dto.students;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractDTO;

@Schema(description = "A course of the student")
public class StudentCourseDTO extends AbstractDTO {

    @Schema(description = "Numeric internal identifier of the course", example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id = null;

    @Schema(description = "A course name", example = "Mathematical analysis", accessMode = Schema.AccessMode.READ_ONLY)
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
