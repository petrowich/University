package ru.petrowich.university.mapper;

import ru.petrowich.university.dto.AbstractDTO;
import ru.petrowich.university.model.AbstractEntity;

public interface Mapper<E extends AbstractEntity, D extends AbstractDTO> {

    E toEntity(D dto);

    D toDto(E entity);
}
