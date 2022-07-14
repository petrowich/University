package ru.petrowich.university.dto.students;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractDTO;

@Schema(description = "A course of the group")
public class GroupCourseDTO extends AbstractDTO {

    @Schema(description = "Numeric internal identifier of the course", example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id = null;

    @Schema(description = "A name of the course", example = "Mathematical analysis",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String name = null;

    @Schema(description = "Numeric internal identifier of the lecturer is teaching the course",
            example = "10001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer authorId = null;

    @Schema(description = "A lecturer full name", example = "Giorgio Parisi",
            accessMode = Schema.AccessMode.READ_ONLY)
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
