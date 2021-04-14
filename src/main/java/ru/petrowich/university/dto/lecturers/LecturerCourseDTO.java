package ru.petrowich.university.dto.lecturers;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractDTO;

@Schema(description = "A course taught by the lecturer")
public class LecturerCourseDTO extends AbstractDTO {

    @Schema(description = "Numeric internal identifier of the course taught by the lecturer", example = "10001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id = null;

    @Schema(description = "A name of the course", example = "Mathematical analysis", accessMode = Schema.AccessMode.READ_ONLY)
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
