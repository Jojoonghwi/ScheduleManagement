package com.sparta.manage_calendar.repository;

import com.sparta.manage_calendar.dto.ScheduleRequestDto;
import com.sparta.manage_calendar.dto.ScheduleResponseDto;
import com.sparta.manage_calendar.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //데이터베이스 저장
    public Schedule save(Schedule schedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (name, password, todo, date) VALUES (?,?,?,?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, schedule.getName());
            preparedStatement.setString(2, schedule.getPassword());
            preparedStatement.setString(3, schedule.getTodo());
            preparedStatement.setString(4, schedule.getDate());
            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        schedule.setId(id);

        return schedule;
    }

    public List<ScheduleResponseDto> findSchedules(
            @PathVariable(required = false) String id,
            @PathVariable(required = false) String name,
            @PathVariable(required = false) String date) {

        //WHERE 1=1은 뒤에 AND를 붙이기 위한 편의성
        StringBuilder sql = new StringBuilder("SELECT * FROM schedule WHERE 1=1");

        // SQL 조건문을 동적으로 추가
        List<Object> params = new ArrayList<>();
        if (id != null) {
            sql.append(" AND id = ?");
            params.add(id);
        }
        if (name != null) {
            sql.append(" AND name = ?");
            params.add(name);
        }
        if (date != null) {
            sql.append(" AND date = ?");
            params.add(date);
        }

        sql.append(" ORDER BY date DESC");

        return jdbcTemplate.query(sql.toString(), params.toArray(), new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long scheduleId = rs.getLong("id");
                String scheduleName = rs.getString("name");
                String todo = rs.getString("todo");
                String scheduleDate = rs.getString("date");
                return new ScheduleResponseDto(scheduleId, scheduleName, todo, scheduleDate);
            }
        });
    }

    public void updateSchedule(Long id, String password, ScheduleRequestDto requestDto) {
        String sql = "UPDATE schedule SET name = ?, todo = ? , date = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getName(), requestDto.getTodo(), requestDto.getDate(), id);
    }

    public void DeleteSchedule(Long id, String password) {
        String sql = "DELETE FROM schedule WHERE id =? AND password =?";
        jdbcTemplate.update(sql, id, password);
    }

    public Schedule findById(Long id, String password) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ? and password = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setName(resultSet.getString("name"));
                schedule.setTodo(resultSet.getString("todo"));
                return schedule;
            } else {
                return null;
            }
        }, id, password);
    }
}
