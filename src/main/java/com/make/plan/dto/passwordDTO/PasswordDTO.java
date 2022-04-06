package com.make.plan.dto.passwordDTO;


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
public class PasswordDTO {
    @NotBlank(message = "Password is a necessary component")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "Password must be 8 to 16 characters in uppercase and lowercase letters, numbers, and special characters.")
    private String pw;


}
