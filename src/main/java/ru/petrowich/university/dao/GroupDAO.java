package ru.petrowich.university.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

@Repository("groupDAO")
public class GroupDAO extends AbstractGenericDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    public GroupDAO(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    public Group getById(Integer groupId) {
        return jdbcTemplate.queryForObject(queries.getQuery("Group.getById"),
                (ResultSet resultSet, int rowNumber) -> getGroup(resultSet), groupId);
    }

    public void add(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Group.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, group.getName());
                    preparedStatement.setBoolean(2, group.isActive());
                    return preparedStatement;
                }, keyHolder);

        Integer courseId = (Integer) keyHolder.getKeyList().get(0).get("group_id");
        group.setId(courseId);
    }

    public void update(Group group) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Group.update"));
                    setNullableValue(preparedStatement, 1, group.getName());
                    preparedStatement.setBoolean(2, group.isActive());
                    preparedStatement.setInt(3, group.getId());
                    return preparedStatement;
                }
        );
    }

    public List<Group> getAll() {
        return jdbcTemplate.query(queries.getQuery("Group.getAll"),
                (ResultSet resultSet, int rowNumber) -> getGroup(resultSet)
        );
    }

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
