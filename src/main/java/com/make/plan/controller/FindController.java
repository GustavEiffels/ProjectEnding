package com.make.plan.controller;

import com.make.plan.dto.findDTO.ByQandAandBirthdayDTO;
import com.make.plan.dto.findDTO.NickDTO;
import com.make.plan.service.forCustomer.email.EmailSenderService;
import com.make.plan.service.forCustomer.find.FindService;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/member/finding")
public class FindController {

    @Autowired
    private FindService findService;

    @Autowired
    private ValidateHandling validateHandling;

    @Autowired
    private EmailSenderService emailSenderService;



    // 계정 정보를 찾기 위한 page로 이동하기 위한 method
    @GetMapping("")
    public String findMainPage() { return "/member/finding/findPage";}


    // id , Email 을 찾기위한 page로 이동하는 method
    @GetMapping("/findAccount")
    public void findAccount(){}

    // password 를 찾기 위한 Page 로 이동하는  method
    @GetMapping("/findPassword")
    public void findPassword(){}


    // nick name 을 입력하면 해당 nickname 에 해당하는 id 와 Email 을 마스킹 된 상태로 보여주기 위한 method
    @PostMapping("/result/emailAndId")
    public String accountFindResult(@Valid NickDTO dto, Errors errors, Model model)throws Exception{

        if(errors.hasErrors()){
            model.addAttribute("dto", dto);
            Map<String, String> validatorResult = validateHandling.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "member/finding/findAccount";
        }
        Map<String, Object> result = findService.findByNick(dto);

        String exceptionEmerge = (String) result.get("exceptionMessage");
        if(exceptionEmerge==null){
            model.addAttribute("email",(String) result.get("email"));
            model.addAttribute("id",(String) result.get("id"));
        }else{
            model.addAttribute("exceptionMessage",exceptionEmerge);
            return "member/finding/findAccount";
        }

        return "member/finding/result/emailAndId";
    }


    // id 혹은 email , 생일과, 질문  질문에 대한 답이 일치할 경우
    // 비밀번호가 변경되고 변경된 비밀번호는 email 로 전송되는 method
    @PostMapping("/result/password")
    public String passwordFindResult(String userInfo, @Valid ByQandAandBirthdayDTO dto, Errors errors, Model model) throws Exception{
    /**
     * dto 에 유효성 검사 method ----------------------------------------------------------------------
     */
        if(errors.hasErrors()){
        model.addAttribute("dto", dto);
        Map<String, String> validatorResult = validateHandling.validateHandling(errors);
        for (String key : validatorResult.keySet()) {
            model.addAttribute(key, validatorResult.get(key));
        }

        return "member/finding/findPassword";
    }
    /**
     * dto 에 유효성 검사 method ----------------------------------------------------------------------
     */



    Map<String ,Object> validResult = findService.resultAccountValid(userInfo,dto);

    // map 에 email 값이 들어있는 경우는 모든 값이 유효할 때임으로
        if(validResult.get("email")!=null) {
        findService.sendNewPassword(userInfo, dto);
    }else{
        if(validResult.get("infoErrorMessage")!=null){
            model.addAttribute("infoErrorMessage",(String) validResult.get("infoErrorMessage"));
            System.out.println((String) validResult.get("infoErrorMessage"));
            return "member/finding/findPassword";
        }else{
            model.addAttribute("validErrorMessage",(String) validResult.get("errorMessage"));
            System.out.println((String) validResult.get("ErrorMessage"));
            return "member/finding/findPassword";
        }
    }

        return "member/finding/result/password";
}

}
