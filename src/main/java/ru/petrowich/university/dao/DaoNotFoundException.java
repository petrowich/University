package ru.petrowich.university.dao;

public class DaoNotFoundException extends RuntimeException  {
    public DaoNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
