package ru.petrowich.university.dao.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.dao.AbstractDAO;
import ru.petrowich.university.dao.StudentDAO;
import ru.petrowich.university.model.Group;
import ru.petrowich.university.model.Student;
import ru.petrowich.university.util.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class StudentDAOImpl extends AbstractDAO implements StudentDAO {
    private static final String ROLE = "STUDENT";
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;
    private final Integer roleId;

    @Autowired
    public StudentDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.roleId = jdbcTemplate.queryForObject(queries.getQuery("Role.getByName"), Integer.class, ROLE);
    }

    @Override
    public Student getById(Integer studentId) {
        String sql = queries.getQuery("Student.getById");
        LOGGER.info("getById: {}; studentId = {}, roleId = {}", sql, studentId, roleId);

        try {
            return jdbcTemplate.queryForObject(sql,
                    (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                    studentId, roleId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warn("getById: {}", String.valueOf(e));
            return null;
        }
    }

    @Override
    public void add(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Person.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, student.getFirstName());
                    setNullableValue(preparedStatement, 2, student.getLastName());
                    preparedStatement.setInt(3, roleId);
                    setNullableValue(preparedStatement, 4, student.getEmail());
                    setNullableValue(preparedStatement, 5, student.getComment());
                    preparedStatement.setBoolean(6, student.isActive());
                    LOGGER.info("add: {}", preparedStatement);
                    return preparedStatement;
                }, keyHolder);

        Integer studentId = (Integer) keyHolder.getKeyList().get(0).get("person_id");
        student.setId(studentId);

        if (student.getGroup() != null && student.getGroup().getId() != null) {
            setStudentGroupId(student.getGroup().getId(), student.getId());
        }
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Person.update"));
                    setNullableValue(preparedStatement, 1, student.getFirstName());
                    setNullableValue(preparedStatement, 2, student.getLastName());
                    setNullableValue(preparedStatement, 3, student.getEmail());
                    setNullableValue(preparedStatement, 4, student.getComment());
                    preparedStatement.setBoolean(5, student.isActive());
                    preparedStatement.setInt(6, student.getId());
                    preparedStatement.setInt(7, roleId);
                    LOGGER.info("update: {}", preparedStatement);
                    return preparedStatement;
                }
        );

        deleteStudentGroupId(student.getId());

        if (student.getGroup() != null && student.getGroup().getId() != null) {
            setStudentGroupId(student.getGroup().getId(), student.getId());
        }
    }

    @Override
    public void delete(Student student) {
        String sql = queries.getQuery("Person.delete");
        LOGGER.info("delete: {}; studentId = {}, roleId = {}", sql, student.getId(), roleId);
        jdbcTemplate.update(sql, student.getId(), roleId);
    }

    @Override
    public List<Student> getAll() {
        String query = queries.getQuery("Student.getAll");
        LOGGER.info("getAll: {}; roleId = {}", query, roleId);
        return jdbcTemplate.query(queries.getQuery("Student.getAll"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                roleId
        );
    }

    @Override
    public List<Student> getByGroupId(Integer groupId) {
        String query = queries.getQuery("Student.getByGroupId");
        LOGGER.info("getByGroupId: {}; groupId = {}, roleId = {}", query, groupId, roleId);
        return jdbcTemplate.query(queries.getQuery("Student.getByGroupId"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                groupId,
                roleId
        );
    }

    @Override
    public List<Student> getByCourseId(Integer courseId) {
        String query = queries.getQuery("Student.getByCourseId");
        LOGGER.info("getByCourseId: {}; courseId = {}, roleId = {}", query, courseId, roleId);
        return jdbcTemplate.query(queries.getQuery("Student.getByCourseId"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                courseId,
                roleId
        );
    }

    @Override
    public List<Student> getByLessonId(Long lessonId) {
        String query = queries.getQuery("Student.getByLessonId");
        LOGGER.info("getByLessonId: {}; lessonId = {}, roleId = {}", query, lessonId, roleId);
        return jdbcTemplate.query(queries.getQuery("Student.getByLessonId"),
                (ResultSet resultSet, int rowNumber) -> getStudent(resultSet),
                lessonId,
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
        String query = queries.getQuery("Student.deleteStudentGroupId");
        LOGGER.info("deleteStudentGroupId: {}; studentId = {}", query, studentId);
        this.jdbcTemplate.update(query, studentId);
    }

    private void setStudentGroupId(Integer groupId, Integer studentId) {
        String query = queries.getQuery("Student.addStudentGroupId");
        LOGGER.info("addStudentGroupId: {}; groupId = {}, studentId = {}", query, groupId, studentId);
        this.jdbcTemplate.update(query, groupId, studentId);
    }
}
