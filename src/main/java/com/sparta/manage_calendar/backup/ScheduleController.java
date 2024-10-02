//package com.sparta.manage_calendar.backup;
//
//import com.sparta.manage_calendar.dto.ScheduleRequestDto;
//import com.sparta.manage_calendar.dto.ScheduleResponseDto;
//import com.sparta.manage_calendar.entity.Schedule;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/schedule")
//public class ScheduleController {
//
//    private final Map<Long, Schedule> ScheduleList  = new HashMap<>();
//
//    @PostMapping("")
//    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
//        //RequestDto -> Entity
//        Schedule schedule = new Schedule(requestDto);
//
//        //schedule Max ID
//        Long maxId = ScheduleList.size() > 0 ? Collections.max(ScheduleList.keySet()) + 1 : 1;
//        schedule.SetId(maxId);
//
//        LocalDateTime dateTime = LocalDateTime.now();
//        schedule.SetDate(dateTime);
//
//        //데이터베이스 저장
//        ScheduleList.put(schedule.getId(), schedule);
//
//        //Entity -> ResponseDto
//        ScheduleResponseDto scheduleresponseDto = new ScheduleResponseDto(schedule);
//
//        return scheduleresponseDto;
//    }
//
//    @GetMapping("")
//    public List<ScheduleResponseDto> getSchedules() {
//        //Map to List
//        List<ScheduleResponseDto> responseList = ScheduleList.values().stream().map(ScheduleResponseDto::new).toList();
//
//        return responseList;
//    }
//
////    @GetMapping("{id}")
////    public List<ScheduleResponseDto> getSchedules() {
////        //Map to List
////        List<ScheduleResponseDto> responseList = ScheduleList.values().stream().map(ScheduleResponseDto::new).toList();
////
////        return responseList;
////    }
//
//    @PutMapping("{id}")
//    public long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
//        //데이터베이스에 있는지 확인
//        if(ScheduleList.containsKey(id)){
//            //해당 일정 가져오기
//            Schedule schedule = ScheduleList.get(id);
//
//            schedule.SetId(id);
//            //일정 수정
//            LocalDateTime dateTime = LocalDateTime.now();
//
//            schedule.update(id, dateTime, requestDto);
//            return schedule.getId();
//        }
//        else {
//            throw new IllegalArgumentException("선택한 일정이 없습니다");
//        }
//    }
//
//    @DeleteMapping("{id}")
//    public Long deleteSchedule(@PathVariable Long id) {
//        if(ScheduleList.containsKey(id)) {
//            // 해당 메모 삭제하기
//            ScheduleList.remove(id);
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택한 일정이 없습니다");
//        }
//    }
//}
