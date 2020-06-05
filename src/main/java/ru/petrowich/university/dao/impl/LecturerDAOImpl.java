package ru.petrowich.university.dao.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class LecturerDAOImpl extends AbstractDAO implements LecturerDAO {
    private static final String ROLE = "LECTURER";
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;
    private final Integer roleId;

    @Autowired
    public LecturerDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.roleId = jdbcTemplate.queryForObject(queries.getQuery("Role.getByName"), Integer.class, ROLE);
    }

    @Override
    public Lecturer getById(Integer lecturerId) {
        String sql = queries.getQuery("Person.getById");
        LOGGER.debug("getById: {}; lecturerId {}, roleId = {}", sql, lecturerId, roleId);

        try {
            return jdbcTemplate.queryForObject(queries.getQuery("Person.getById"),
                    (ResultSet resultSet, int rowNumber) -> getLecturer(resultSet),
                    lecturerId, roleId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("getById: {}", String.valueOf(e));
            return null;
        }
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
                    LOGGER.debug("add: {}", preparedStatement);
                    return preparedStatement;
                }, keyHolder);

        Integer studentId = (Integer) keyHolder.getKeyList().get(0).get("person_id");
        lecturer.setId(studentId);
    }

    @Override
    public void update(Lecturer lecturer) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Person.update"));
                    setNullableValue(preparedStatement, 1, lecturer.getFirstName());
                    setNullableValue(preparedStatement,2, lecturer.getLastName());
                    setNullableValue(preparedStatement,3, lecturer.getEmail());
                    setNullableValue(preparedStatement,4, lecturer.getComment());
                    preparedStatement.setBoolean(5, lecturer.isActive());
                    preparedStatement.setInt(6, lecturer.getId());
                    preparedStatement.setInt(7, roleId);
                    LOGGER.debug("update: {}", preparedStatement);
                    return preparedStatement;
                }
        );
    }

    @Override
    public void delete(Lecturer lecturer) {
        String sql = queries.getQuery("Person.delete");
        LOGGER.debug("delete: {}; lecturerId {}, roleId = {}", sql, lecturer.getId(), roleId);
        jdbcTemplate.update(sql, lecturer.getId(), roleId);
    }

    @Override
    public List<Lecturer> getAll() {
        String query = queries.getQuery("Person.getAll");
        LOGGER.debug("getAll: {}; roleId = {}", query, roleId);
        return jdbcTemplate.query(query,
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
