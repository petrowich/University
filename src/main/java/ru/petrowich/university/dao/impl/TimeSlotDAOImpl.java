package ru.petrowich.university.dao.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.dao.AbstractDAO;
import ru.petrowich.university.dao.DaoException;
import ru.petrowich.university.dao.TimeSlotDAO;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.util.Queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class TimeSlotDAOImpl extends AbstractDAO implements TimeSlotDAO {
    private final Logger LOGGER = getLogger(getClass().getSimpleName());
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    @Autowired
    public TimeSlotDAOImpl(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    @Override
    public TimeSlot getById(Integer timeSlotId) {
        String query = queries.getQuery("TimeSlots.getById");
        LOGGER.debug("getById: {}; timeSlotId = {}", query, timeSlotId);
        try {
            return jdbcTemplate.queryForObject(query,
                    (ResultSet resultSet, int rowNumber) -> getTimeSlot(resultSet),
                    timeSlotId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("nonexistent timeSlotId {} was passed", timeSlotId);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void add(TimeSlot timeSlot) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("TimeSlots.add"),
                            Statement.RETURN_GENERATED_KEYS);
                    setNullableValue(preparedStatement, 1, timeSlot.getName());
                    setNullableValue(preparedStatement, 2, timeSlot.getStartTime());
                    setNullableValue(preparedStatement, 3, timeSlot.getEndTime());
                    LOGGER.debug("add: {}", preparedStatement);
                    return preparedStatement;
                }, keyHolder);

        Integer lessonId = (Integer) keyHolder.getKeyList().get(0).get("timeslot_id");
        timeSlot.setId(lessonId);
    }

    @Override
    public void update(TimeSlot timeSlot) {
        jdbcTemplate.update(
                (Connection connection) -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.getQuery("TimeSlots.update"));
                    setNullableValue(preparedStatement, 1, timeSlot.getName());
                    setNullableValue(preparedStatement, 2, timeSlot.getStartTime());
                    setNullableValue(preparedStatement, 3, timeSlot.getEndTime());
                    preparedStatement.setInt(4, timeSlot.getId());
                    LOGGER.debug("update: {}", preparedStatement);
                    return preparedStatement;
                }
        );
    }

    @Override
    public void delete(TimeSlot timeSlot) {
        String deleteLessonTimeSlotQuery = queries.getQuery("Lesson.deleteTimeSlot");
        LOGGER.debug("deleteLessonTimeSlot: {}; timeSlotId = {}", deleteLessonTimeSlotQuery, timeSlot.getId());
        this.jdbcTemplate.update(deleteLessonTimeSlotQuery, timeSlot.getId());

        String deleteTimeSlotsQuery = queries.getQuery("TimeSlots.delete");
        LOGGER.debug("delete: {}; timeSlotId = {}", deleteTimeSlotsQuery, timeSlot.getId());
        this.jdbcTemplate.update(deleteTimeSlotsQuery, timeSlot.getId());
    }

    @Override
    public List<TimeSlot> getAll() {
        String query = queries.getQuery("TimeSlots.getAll");
        LOGGER.debug("getAll: {}", query);
        return jdbcTemplate.query(query,
                (ResultSet resultSet, int rowNumber) -> getTimeSlot(resultSet)
        );
    }

    private TimeSlot getTimeSlot(ResultSet resultSet) throws SQLException {
        return new TimeSlot()
                .setId(resultSet.getInt("timeslot_id"))
                .setName(resultSet.getString("timeslot_name"))
                .setStartTime(resultSet.getTime("timeslot_start_time").toLocalTime())
                .setEndTime(resultSet.getTime("timeslot_end_time").toLocalTime());
    }
}
