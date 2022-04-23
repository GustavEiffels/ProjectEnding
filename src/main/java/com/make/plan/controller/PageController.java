package com.make.plan.controller;

import com.make.plan.dto.AnswerDTO;
import com.make.plan.dto.QuestionDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.service.forCustomer.answer.AnswerService;
import com.make.plan.service.forCustomer.question.QuestionService;
import com.make.plan.service.forCustomer.user.UserService;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {

    @Autowired
    private ValidateHandling validateHandling;

    @Autowired
    private UserService userService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;



    @GetMapping("/")
    public String home() { return "home";}

    @GetMapping("/glance")
    public void glance() {}

    @GetMapping("/calendar/main")
    public void calendarMain() {}

    @GetMapping("/calendar/table")
    public void calendarList() {}

    @GetMapping("/member/join")
    public void join() {}

    @PostMapping("/member/join/joining")
    public String joining(@Valid UserDTO dto,
                                Errors errors,
                                @Valid QuestionDTO qdto ,
                                Errors qerrors,
                                @Valid AnswerDTO adto ,
                                Errors aerrors,
                                Model model,
                                String pwCheck
    ) throws Exception {
        // 유효성 통과 못한 필드와 메시지를 핸들링
        if(errors.hasErrors()){
            model.addAttribute("dto", dto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = validateHandling.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "member/join";
        }
        if(qerrors.hasErrors()){
            model.addAttribute("dto",dto);

            Map<String, String> validatorResult = validateHandling.validateHandling(qerrors);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "member/join";
        }
        if(aerrors.hasErrors()){
            model.addAttribute("dto", adto);

            Map<String, String> validatorResult = validateHandling.validateHandling(aerrors);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key, validatorResult.get(key));
            }
            return "member/join";
        }

        String email = dto.getEmail();
        String id = dto.getId();
        String pw = dto.getPw();
        String nick = dto.getNick();

        Map<String, String> result = validateHandling.joinValidation(email, id, nick, pw, pwCheck);

        String validCheck = result.get("result");

        if(validCheck.equals("okay")) {

            userService.join(dto);
            questionService.insertQuestionTableTest(qdto);
            AnswerDTO answerDTO = AnswerDTO.builder()
                    .code(dto.getCode())
                    .qno(qdto.getQno())
                    .answer(adto.getAnswer())
                    .build();
            answerService.selfcheckinggood(answerDTO);
        }else{
            if(result.get("emailMessage")!=null){
                model.addAttribute("emailMessage",result.get("emailMessage"));
            }
            if(result.get("idMessage")!=null){
                model.addAttribute("idMessage",result.get("idMessage"));
            }
            if(result.get("nickMessage")!=null){
                model.addAttribute("nickMessage",result.get("nickMessage"));
            }
            if(result.get("pwMessage")!=null){
                model.addAttribute("pwMessage",result.get("pwMessage"));
            }
            return "member/join";
        }
        return "member/success";
    }

}
