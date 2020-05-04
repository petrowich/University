package ru.petrowich.university.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.util.Queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("timeSlotDAO")
public class TimeSlotDAO {
    private final JdbcTemplate jdbcTemplate;
    private final Queries queries;

    public TimeSlotDAO(JdbcTemplate jdbcTemplate, Queries queries) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
    }

    public TimeSlot getById(Integer timeSlotId) {
        return jdbcTemplate.queryForObject(queries.getQuery("TimeSlots.getById"),
                (ResultSet resultSet, int rowNumber) -> getTimeSlot(resultSet),
                timeSlotId);
    }

    public List<TimeSlot> getAll() {
        return jdbcTemplate.query(queries.getQuery("TimeSlots.getAll"),
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
