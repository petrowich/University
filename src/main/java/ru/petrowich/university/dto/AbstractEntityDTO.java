package ru.petrowich.university.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public abstract class AbstractEntityDTO<I extends Number> extends AbstractDTO {

    @Schema(description = "Internal numeric identifier", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private I id;

    public I getId() {
        return id;
    }

    public AbstractEntityDTO<I> setId(I id) {
        this.id = id;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        AbstractEntityDTO<I> dto = (AbstractEntityDTO<I>) object;

        return Objects.equals(this.getId(), dto.getId());
    }
}
