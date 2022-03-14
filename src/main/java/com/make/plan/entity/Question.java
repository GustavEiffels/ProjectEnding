package com.make.plan.entity;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;

@Entity
@Log4j2
@Getter
@ToString
@Table(name = "Question")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    @Column(nullable = false)
    private String context;


    public void insertcontext(String context){
        this.context=context;
    }
}

