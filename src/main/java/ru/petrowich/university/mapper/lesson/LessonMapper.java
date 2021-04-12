package ru.petrowich.university.mapper.lesson;

import org.modelmapper.ModelMapper;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.lessons.LessonDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.Student;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class LessonMapper extends AbstractMapper<Lesson, LessonDTO> {
    private final ModelMapper modelMapper;

    @Autowired
    public LessonMapper(ModelMapper modelMapper) {
        super(modelMapper, Lesson.class, LessonDTO.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Lesson.class, LessonDTO.class)
                .addMappings(propertyMap -> propertyMap.skip(LessonDTO::setNumberOfAttendees)).setPostConverter(toDtoConverter());
    }

    @Override
    public void mapSpecificFields(Lesson lesson, LessonDTO lessonDTO) {
        Set<Student> students = new HashSet<>();
        lesson.getCourse().getGroups().forEach(group -> students.addAll(group.getStudents()));
        lessonDTO.setNumberOfAttendees(students.size());
    }
}
