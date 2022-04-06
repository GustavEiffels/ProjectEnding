package com.make.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private int priority;
    private boolean allDay;
    private String start;
    private String end;
    private String backgroundColor;
    private String borderColor;
    private String textColor;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Long code;
}
