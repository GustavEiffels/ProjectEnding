package com.make.plan.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@ToString
public class AnswerDTO {
    private Long ano;
    private Long code;
    private Long qno;

    @NotBlank(message ="please insert Answer by the context")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "answer type is Korean, numeric, English 2~20 characters")
    private String answer;
}
