package ru.petrowich.university.mapper.lecturers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.lecturers.LecturerCourseDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Course;

@Component
public class LecturerCourseMapper extends AbstractMapper<Course, LecturerCourseDTO> {
    @Autowired
    public LecturerCourseMapper(ModelMapper modelMapper) {
        super(modelMapper, Course.class, LecturerCourseDTO.class);
    }
}
