package ru.petrowich.university.mapper.students;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.students.StudentCourseDTO;
import ru.petrowich.university.dto.students.StudentDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Student;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper extends AbstractMapper<Student, StudentDTO> {
    private final ModelMapper modelMapper;

    @Autowired
    public StudentMapper(ModelMapper modelMapper) {
        super(modelMapper, Student.class, StudentDTO.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Student.class, StudentDTO.class)
                .addMappings(propertyMap -> propertyMap.skip(StudentDTO::setCourses))
                .addMappings(propertyMap -> propertyMap.skip(StudentDTO::setNumberOfAssignedCourses))
                .setPostConverter(toDtoConverter());
    }

    @Override
    public void mapSpecificFields(Student student, StudentDTO studentDTO) {
        List<StudentCourseDTO> courseDTOList = student.getGroup().getCourses().stream()
                .map(course -> modelMapper.map(course, StudentCourseDTO.class))
                .collect(Collectors.toList());
        studentDTO.setCourses(courseDTOList);
        studentDTO.setNumberOfAssignedCourses(courseDTOList.size());
    }
}
