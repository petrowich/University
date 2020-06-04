package ru.petrowich.university.dao.impl;

import org.slf4j.Logger;
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

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class GroupDAOImpl extends AbstractDAO implements GroupDAO {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    @Autowired
    public GroupDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    @Override
    public Group getById(Integer groupId) {
        String sql = queries.getQuery("Group.getById");
        LOGGER.info("getById: {}; groupId {}", sql, groupId);

        try {
            return jdbcTemplate.queryForObject(sql,
                    (ResultSet resultSet, int rowNumber) -> getGroup(resultSet),
                    groupId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("getById: {}", String.valueOf(e));
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
                    LOGGER.info("add: {}", preparedStatement);
                    return preparedStatement;
                }, keyHolder
        );

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
                    LOGGER.info("update: {}", preparedStatement);
                    return preparedStatement;
                }
        );
    }

    @Override
    public void delete(Group group) {
        String sql = queries.getQuery("Group.delete");
        LOGGER.info("delete: {}; groupId {}", sql, group.getId());
        jdbcTemplate.update(sql, group.getId());
    }

    @Override
    public List<Group> getAll() {
        String query = queries.getQuery("Group.getAll");
        LOGGER.info("getAll: {} ", query);
        return jdbcTemplate.query(query, (ResultSet resultSet, int rowNumber) -> getGroup(resultSet));
    }

    @Override
    public List<Group> getByCourseId(Integer courseId) {
        String query = queries.getQuery("Group.getByCourseId");
        LOGGER.info("getByCourseId: {}; courseId = {}", query, courseId);
        return jdbcTemplate.query(query,
                (ResultSet resultSet, int rowNumber) -> getGroup(resultSet),
                courseId
        );
    }

    @Override
    public List<Group> getByLessonId(Long lessonId) {
        String query = queries.getQuery("Group.getByLessonId");
        LOGGER.info("getByLessonId: {}; getByLessonId = {}", query, lessonId);
        return jdbcTemplate.query(query,
                (ResultSet resultSet, int rowNumber) -> getGroup(resultSet),
                lessonId
        );
    }

    private Group getGroup(ResultSet resultSet) throws SQLException {
        return new Group()
                .setId(resultSet.getInt("group_id"))
                .setName(resultSet.getString("group_name"))
                .setActive(resultSet.getBoolean("group_active"));
    }
}
