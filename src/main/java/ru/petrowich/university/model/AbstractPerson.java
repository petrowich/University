package ru.petrowich.university.model;

import java.util.Objects;

public abstract class AbstractPerson {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String comment;
    private boolean active;

    public Integer getId() {
        return id;
    }

    public AbstractPerson setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AbstractPerson setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AbstractPerson setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AbstractPerson setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public AbstractPerson setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public AbstractPerson setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
