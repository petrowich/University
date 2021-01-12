package ru.petrowich.university.model;

import javax.persistence.Entity;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

@Entity(name = "Student")
@DiscriminatorValue("1")
public class Student extends AbstractPerson {

    @ManyToOne
    @JoinTable(
            name = "t_groups_students",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Group group = new Group();

    @Override
    public Student setId(Integer id) {
        super.setId(id);
        return this;
    }

    @Override
    public Student setActive(boolean active) {
        super.setActive(active);
        return this;
    }

    @Override
    public Student setFirstName(String firstName) {
        super.setFirstName(firstName);
        return this;
    }

    @Override
    public Student setLastName(String lastName) {
        super.setLastName(lastName);
        return this;
    }

    @Override
    public Student setEmail(String email) {
        super.setEmail(email);
        return this;
    }

    @Override
    public Student setComment(String comment) {
        super.setComment(comment);
        return this;
    }

    public Group getGroup() {
        if (group == null) {
            return new Group();
        }
        return group;
    }

    public Student setGroup(Group group) {
        this.group = group;
        return this;
    }
}
