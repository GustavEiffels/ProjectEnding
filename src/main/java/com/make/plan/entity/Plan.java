package com.make.plan.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Plan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private int priority;
    private int allDay;
    private LocalDateTime start;
    private LocalDateTime end;
    private String backgroundColor;
    private String borderColor;
    private String textColor;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.REMOVE)
    // commit 시점에만 삭제
    @JoinColumn(name = "code")
    private User user;

    public void changeTitle(String title) { this.title = title; }
    public void changeDescription(String description) { this.description = description; }
    public void changeLocation(String location) { this.location = location; }
    public void changePriority(int priority) { this.priority = priority; }
    public void changeAllDay(int allDay) { this.allDay = allDay; }
    public void changeStart(LocalDateTime start) { this.start = start; }
    public void changeEnd(LocalDateTime end) { this.end = end; }
    public void changeBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    public void changeBorderColor(String borderColor) { this.borderColor = borderColor; }
    public void changeTextColor(String textColor) { this.textColor = textColor; }

}
