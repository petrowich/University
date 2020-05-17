package ru.petrowich.university.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.dao.AbstractDAO;
import ru.petrowich.university.dao.GroupDAO;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GroupDAOImpl extends AbstractDAO implements GroupDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    @Override
    public Group getById(Integer groupId) {
        try {
            return jdbcTemplate.queryForObject(queries.getQuery("Group.getById"),
                    (ResultSet resultSet, int rowNumber) -> getGroup(resultSet), groupId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void add(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Group.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, group.getName());
                    preparedStatement.setInt(2, group.getStudents().size());
                    preparedStatement.setBoolean(3, group.isActive());
                    return preparedStatement;
                }, keyHolder);

        Integer courseId = (Integer) keyHolder.getKeyList().get(0).get("group_id");
        group.setId(courseId);
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Group.update"));
                    setNullableValue(preparedStatement, 1, group.getName());
                    preparedStatement.setInt(2, group.getStudents().size());
                    preparedStatement.setBoolean(3, group.isActive());
                    preparedStatement.setInt(4, group.getId());
                    return preparedStatement;
                }
        );
    }

    @Override
    public void delete(Group group) {
        jdbcTemplate.update(queries.getQuery("Group.delete"), group.getId());
    }

    @Override
    public List<Group> getAll() {
        return jdbcTemplate.query(queries.getQuery("Group.getAll"),
                (ResultSet resultSet, int rowNumber) -> getGroup(resultSet)
        );
    }

    @Override
    public List<Group> getByCourseId(Integer courseId) {
        return jdbcTemplate.query(queries.getQuery("Group.getByCourseId"),
                (ResultSet resultSet, int rowNumber) -> getGroup(resultSet), courseId
        );
    }

    private Group getGroup(ResultSet resultSet) throws SQLException {
        return new Group()
                .setId(resultSet.getInt("group_id"))
                .setName(resultSet.getString("group_name"))
                .setActive(resultSet.getBoolean("group_active"));
    }
}
