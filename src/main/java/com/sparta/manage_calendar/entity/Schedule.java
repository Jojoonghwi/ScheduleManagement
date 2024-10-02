package com.sparta.manage_calendar.entity;

import com.sparta.manage_calendar.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private Long id;
    private String name;
    private String password;
    private String todo;
    private String date;

    public Schedule(ScheduleRequestDto requestDto) {
        this.id = requestDto.getId();
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.date = LocalDateTime.now().toString();
    }

    public void update(Long id, LocalDateTime date, ScheduleRequestDto requestDto) {
        this.id = id;
        this.name = requestDto.getName();
        this.password = requestDto.getPassword();
        this.todo = requestDto.getTodo();
        this.date = date.toString();
    }
}
