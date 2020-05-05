package ru.petrowich.university.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.dao.AbstractDAO;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lesson;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.util.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;

@Repository
public class LessonDAOImpl extends AbstractDAO implements LessonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    public LessonDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    @Override
    public Lesson getById(Long lessonId) {
        return jdbcTemplate.queryForObject(queries.getQuery("Lesson.getById"),
                (ResultSet resultSet, int rowNumber) -> getLesson(resultSet),
                lessonId);
    }

    @Override
    public void add(Lesson lesson) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Lesson.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, lesson.getCourse().getId());
                    setNullableValue(preparedStatement, 2, lesson.getLecturer().getId());
                    setNullableValue(preparedStatement, 3, lesson.getTimeSlot().getId());
                    setNullableValue(preparedStatement, 4, lesson.getDate());
                    setNullableValue(preparedStatement, 5, lesson.getStartTime());
                    setNullableValue(preparedStatement, 6, lesson.getEndTime());
                    return preparedStatement;
                }, keyHolder);

        Long lessonId = (Long) keyHolder.getKeyList().get(0).get("lesson_id");
        lesson.setId(lessonId);
    }

    @Override
    public void update(Lesson lesson) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("Lesson.update"));
                    setNullableValue(preparedStatement, 1, lesson.getCourse().getId());
                    setNullableValue(preparedStatement, 2, lesson.getLecturer().getId());
                    setNullableValue(preparedStatement, 3, lesson.getTimeSlot().getId());
                    setNullableValue(preparedStatement, 4, lesson.getDate());
                    setNullableValue(preparedStatement, 5, lesson.getStartTime());
                    setNullableValue(preparedStatement, 6, lesson.getEndTime());
                    preparedStatement.setLong(7, lesson.getId());
                    return preparedStatement;
                });
    }

    @Override
    public void delete(Lesson lesson) {
        jdbcTemplate.update(queries.getQuery("Lesson.delete"), lesson.getId());
    }

    @Override
    public List<Lesson> getAll() {
        return jdbcTemplate.query(queries.getQuery("Lesson.getAll"),
                (ResultSet resultSet, int rowNumber) -> getLesson(resultSet)
        );
    }

    @Override
    public List<Lesson> getByLecturerId(Integer lecturerId) {
        return jdbcTemplate.query(queries.getQuery("Lesson.getByLecturerId"),
                (ResultSet resultSet, int rowNumber) -> getLesson(resultSet),
                lecturerId
        );
    }

    @Override
    public List<Lesson> getByStudentId(Integer studentIdId) {
        return jdbcTemplate.query(queries.getQuery("Lesson.getByStudentId"),
                (ResultSet resultSet, int rowNumber) -> getLesson(resultSet),
                studentIdId
        );
    }

    private Lesson getLesson(ResultSet resultSet) throws SQLException {
        Lesson lesson = new Lesson()
                .setId(resultSet.getLong("lesson_id"))
                .setCourse(new Course().setId(resultSet.getInt("course_id")))
                .setLecturer(new Lecturer().setId(resultSet.getInt("lecturer_id")))
                .setDate(resultSet.getDate("lesson_date").toLocalDate());

        Integer timeSlotId = resultSet.getInt("timeslot_id");

        if (!resultSet.wasNull()) {
            lesson.setTimeSlot(new TimeSlot().setId(timeSlotId));
        }

        Time timeStart = resultSet.getTime("lesson_start_time");

        if (!resultSet.wasNull()) {
            lesson.setStartTime(timeStart.toLocalTime());
        }

        Time timeEnd = resultSet.getTime("lesson_end_time");

        if (!resultSet.wasNull()) {
            lesson.setEndTime(timeEnd.toLocalTime());
        }

        return lesson;
    }
}
