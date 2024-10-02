package com.sparta.manage_calendar.controller;

import com.sparta.manage_calendar.dto.ScheduleRequestDto;
import com.sparta.manage_calendar.dto.ScheduleResponseDto;
import com.sparta.manage_calendar.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    //private final Map<Long, Schedule> ScheduleList  = new HashMap<>();

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        //RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        // DB 저장
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

        //Entity -> ResponseDto
        ScheduleResponseDto scheduleresponseDto = new ScheduleResponseDto(schedule);

        return scheduleresponseDto;
    }

    //전체 조회
    @GetMapping("")
    public List<ScheduleResponseDto> getSchedulesAll() {
        // DB 조회
        String sql = "SELECT * FROM schedule ORDER BY date DESC";
        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 schedule 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String todo = rs.getString("todo");
                String date = rs.getString("date");
                return new ScheduleResponseDto(id, name, todo, date);
            }
        });
    }

    @GetMapping({"/id/{id}", "/name/{name}", "/date/{date}", "/datename/{date}/{name}"})
    public List<ScheduleResponseDto> getSchedules(
            //required = false : 경로가 없어도 오류 발생하지 않도록
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

    @PutMapping("{id}/{password}")
    public long updateSchedule(@PathVariable Long id, @PathVariable String password, @RequestBody ScheduleRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findById(id, password);

        if (schedule != null) {
            // schedule 내용 수정
            String sql = "UPDATE schedule SET name = ?, todo = ? , date = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getName(), requestDto.getTodo(), requestDto.getDate(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("{id}/{password}")
    public Long deleteSchedule(@PathVariable Long id, @PathVariable String password) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findById(id, password);
        if (schedule != null) {
            // schedule 삭제
            String sql = "DELETE FROM schedule WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Schedule findById(Long id, String password) {
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
