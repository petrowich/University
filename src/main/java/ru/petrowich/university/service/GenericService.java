package ru.petrowich.university.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface GenericService<T, I> {

    T getById(I id);

    @Transactional
    void add(T object);

    @Transactional
    void update(T object);

    @Transactional
    void delete(T object);

    List<T> getAll();
}
