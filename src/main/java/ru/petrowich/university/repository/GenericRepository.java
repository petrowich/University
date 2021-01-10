package ru.petrowich.university.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericRepository<T, I> {
    T findById(I id);

    void save(T object);

    void update(T object);

    void delete(T object);

    List<T> findAll();
}
