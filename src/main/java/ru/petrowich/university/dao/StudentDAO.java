package ru.petrowich.university.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

@Repository("studentDAO")
public class StudentDAO {
    private static final String ROLE = "STUDENT";
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;
    private final Integer roleId;

    public StudentDAO(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.roleId = jdbcTemplate.queryForObject(queries.getQuery("Role.getByName"), Integer.class, ROLE);
    }

    public Student getById(Integer studentId) {
        return jdbcTemplate.queryForObject(queries.getQuery("Student.getById"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                studentId,
                roleId);
    }

    public void add(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Person.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, student.getFirstName());
                    preparedStatement.setString(2, student.getLastName());
                    preparedStatement.setInt(3, roleId);
                    preparedStatement.setString(4, student.getEmail());
                    preparedStatement.setString(5, student.getComment());
                    preparedStatement.setBoolean(6, student.isActive());
                    return preparedStatement;
                }, keyHolder);

        Integer studentId = (Integer) keyHolder.getKeyList().get(0).get("person_id");
        student.setId(studentId);

        if (student.getGroup() != null && student.getGroup().getId() != null) {
            setStudentGroupId(student.getGroup().getId(), student.getId());
        }
    }

    public void update(Student student) {
        jdbcTemplate.update(queries.getQuery("Person.update"),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getComment(),
                student.isActive(),
                student.getId(),
                roleId
        );

        deleteStudentGroupId(student.getId());

        if (student.getGroup() != null && student.getGroup().getId() != null) {
            setStudentGroupId(student.getGroup().getId(), student.getId());
        }
    }

    public List<Student> getAll() {
        return jdbcTemplate.query(queries.getQuery("Student.getAll"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                roleId
        );
    }

    public List<Student> getByGroupId(Integer groupId) {
        return jdbcTemplate.query(queries.getQuery("Student.getByGroupId"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                groupId,
                roleId
        );
    }

    public List<Student> getByGroupsCourseId(Integer courseId) {
        return jdbcTemplate.query(queries.getQuery("Student.getByGroupsCourseId"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                courseId,
                roleId
        );
    }

    private Student getStudent(ResultSet resultSet) throws SQLException {
        Student student = new Student()
                .setId(resultSet.getInt("person_id"))
                .setFirstName(resultSet.getString("person_first_name"))
                .setLastName(resultSet.getString("person_last_name"))
                .setEmail(resultSet.getString("person_email"))
                .setComment(resultSet.getString("person_comment"))
                .setActive(resultSet.getBoolean("person_active"));

        Integer groupId = resultSet.getInt("group_id");

        if (!resultSet.wasNull()) {
            student.setGroup(new Group().setId(groupId));
        }

        return student;
    }

    private void deleteStudentGroupId(Integer studentId) {
        this.jdbcTemplate.update(queries.getQuery("Student.deleteStudentGroupId"), studentId);
    }

    private void setStudentGroupId(Integer groupId, Integer studentId) {
        this.jdbcTemplate.update(queries.getQuery("Student.addStudentGroupId"), groupId, studentId);
    }
}
