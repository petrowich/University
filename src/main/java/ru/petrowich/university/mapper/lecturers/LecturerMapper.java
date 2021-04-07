package ru.petrowich.university.mapper.lecturers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.lecturers.LecturerDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.Lecturer;

@Component
public class LecturerMapper extends AbstractMapper<Lecturer, LecturerDTO> {
    @Autowired
    public LecturerMapper(ModelMapper modelMapper) {
        super(modelMapper, Lecturer.class, LecturerDTO.class);
    }
}
