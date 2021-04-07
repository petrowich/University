package ru.petrowich.university.model;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "t_persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_role_id", discriminatorType = DiscriminatorType.INTEGER)
public abstract class AbstractPerson extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "seq_persons", sequenceName = "seq_persons", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_persons")
    @Column(name = "person_id", nullable = false, unique = true)
    private Integer id;

    @Size(max = 255, message = "person first name length is more than 255 characters")
    @Column(name = "person_first_name")
    private String firstName;

    @Size(max = 255, message = "person last name length is more than 255 characters")
    @Column(name = "person_last_name")
    private String lastName;

    @NotBlank(message = "person email is empty")
    @Email(message = "person email is not valid")
    @Column(name = "person_email")
    private String email;

    @Size(max = 2048, message = "person comment is more than 2048 characters")
    @Column(name = "person_comment")
    private String comment;

    @Column(name = "person_active")
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

    public String getFullName() {
        StringJoiner fullName = new StringJoiner(" ");

        if (firstName != null) {
            fullName.add(firstName);
        }

        if (lastName != null) {
            fullName.add(lastName);
        }

        return fullName.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        AbstractPerson person = (AbstractPerson) object;

        return Objects.equals(this.getId(), person.getId());
    }
}
