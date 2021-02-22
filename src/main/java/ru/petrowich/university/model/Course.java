package ru.petrowich.university.model;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinTable;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Course")
@Table(name = "t_courses")
public class Course {
    @Id
    @SequenceGenerator(name = "seq_courses", sequenceName = "seq_courses", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_courses")
    @Column(name = "course_id")
    private Integer id;

    @NotBlank(message = "course name is empty")
    @Size(max=255, message = "course name length is more than 255 characters")
    @Column(name = "course_name")
    private String name;

    @Size(max=2048, message = "description is more than 2048 characters")
    @Column(name = "course_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_author_id", referencedColumnName = "person_id")
    private Lecturer author = new Lecturer();

    @ManyToMany
    @JoinTable(name = "t_groups_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    List<Group> groups = new ArrayList<>();

    @Column(name = "course_active")
    private boolean active;

    public Integer getId() {
        return id;
    }

    public Course setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Course setDescription(String description) {
        this.description = description;
        return this;
    }

    public Lecturer getAuthor() {
        if (author == null) {
            return new Lecturer();
        }
        return author;
    }

    public Course setAuthor(Lecturer author) {
        this.author = author;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Course setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Course setGroups(List<Group> groups) {
        this.groups = groups;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Course course = (Course) object;

        return Objects.equals(this.getId(), course.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
