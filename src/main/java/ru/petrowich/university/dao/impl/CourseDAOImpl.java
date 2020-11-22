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
import ru.petrowich.university.model.Group;
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
    private static final String ERROR_MSG_COURSE_ID_IS_NULL = "Null course ID passed";
    private static final String ERROR_MSG_GROUP_ID_IS_NULL = "Null group ID passed";
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
        String query = queries.getQuery("Course.getById");
        LOGGER.debug("getById: {}; courseId {}", query, courseId);

        try {
            return jdbcTemplate.queryForObject(query,
                    (ResultSet resultSet, int rowNumber) -> getCourse(resultSet), courseId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("nonexistent courseId {} was passed", courseId);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void add(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = queries.getQuery("Course.add");

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, course.getName());
                    setNullableValue(preparedStatement, 2, course.getDescription());
                    setNullableValue(preparedStatement, 3, course.getAuthor().getId());
                    preparedStatement.setBoolean(4, course.isActive());
                    LOGGER.debug("add: {}", preparedStatement);
                    return preparedStatement;
                }, keyHolder);

        Integer courseId = (Integer) keyHolder.getKeyList().get(0).get("course_id");
        course.setId(courseId);
    }

    @Override
    public void update(Course course) {
        String query = queries.getQuery("Course.update");

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, course.getName());
                    setNullableValue(preparedStatement, 2, course.getDescription());
                    setNullableValue(preparedStatement, 3, course.getAuthor().getId());
                    preparedStatement.setBoolean(4, course.isActive());
                    preparedStatement.setInt(5, course.getId());
                    LOGGER.debug("update: {}", preparedStatement);
                    return preparedStatement;
                });
    }

    @Override
    public void delete(Course course) {
        String query = queries.getQuery("Course.delete");
        LOGGER.debug("delete: {}; courseId {}", query, course.getId());
        jdbcTemplate.update(query, course.getId());
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
        return jdbcTemplate.query(query, (ResultSet resultSet, int rowNumber) -> getCourse(resultSet), authorId);
    }

    @Override
    public List<Course> getByStudentId(Integer studentId) {
        String query = queries.getQuery("Course.getByStudentId");
        LOGGER.debug("getByStudentId: {}; studentId = {}", query, studentId);
        return jdbcTemplate.query(query, (ResultSet resultSet, int rowNumber) -> getCourse(resultSet), studentId);
    }

    @Override
    public List<Course> getByGroupId(Integer groupId) {
        String query = queries.getQuery("Course.getByGroupId");
        LOGGER.debug("getByGroupId: {}; groupId = {}", query, groupId);
        return jdbcTemplate.query(query, (ResultSet resultSet, int rowNumber) -> getCourse(resultSet), groupId);
    }

    @Override
    public void assignGroupToCourse(Group group, Course course) {
        if (group.getId() == null) {
            throw new NullPointerException(ERROR_MSG_GROUP_ID_IS_NULL);
        }

        if (course.getId() == null) {
            throw new NullPointerException(ERROR_MSG_COURSE_ID_IS_NULL);
        }

        assignGroupToCourseById(group.getId(), course.getId());
    }

    @Override
    public void removeGroupFromCourse(Group group, Course course) {
        if (group.getId() == null) {
            throw new NullPointerException(ERROR_MSG_GROUP_ID_IS_NULL);
        }

        if (course.getId() == null) {
            throw new NullPointerException(ERROR_MSG_COURSE_ID_IS_NULL);
        }

        removeGroupFromCourseById(group.getId(), course.getId());
    }

    @Override
    public void assignGroupsToCourse(List<Group> groups, Course course) {
        if (course.getId() == null) {
            throw new NullPointerException(ERROR_MSG_COURSE_ID_IS_NULL);
        }

        groups.forEach(group -> {
            if (group.getId() == null) {
                throw new NullPointerException(ERROR_MSG_GROUP_ID_IS_NULL);
            }
        });

        groups.forEach(group -> assignGroupToCourseById(group.getId(), course.getId()));
    }

    @Override
    public void removeGroupsFromCourse(List<Group> groups, Course course) {
        if (course.getId() == null) {
            throw new NullPointerException(ERROR_MSG_COURSE_ID_IS_NULL);
        }

        groups.forEach(group -> {
            if (group.getId() == null) {
                throw new NullPointerException(ERROR_MSG_GROUP_ID_IS_NULL);
            }
        });

        groups.forEach(group -> removeGroupFromCourseById(group.getId(), course.getId()));
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

    private void assignGroupToCourseById(Integer groupId, Integer courseId) {
        String query = queries.getQuery("GroupCourse.add");
        LOGGER.debug("GroupCourse.add: {}; courseId = {}; groupId = {};", query, courseId, groupId);
        jdbcTemplate.update(query, groupId, courseId, groupId, courseId);
    }

    private void removeGroupFromCourseById(Integer groupId, Integer courseId) {
        String query = queries.getQuery("GroupCourse.delete");
        LOGGER.debug("GroupCourse.delete: {}; groupId = {}; courseId = {};", query, groupId, courseId);
        jdbcTemplate.update(query, groupId, courseId);
    }
}
