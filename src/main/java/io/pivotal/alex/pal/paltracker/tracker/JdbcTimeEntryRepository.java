package io.pivotal.alex.pal.paltracker.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("projectId", timeEntry.getProjectId());
        namedParameters.put("userId", timeEntry.getUserId());
        namedParameters.put("date", Date.valueOf(timeEntry.getDate()));
        namedParameters.put("hours", timeEntry.getHours());

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (:projectId, :userId, :date, :hours)",
                new MapSqlParameterSource(namedParameters),
                holder);

        return find(
                ((BigInteger) holder.getKeys().get("GENERATED_KEY")).longValue()
        );
    }

    @Override
    public TimeEntry find(long id) {
        Map<String, Long> findParams = new HashMap<>();
        findParams.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject("SELECT * FROM time_entries where id = :id",
                    new MapSqlParameterSource(findParams),
                    new RowMapper<TimeEntry>() {
                        @Override
                        public TimeEntry mapRow(ResultSet resultSet, int i) throws SQLException {
                            return new TimeEntry(
                                    resultSet.getLong("id"),
                                    resultSet.getLong("project_id"),
                                    resultSet.getLong("user_id"),
                                    resultSet.getDate("date").toLocalDate(),
                                    resultSet.getInt("hours")
                            );
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        List<Map<String, Object>> timeEntries = this.jdbcTemplate.queryForList("SELECT * FROM time_entries");
        return timeEntries
                .stream()
                .map(timeEntryResultMap -> new TimeEntry(
                        (Long) timeEntryResultMap.get("id"),
                        (Long) timeEntryResultMap.get("project_id"),
                        (Long) timeEntryResultMap.get("user_id"),
                        ((Date) timeEntryResultMap.get("date")).toLocalDate(),
                        (Integer) timeEntryResultMap.get("hours")
                )).collect(Collectors.toList());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("projectId", timeEntry.getProjectId());
        namedParameters.put("id", id);
        namedParameters.put("userId", timeEntry.getUserId());
        namedParameters.put("date", timeEntry.getDate());
        namedParameters.put("hours", timeEntry.getHours());

        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "UPDATE time_entries SET project_id = :projectId, user_id = :userId, date = :date, hours = :hours WHERE id = :id",
                new MapSqlParameterSource(namedParameters),
                holder);

        return find(id);
    }

    @Override
    public TimeEntry delete(long id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        TimeEntry timeEntry = find(id);
        namedParameterJdbcTemplate.update("DELETE FROM time_entries WHERE id = :id", new MapSqlParameterSource(namedParameters));

        return timeEntry;
    }
}
