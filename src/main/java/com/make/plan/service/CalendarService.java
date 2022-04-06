package com.make.plan.service;

import com.make.plan.dto.CalendarDTO;
import com.make.plan.entity.Plan;
import com.make.plan.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {

    List<CalendarDTO> getAll();

    CalendarDTO register(CalendarDTO calendarDTO);

    CalendarDTO modify(CalendarDTO calendarDTO);

    Long remove(Long id);

    default Plan dtoToEntity(CalendarDTO calendarDTO) {
        String[] colors = {"#dcced3", "#d1bec7", "#c7b0bc", "#876479", "#674559", "#4c3041"};
        int allDay = calendarDTO.isAllDay() ? 1 : 0;
        int p = calendarDTO.getPriority();
        String textColor = p > 2 ? "white" : "gray";

        User user = User.builder()
                .code(calendarDTO.getCode())
                .build();

        Plan plan = Plan.builder()
                .id(calendarDTO.getId())
                .title(calendarDTO.getTitle())
                .description(calendarDTO.getDescription())
                .location(calendarDTO.getLocation())
                .priority(p)
                .allDay(allDay)
                .start(LocalDateTime.parse(calendarDTO.getStart()))
                .end(LocalDateTime.parse(calendarDTO.getEnd()))
                .backgroundColor(colors[p - 1])
                .borderColor(colors[p])
                .textColor(textColor)
                .user(user)
                .build();
        return plan;
    }

    default CalendarDTO entityToDTO(Plan plan) {
        boolean allDay = plan.getAllDay() == 0 ? false : true;
        CalendarDTO dto = CalendarDTO.builder()
                .id(plan.getId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .location(plan.getLocation())
                .priority(plan.getPriority())
                .allDay(allDay)
                .start(plan.getStart().toString())
                .end(plan.getEnd().toString())
                .backgroundColor(plan.getBackgroundColor())
                .borderColor(plan.getBorderColor())
                .textColor(plan.getTextColor())
                .build();
        return dto;
    }

    List<CalendarDTO> getcurrentplan(User code, String currentDate);
}
