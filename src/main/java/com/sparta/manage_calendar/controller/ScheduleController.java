package com.sparta.manage_calendar.controller;

import com.sparta.manage_calendar.dto.ScheduleRequestDto;
import com.sparta.manage_calendar.dto.ScheduleResponseDto;
import com.sparta.manage_calendar.service.ScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.*;

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
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.postSchedule(requestDto);
    }

    @GetMapping({"", "/id/{id}", "/name/{name}", "/date/{date}", "/datename/{date}/{name}"})
    public List<ScheduleResponseDto> getSchedules(
            //required = false : 경로가 없어도 오류 발생하지 않도록
            @PathVariable(required = false) String id,
            @PathVariable(required = false) String name,
            @PathVariable(required = false) String date) {

        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.getSchedules(id, name, date);
    }

    @PutMapping("{id}/{password}")
    public long updateSchedule(@PathVariable Long id, @PathVariable String password, @RequestBody ScheduleRequestDto requestDto) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.putSchedule(id, password, requestDto);
    }

    @DeleteMapping("{id}/{password}")
    public Long deleteSchedule(@PathVariable Long id, @PathVariable String password) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.deleteSchedule(id, password);
    }
}
