package com.make.plan.controller;

import com.make.plan.dto.passwordDTO.PasswordDTO;
import com.make.plan.service.forCustomer.login.LoginService;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private  ValidateHandling validateHandling;

    @Autowired
    private LoginService loginService;


    @PostMapping("/main")
    public String login(String account,
                        @Valid PasswordDTO dto,
                        Errors errors,
                        Model model,
                        HttpSession session)
            throws Exception {
        /**
         *  Password dto가 유효한지 확인하는 method -----------------------------------------------
         */
        if (errors.hasErrors()) {
            model.addAttribute("dto", dto);
            Map<String, String> validatorResult = validateHandling.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "home";
        }
        /**
         *  Password dto가 유효한지 확인하는 method -----------------------------------------------
         */

        String url = null;

        Map<String, Object> loginResult = loginService.login(account, dto);

        /**
         * login
         *  공통 값
         *  key = loginResult
         *
         *  true ----------
         *  result.put("code",(Long)info[0]);
         *  result.put("pw",(String)info[2]);
         *  result.put("nick",(String)info[3]);
         *  result.put("status",(String)info[4]);
         *  result.put("birthday",(LocalDate)info[5]);
         *
         *  false ------------
         *  key = errorMessage
         *  key = pwError
         */

        boolean isLogin = (boolean) loginResult.get("loginResult");

        // isLogin ---> ture : 로그인 값이 유효 하다면
        if (isLogin)
        {
            session.setAttribute("code", (Long) loginResult.get("code"));
            session.setAttribute("nick", (String) loginResult.get("nick"));
            if (loginResult.get("status").equals("휴면")) {
                url = "edit/unSub/cancel";
            } else if (loginResult.get("status").equals("회원")) {
                url = "calendar/main";
            }
        }
        else
        {
            String pwError = (String) loginResult.get("pwError");
            /**
             *  pwError 가 존재 ---> pw 가 틀렸음
             *  pwError 가 존재 하지 않음  ---> 입력한 id 혹은 email 이 틀렸을 때
             */
            if (pwError == null) {
                model.addAttribute("accountError", (String) loginResult.get("errorMessage"));
                return "home";
            } else {
                model.addAttribute("passwordError", (String) loginResult.get("pwError"));
                return "home";
            }
        }
        return url;
    }

    @PostMapping("/member/logout")
    public String logout(HttpSession session){
        String userNick = (String)session.getAttribute("nick");
        session.invalidate();
        return "redirect:/";
    }
}
