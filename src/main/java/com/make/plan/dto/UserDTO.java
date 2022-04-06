package com.make.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserDTO {
    private Long code;

    @NotBlank(message = "Id value is a necessary component")
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{6,11}$",
            message = "Id Must be 6 to 11 characters in Alphabet and number.")
    private String id;

    @NotBlank(message = "Password is a necessary component")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "Password must be 8 to 16 characters in uppercase and lowercase letters, numbers, and special characters.")
    private String pw;

    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",
            message = "Email format is incorrect.")
    @NotBlank(message = "Email is a necessary component")
    private String email;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "Nicknames must be 2 to 20 characters long, and can be in Korean, English, and numbers.")
    @NotBlank(message = "nick name is necessary")
    private String nick;

    @NotBlank(message = "Gender is necessary")
    private String gender;

    @NotBlank(message = "Birthday is necessary")
    private String birthday;

    private String status;
    private LocalDateTime regDate;
}
