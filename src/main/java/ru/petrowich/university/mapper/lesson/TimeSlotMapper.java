package ru.petrowich.university.mapper.lesson;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.petrowich.university.dto.lessons.TimeSlotDTO;
import ru.petrowich.university.mapper.AbstractMapper;
import ru.petrowich.university.model.TimeSlot;

@Component
public class TimeSlotMapper extends AbstractMapper<TimeSlot, TimeSlotDTO> {
    @Autowired
    public TimeSlotMapper(ModelMapper modelMapper) {
        super(modelMapper, TimeSlot.class, TimeSlotDTO.class);
    }
}
