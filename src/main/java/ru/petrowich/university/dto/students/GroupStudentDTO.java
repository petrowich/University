package ru.petrowich.university.dto.students;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractDTO;

@Schema(description = "A student of the group")
public class GroupStudentDTO extends AbstractDTO {

    @Schema(description = "Numeric internal identifier of the student", example = "10001",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id = null;

    @Schema(description = "A student full name", example = "Klaus Hasselmann",
            accessMode = Schema.AccessMode.READ_ONLY)
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
