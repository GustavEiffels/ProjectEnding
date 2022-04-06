package com.make.plan.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDTO {
    private Long qno;
    @NotBlank(message = "please choose Question context for finding your information")
    private String context;
}
