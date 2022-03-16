package com.make.plan.service.forCustomer.login;

import com.make.plan.dto.passwordDTO.PasswordDTO;

import java.util.Map;

public interface LoginService {
    /**
     *  로그인을 위한 method
     */
    Map<String, Object> login(String account, PasswordDTO dto) throws Exception;
}