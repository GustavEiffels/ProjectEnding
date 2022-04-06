package com.make.plan.service.forCustomer.login;


import com.make.plan.dto.passwordDTO.PasswordDTO;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class LonginServiceImpl implements LoginService {

    private final ValidateHandling validateHandling;

    /**------------------- 로그인을 위한 method--------------------------------------------------------
     */
    @Override
    public Map<String, Object> login(String account, PasswordDTO dto) throws Exception {
        Map<String,Object> result = validateHandling.accountValidCheck(account);
        /**
         * accountValidCheck
         *  값이 유효하면-----
         *  result.put("code",(Long)info[0]);
         *  result.put("pw",(String)info[2]);
         *  result.put("nick",(String)info[3]);
         *  result.put("status",(String)info[4]);
         *  result.put("birthday",(LocalDate)info[5]);
         *
         *  값이 유효하지 않다면 -------
         *  key --> errorMessage
         */

        // false ---> 로그인이 불가능
        // true ----> 로그인이 가능하게 끔
        boolean isAvailable = false;

        // map 에 "errorMessage" 라는 key 가 존재하지 않을 때
        // -----> 입력한 id  와  email 이 존재한다
        if(result.get("errorMessage")==null)
        {

            if(BCrypt.checkpw(dto.getPw(), (String)result.get("pw")))
            {
                // 입력한 비밀번호와 등록한 비밀번호가 맞다면
                isAvailable = true;
            }

            else
            {
                /***
                 *  else 는 비밀번호가 서로 다르다는 말
                 *  pwError 에 값을 부여
                 * */
                result.put("pwError", "Wrong Password ! Pleas check it");
            }
        }
        // result ( map ) 에 로그인을 가능하게끔 하는 결과를 입력한다
        result.put("loginResult", isAvailable);
        return result;
    }
}