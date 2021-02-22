package ru.petrowich.university.model;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinTable;
import javax.persistence.GenerationType;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Group")
@Table(name = "t_groups")
public class Group {
    @Id
    @SequenceGenerator(name="seq_groups", sequenceName="seq_groups", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_groups")
    @Column(name = "group_id")
    private Integer id;

    @NotBlank(message = "group name is empty")
    @Size(max=255, message = "group name length is more than 255 characters")
    @Column(name = "group_name")
    private String name;

    @Min(value=1, message = "group capacity is less than 1")
    @Max(value=100,  message = "group capacity exceed 100")
    @Column(name = "group_capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "t_groups_courses",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    List<Course> courses = new ArrayList<>();

    @Column(name = "group_active")
    private boolean active;

    public Integer getId() {
        return id;
    }

    public Group setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Group setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Group setStudents(List<Student> students) {
        this.students = students;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Group setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Group setCourses(List<Course> courses) {
        this.courses = courses;
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

        Group group = (Group) object;

        return Objects.equals(this.getId(), group.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
