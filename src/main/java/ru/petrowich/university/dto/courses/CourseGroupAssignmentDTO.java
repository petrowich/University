package ru.petrowich.university.dto.courses;

import ru.petrowich.university.dto.AbstractDTO;

public class CourseGroupAssignmentDTO extends AbstractDTO {
    private Integer courseId = null;
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
