package ru.petrowich.university.dao.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.dao.AbstractDAO;
import ru.petrowich.university.dao.CourseDAO;
import ru.petrowich.university.dao.DaoException;
import ru.petrowich.university.model.Course;
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
public class CourseDAOImpl extends AbstractDAO implements CourseDAO {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    @Autowired
    public CourseDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    @Override
    public Course getById(Integer courseId) {
        String sql = queries.getQuery("Course.getById");
        LOGGER.debug("getById: {}; courseId {}", sql, courseId);

        try {
            return jdbcTemplate.queryForObject(sql,
                    (ResultSet resultSet, int rowNumber) -> getCourse(resultSet), courseId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("nonexistent courseId {} was passed", courseId);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void add(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Course.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, course.getName());
                    setNullableValue(preparedStatement, 2, course.getDescription());
                    setNullableValue(preparedStatement, 3, course.getAuthor().getId());
                    preparedStatement.setBoolean(4, course.isActive());
                    LOGGER.debug("add: {}", preparedStatement);
                    return preparedStatement;
                }, keyHolder
        );

        Integer courseId = (Integer) keyHolder.getKeyList().get(0).get("course_id");
        course.setId(courseId);
    }

    @Override
    public void update(Course course) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Course.update"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, course.getName());
                    setNullableValue(preparedStatement, 2, course.getDescription());
                    setNullableValue(preparedStatement, 3, course.getAuthor().getId());
                    preparedStatement.setBoolean(4, course.isActive());
                    preparedStatement.setInt(5, course.getId());
                    LOGGER.debug("update: {}", preparedStatement);
                    return preparedStatement;
                }
        );
    }

    @Override
    public void delete(Course course) {
        String sql = queries.getQuery("Course.delete");
        LOGGER.debug("delete: {}; courseId {}", sql, course.getId());
        jdbcTemplate.update(sql, course.getId());
    }

    @Override
    public List<Course> getAll() {
        String query = queries.getQuery("Course.getAll");
        LOGGER.debug("getAll: {}", query);
        return jdbcTemplate.query(query, (ResultSet resultSet, int rowNumber) -> getCourse(resultSet));
    }

    @Override
    public List<Course> getByAuthorId(Integer authorId) {
        String query = queries.getQuery("Course.getByAuthorId");
        LOGGER.debug("getByAuthorId: {}; authorId = {}", query, authorId);
        return jdbcTemplate.query(query,
                (ResultSet resultSet, int rowNumber) -> getCourse(resultSet),
                authorId
        );
    }

    @Override
    public List<Course> getByStudentId(Integer studentId) {
        String query = queries.getQuery("Course.getByStudentId");
        LOGGER.debug("getByStudentId: {}; studentId = {}", query, studentId);
        return jdbcTemplate.query(query,
                (ResultSet resultSet, int rowNumber) -> getCourse(resultSet),
                studentId
        );
    }

    private Course getCourse(ResultSet resultSet) throws SQLException {
        Course course = new Course()
                .setId(resultSet.getInt("course_id"))
                .setName(resultSet.getString("course_name"))
                .setDescription(resultSet.getString("course_description"))
                .setActive(resultSet.getBoolean("course_active"));

        Integer authorId = resultSet.getInt("course_author_id");

        if (!resultSet.wasNull()) {
            course.setAuthor(new Lecturer().setId(authorId));
        }

        return course;
    }
}
