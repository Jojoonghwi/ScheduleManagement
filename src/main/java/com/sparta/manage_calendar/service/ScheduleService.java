package com.sparta.manage_calendar.service;

import com.sparta.manage_calendar.dto.ScheduleRequestDto;
import com.sparta.manage_calendar.dto.ScheduleResponseDto;
import com.sparta.manage_calendar.entity.Schedule;
import com.sparta.manage_calendar.repository.ScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ScheduleResponseDto postSchedule(ScheduleRequestDto requestDto) {
        //RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);

        // DB 저장
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule saveSchedule =  scheduleRepository.save(schedule);

        //Entity -> ResponseDto
        ScheduleResponseDto scheduleresponseDto = new ScheduleResponseDto(saveSchedule);

        return scheduleresponseDto;
    }

    public List<ScheduleResponseDto> getSchedules(
            @PathVariable(required = false) String id,
            @PathVariable(required = false) String name,
            @PathVariable(required = false) String date) {

        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findSchedules(id, name, date);
    }

    public long putSchedule(Long id, String password, ScheduleRequestDto requestDto) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(id, password);

        if (schedule != null) {
            // schedule 내용 수정
            scheduleRepository.updateSchedule(id, password, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteSchedule(Long id, String password) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(id, password);
        if (schedule != null) {
            // schedule 삭제
            scheduleRepository.DeleteSchedule(id, password);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
