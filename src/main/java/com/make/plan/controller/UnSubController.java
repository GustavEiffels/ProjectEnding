package com.make.plan.controller;

import com.make.plan.dto.passwordDTO.PasswordDTO;
import com.make.plan.service.forCustomer.edit.EditService;
import com.make.plan.service.forCustomer.unSub.UnSubService;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/edit/unSub/")
public class UnSubController
{

    @Autowired
    private EditService editService;

    @Autowired
    private ValidateHandling validateHandling;

    @Autowired
    private UnSubService unSubService;


    @PostMapping("")
    public String unSubscribe(@Valid PasswordDTO dto, Errors errors,Model model, HttpSession session)
    {
        // ---- 유효성 검사 ----------
        if(errors.hasErrors())
        {
            model.addAttribute("dto", dto);
            Map<String, String> validatorResult = validateHandling.validateHandling(errors);
            for (String key : validatorResult.keySet())
            {
                model.addAttribute(key, validatorResult.get(key));
            }
            return  "edit/edit_status";
        }

        /** 입력한 비밀번호와, user 의 비밀번호가 같은지 확인 ----------------------------
         */

        // ---  계정을 탈퇴 ( 휴면 ) 계정으로 전환 확인 page 로 이동  ----------
        if( editService.isPwEqual(dto.getPw(), (Long)session.getAttribute("code")) )
        {
            return "edit/unSub/unsubscribing";
        }
        // --- 비밀번호가 서로 맞지 않음으로 현재 page로 이동 ----------
        else
        {
            model.addAttribute("errorMessage","Wrong Password! , Please Check again");
            return  "edit/edit_status";
        }

    }

    @PostMapping("/complete")
    public String unSubComplete(HttpSession sessioin)
    {
        Long code = (Long)sessioin.getAttribute("code");

        // 정상적으로 만료가 된다면
        if ( unSubService.unSubscribing("휴면",code))
        {
            unSubService.updateModDate(code);
            sessioin.invalidate();
        }
        return "/edit/unSub/complete";
    }

    @PostMapping("/cancel")
    public String unSubCancel(){return "/edit/unSub/cancel";}

    @PostMapping("/canceling")
    public String unSubCanceling(
            HttpSession session,
            @Valid PasswordDTO dto,
            Errors errors,
            Model model,
            String pwCheck)
    {
        if (errors.hasErrors())
        {
            model.addAttribute("dto", dto);
            Map<String, String> validatorResult = validateHandling.validateHandling(errors);

            for (String key : validatorResult.keySet())
            {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "edit/unSub/cancel";
        }

        // 비밀번호 값과 비밀번호 확인 값이 같다면 --------------------
        if (editService.unSubCancel(dto.getPw(), pwCheck, (Long) session.getAttribute("code")) > 0)
        {
            return "redirect:/";
        }
        else
        {
            model.addAttribute("errorMessage", "The passwords entered do not match each other.");
            return "edit/unSub/cancel";

        }
    }
}
