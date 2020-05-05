package ru.petrowich.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.dao.AbstractDAO;
import ru.petrowich.university.dao.LecturerDAO;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LecturerDAOImpl extends AbstractDAO implements LecturerDAO {
    private static final String ROLE = "LECTURER";
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;
    private final Integer roleId;

    public LecturerDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.roleId = jdbcTemplate.queryForObject(queries.getQuery("Role.getByName"), Integer.class, ROLE);
    }

    @Override
    public Lecturer getById(Integer lecturerId) {
        return jdbcTemplate.queryForObject(queries.getQuery("Person.getById"),
                (ResultSet resultSet, int rowNumber) -> getLecturer(resultSet),
                lecturerId,
                roleId);
    }

    @Override
    public void add(Lecturer lecturer) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Person.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, lecturer.getFirstName());
                    preparedStatement.setString(2, lecturer.getLastName());
                    preparedStatement.setInt(3, roleId);
                    preparedStatement.setString(4, lecturer.getEmail());
                    preparedStatement.setString(5, lecturer.getComment());
                    preparedStatement.setBoolean(6, lecturer.isActive());
                    return preparedStatement;
                }, keyHolder);

        Integer studentId = (Integer) keyHolder.getKeyList().get(0).get("person_id");
        lecturer.setId(studentId);
    }

    @Override
    public void update(Lecturer lecturer) {
        jdbcTemplate.update(queries.getQuery("Person.update"),
                lecturer.getFirstName(),
                lecturer.getLastName(),
                lecturer.getEmail(),
                lecturer.getComment(),
                lecturer.isActive(),
                lecturer.getId(),
                roleId
        );
    }

    @Override
    public void delete(Lecturer lecturer) {
        jdbcTemplate.update(queries.getQuery("Person.delete"), lecturer.getId());
    }

    @Override
    public List<Lecturer> getAll() {
        return jdbcTemplate.query(queries.getQuery("Person.getAll"),
                (ResultSet resultSet, int rowNumber) -> getLecturer(resultSet),
                roleId
        );
    }

    private Lecturer getLecturer(ResultSet resultSet) throws SQLException {
        return new Lecturer()
                .setId(resultSet.getInt("person_id"))
                .setFirstName(resultSet.getString("person_first_name"))
                .setLastName(resultSet.getString("person_last_name"))
                .setEmail(resultSet.getString("person_email"))
                .setComment(resultSet.getString("person_comment"))
                .setActive(resultSet.getBoolean("person_active"));
    }
}
