package ru.petrowich.university.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface GenericService<T, I> {
    T getById(I id);

    @Transactional
    T add(T object);

    @Transactional
    T update(T object);

    @Transactional
    void delete(T object);

    List<T> getAll();
}
