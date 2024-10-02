package com.sparta.manage_calendar.dto;

import lombok.Getter;
import com.sparta.manage_calendar.entity.Schedule;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {
    private Long id;
    private String name;
    private String password;
    private String todo;
    private String date = LocalDateTime.now().toString();
}
