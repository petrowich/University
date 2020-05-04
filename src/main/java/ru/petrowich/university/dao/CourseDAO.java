package ru.petrowich.university.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

@Repository("courseDAO")
public class CourseDAO extends AbstractGenericDAO {

    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    public CourseDAO(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    public Course getById(Integer courseId) {
        return jdbcTemplate.queryForObject(queries.getQuery("Course.getById"),
                (ResultSet resultSet, int rowNumber) -> new Course()
                        .setId(courseId)
                        .setName(resultSet.getString("course_name"))
                        .setDescription(resultSet.getString("course_description"))
                        .setAuthor(new Lecturer().setId(resultSet.getInt("course_author_id")))
                        .setActive(resultSet.getBoolean("course_active")),
                courseId);
    }

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
                    return preparedStatement;
                }, keyHolder);

        Integer courseId = (Integer) keyHolder.getKeyList().get(0).get("course_id");
        course.setId(courseId);
    }

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
                    return preparedStatement;
                }
        );
    }

    public List<Course> getAll() {
        return jdbcTemplate.query(queries.getQuery("Course.getAll"),
                (ResultSet resultSet, int rowNumber) -> getCourse(resultSet)
        );
    }

    public List<Course> getByAuthorId(Integer authorId) {
        return jdbcTemplate.query(queries.getQuery("Course.getByAuthorId"),
                (ResultSet resultSet, int rowNumber) -> getCourse(resultSet),
                authorId
        );
    }

    public List<Course> getByStudentId(Integer studentId) {
        return jdbcTemplate.query(queries.getQuery("Course.getByStudentId"),
                (ResultSet resultSet, int rowNumber) -> getCourse(resultSet), studentId
        );
    }

    private Course getCourse(ResultSet resultSet) throws SQLException {
        return new Course()
                .setId(resultSet.getInt("course_id"))
                .setName(resultSet.getString("course_name"))
                .setDescription(resultSet.getString("course_description"))
                .setAuthor(new Lecturer().setId(resultSet.getInt("course_author_id")))
                .setActive(resultSet.getBoolean("course_active"));
    }
}
