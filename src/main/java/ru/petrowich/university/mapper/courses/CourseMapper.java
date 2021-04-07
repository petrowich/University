package ru.petrowich.university.mapper.courses;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.courses.CourseDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Course;

import javax.annotation.PostConstruct;

@Component
public class CourseMapper extends AbstractMapper<Course, CourseDTO> {
    private final ModelMapper modelMapper;

    @Autowired
    public CourseMapper(ModelMapper modelMapper) {
        super(modelMapper, Course.class, CourseDTO.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Course.class, CourseDTO.class)
                .addMappings(m -> m.skip(CourseDTO::setNumberOfAssignedGroups)).setPostConverter(toDtoConverter());
    }

    @Override
    public void mapSpecificFields(Course course, CourseDTO courseDTO) {
        courseDTO.setNumberOfAssignedGroups(course.getGroups().size());
    }
}
