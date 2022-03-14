package com.make.plan.dto.findDTO;

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
public class NickDTO {
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "not type of nick , please insert nickname type")
    @NotBlank(message = "nick name is necessary")
    private String nick;
}
