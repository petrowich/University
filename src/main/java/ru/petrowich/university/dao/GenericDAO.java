package ru.petrowich.university.dao;

import java.util.List;

public interface GenericDAO<T, I> {
    T getById(I id);

    void add(T object);

    void update(T object);

    void delete(T object);

    List<T> getAll();
}
