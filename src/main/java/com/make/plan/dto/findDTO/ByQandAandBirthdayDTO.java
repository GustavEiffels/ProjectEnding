package com.make.plan.dto.findDTO;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@ToString
@Builder
public class ByQandAandBirthdayDTO {
    @NotBlank(message ="please insert Answer by the context")
    private String answer;

    @NotBlank(message = "Birthday is necessary")
    private String birthday;

    @NotBlank(message = "please choose Question context for finding your information")
    private String context;

}
