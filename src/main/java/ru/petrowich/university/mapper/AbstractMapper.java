package ru.petrowich.university.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.petrowich.university.dto.AbstractDTO;
import ru.petrowich.university.model.AbstractEntity;

import java.util.Objects;

public abstract class AbstractMapper<E extends AbstractEntity, D extends AbstractDTO> implements Mapper<E, D> {
    private final ModelMapper modelMapper;
    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    @Autowired
    public AbstractMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public E toEntity(D dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        return modelMapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        return modelMapper.map(entity, dtoClass);
    }

    public Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    public void mapSpecificFields(E source, D destination) {
    }

    public void mapSpecificFields(D source, E destination) {
    }
}
