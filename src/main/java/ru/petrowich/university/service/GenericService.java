package ru.petrowich.university.service;

import java.util.List;

public interface GenericService<T, I> {
    T getById(I id);

    void add(T object);

    void update(T object);

    void delete(T object);

    List<T> getAll();
}
