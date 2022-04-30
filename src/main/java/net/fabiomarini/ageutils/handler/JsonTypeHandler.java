package net.fabiomarini.ageutils.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(JsonTypeHandler.class);
    protected static final ThreadLocal<ObjectMapper> objectMapper = ThreadLocal.withInitial(() -> {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    });

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object t, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, objectMapper.get().writeValueAsString(t));
        } catch (JsonProcessingException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String jsonString = resultSet.getString(columnName);
        return readFromString(jsonString);
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int columnIdx) throws SQLException {
        String jsonString = resultSet.getString(columnIdx);
        return readFromString(jsonString);
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int columnIdx) throws SQLException {
        String jsonString = callableStatement.getString(columnIdx);
        return readFromString(jsonString);
    }

    protected T readFromString(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            return objectMapper.get().readValue(jsonString, new TypeReference<T>() {
            });
        } catch (Exception e) {
            logger.error("Error converting JSON value", e);
            return null;
        }
    }

    protected T readFromString(String jsonString, Class<T> type) {
        if (jsonString == null) {
            return null;
        }
        try {
            return objectMapper.get().readValue(jsonString, type);
        } catch (Exception e) {
            logger.error("Error converting JSON value", e);
            return null;
        }
    }
}
