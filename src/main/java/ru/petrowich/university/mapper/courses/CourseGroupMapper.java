package ru.petrowich.university.mapper.courses;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.courses.CourseGroupDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Group;

@Component
public class CourseGroupMapper extends AbstractMapper<Group, CourseGroupDTO> {
    @Autowired
    public CourseGroupMapper(ModelMapper modelMapper) {
        super(modelMapper, Group.class, CourseGroupDTO.class);
    }
}
