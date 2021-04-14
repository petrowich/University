package ru.petrowich.university.dto.courses;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.petrowich.university.dto.AbstractDTO;

@Schema(description = "A bind of a group to the course")
public class CourseGroupAssignmentDTO extends AbstractDTO {

    @Schema(description = "Numeric internal identifier of the course", example = "11", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Integer courseId = null;

    @Schema(description = "Numeric internal identifier of the group", example = "101", accessMode = Schema.AccessMode.WRITE_ONLY)
    private Integer groupId = null;

    public Integer getCourseId() {
        return courseId;
    }

    public CourseGroupAssignmentDTO setCourseId(Integer courseId) {
        this.courseId = courseId;
        return this;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public CourseGroupAssignmentDTO setGroupId(Integer groupId) {
        this.groupId = groupId;
        return this;
    }
}
