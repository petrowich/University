package ru.petrowich.university.mapper.students;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.students.GroupDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Group;

import javax.annotation.PostConstruct;

@Component
public class GroupMapper extends AbstractMapper<Group, GroupDTO> {
    private final ModelMapper modelMapper;

    @Autowired
    public GroupMapper(ModelMapper modelMapper) {
        super(modelMapper, Group.class, GroupDTO.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Group.class, GroupDTO.class)
                .addMappings(m -> m.skip(GroupDTO::setNumberOfAssignedCourses))
                .addMappings(m -> m.skip(GroupDTO::setNumberOfStudents))
                .setPostConverter(toDtoConverter());
    }

    @Override
    public void mapSpecificFields(Group group, GroupDTO groupDTO) {
        groupDTO.setNumberOfAssignedCourses(group.getCourses().size());
        groupDTO.setNumberOfStudents(group.getStudents().size());
    }
}
