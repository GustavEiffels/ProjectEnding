package com.make.plan.service.forCustomer.user;


import com.make.plan.dto.UserDTO;
import com.make.plan.entity.User;
import com.make.plan.service.forCustomer.validationHandling.CryptoUtil;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public interface UserService {
    // default 를 사용하는 이유
    // interface 내에서도  로직이 포함된 메소드를 선언할 수 있게 하기 위해서 .
    // (하위 호환성 때문임)

    // DTO 를 Entity 로 변환

    // Entity 를 DTO 로 변경




    default User dtoToEntity(UserDTO dto) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dto.getBirthday(), formatter);
        String email = CryptoUtil.encryptAES256(dto.getEmail(),"Email");
        String enpw = BCrypt.hashpw(dto.getPw(),BCrypt.gensalt());
        User entity = User.builder()
                .code(dto.getCode())
                .id(dto.getId())
                .pw(enpw)
                .email(email)
                .nick(dto.getNick())
                .gender(dto.getGender())
                .birthday(date)//date
                .status(dto.getStatus())
                .build();
        return entity;
    }
    default UserDTO entityToDto(User entity) throws Exception{
        String email = CryptoUtil.decryptAES256(entity.getEmail(),"Email");
        UserDTO dto = UserDTO.builder()
                .code(entity.getCode())
                .id(entity.getId())
                .pw(entity.getPw())
                .email(email)
                .nick(entity.getNick())
                .gender(entity.getGender())
                .birthday(String.valueOf(entity.getBirthday()))
                .status(entity.getStatus())
                .regDate(entity.getRegDate())
                .build();
        return dto;
    }


    public default Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }





    // 회원 가입을 위한 method 생성
    public User join(UserDTO dto) throws Exception;


    // 복호화 확인을 위한 method 생성
    public String dec(Long code);

    // 로그인을 위해 해당 아이디가 있는지 확인하는 method 작성
    public Boolean checkid(String id);

    // 로그인을 위해 해당 email이 있는지 확인하는 method 작성
    public Boolean checkEmail(String email);






}
