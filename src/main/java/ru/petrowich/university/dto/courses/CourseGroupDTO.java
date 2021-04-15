package ru.petrowich.university.dto.courses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractDTO;

@Schema(description = "A group assigned to the course")
public class CourseGroupDTO extends AbstractDTO {

    @Schema(description = "Numeric internal identifier of the group", example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id = null;

    @Schema(description = "Numeric internal identifier of the group", example = "AA-01",
            accessMode = Schema.AccessMode.READ_ONLY)
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
