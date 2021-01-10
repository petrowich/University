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
import javax.persistence.Transient;
import javax.persistence.GenerationType;
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

    @Column(name = "group_name")
    private String name;

    @Column(name = "group_capacity")
    private Integer capacity = 0;

    @OneToMany(mappedBy = "group")
    private List<Student> students = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "t_groups_courses",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    List<Course> courses = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "v_lessons_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    @Transient
    List<Lesson> lessons = new ArrayList<>();

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
        return this.capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Group setStudents(List<Student> students) {
        this.students = students;
        this.capacity = students.size();
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

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
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
