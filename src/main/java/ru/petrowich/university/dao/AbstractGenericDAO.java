package ru.petrowich.university.dao;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class AbstractGenericDAO {
    protected static void setNullableValue(PreparedStatement preparedStatement, int parameterIndex, String value) throws SQLException {
        if (value != null) {
            preparedStatement.setString(parameterIndex, value);
        } else {
            preparedStatement.setNull(parameterIndex, Types.NULL);
        }
    }


    protected static void setNullableValue(PreparedStatement preparedStatement, int parameterIndex, Integer value) throws SQLException {
        if (value != null) {
            preparedStatement.setInt(parameterIndex, value);
        } else {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
        }
    }

    protected static void setNullableValue(PreparedStatement preparedStatement, int parameterIndex, Long value) throws SQLException {
        if (value != null) {
            preparedStatement.setLong(parameterIndex, value);
        } else {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
        }
    }

    protected static void setNullableValue(PreparedStatement preparedStatement, int parameterIndex, LocalDate value) throws SQLException {
        if (value != null) {
            preparedStatement.setDate(parameterIndex, Date.valueOf(value));
        } else {
            preparedStatement.setNull(parameterIndex, Types.DATE);
        }
    }

    protected static void setNullableValue(PreparedStatement preparedStatement, int parameterIndex, LocalTime value) throws SQLException {
        if (value != null) {
            preparedStatement.setTime(parameterIndex, Time.valueOf(value));
        } else {
            preparedStatement.setNull(parameterIndex, Types.TIME);
        }
    }
}
