package com.make.plan.controller;


import com.make.plan.service.forCustomer.edit.EditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/edit")
public class EditController {
    @Autowired
    private EditService editService;

    @GetMapping("")
    public String editMain()
    {
        return "/edit/editmain";
    }

    @GetMapping("/edit_status")
    public void unsubscribe(){};

    @GetMapping("/editmain")
    public void editmain2(){}



    @GetMapping("/edit_info")
    public void editInfo(HttpSession session) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        String nick = (String) session.getAttribute("nick");
        Map<String, Object> result = editService.bringUserInfo(nick);
        String userId = (String) result.get("userId");
        String userEmail = (String)result.get("userEmail");
        String userGender = null;
        userGender = (String)result.get("userGender");
        Long userCode = (Long)result.get("userCode");
        String userAnswer = editService.bringAnswerInfo(userCode);
            System.out.println(userAnswer);
        String userContext = editService.bringQuestionInfo(userCode);

        if(userGender.equals("m")==true){
        userGender="Male";
    }else if(userGender.equals("f")==true){
        userGender="Female";
    }
    LocalDate userBirthday = (LocalDate) result.get("userBirthday");


        session.setAttribute("id",userId);
        session.setAttribute("email",userEmail);
        session.setAttribute("birthday",userBirthday);
        session.setAttribute("gender",userGender);
        session.setAttribute("answer",userAnswer);
        session.setAttribute("context",userContext);
}

    /**
     * 회원 정보 변경 page 에서 변경하는 method =============================================================================
     */
    @PostMapping("/editing")
    public String editingUserInfo(HttpSession session ,
                                  String nick,
                                  String pw,
                                  String answer,
                                  Model model,
                                  String pwCheck,
                                  String gender,
                                  String birthday,
                                  String context)
    {
        /**
         *  paramValid : key---> isValid
         *  true --> error 존재 x
         *  false --> error 존재함
         */
        Map<String, Object> paramValid = editService.parameterValidCheck
                (
                        session,
                        nick,
                        pw,
                        answer,
                        gender,
                        birthday,
                        context
                );

        // error 가 존재하지 않음 : 변수 전체는 유효한 값 -----------------------------------------------------------------
        if((boolean)paramValid.get("isValid"))
        {
            nick = (String)paramValid.get("nick");
            pw = (String)paramValid.get("pw");
            answer = (String)paramValid.get("answer");
            birthday = (String)paramValid.get("birthday");
            gender = (String)paramValid.get("gender");
            context = (String)paramValid.get("context");

            // session 에서 nick name 을 받아옴 : 바뀔 nick 과 비교하기 위해서 ----------------------------------------------
            String currentNick = (String)session.getAttribute("nick");

            /**
             * @return --------------------------------------------------------------------
             * key
             * result ---> true : 값을 정상적으로 전달
             * result ---> false : error 발생
             *
             * error
             * key
             * nickErrorMessage
             * pwErrorMessage
             */
            Map<String, Object> result = editService.changeUserInfo
                    (
                            session,
                            currentNick,
                            nick,
                            pw,
                            pwCheck,
                            gender,
                            birthday,
                            answer,
                            context
                    );
            // 정상적으로 회원정보가 변경 됨 -----------------------------------------------
            if ((boolean) result.get("result"))
            {
                session.setAttribute("nick", nick);
            }

            // error 가 발생했을 때 ------------------------------------------------------
            // 닉네임 중복, 비밀번호 비밀번호 체크 불일치
            else
            {

                // 닉네임이 중복 되었을 경우 ----------------------------
                if(result.containsKey("nickErrorMessage"))
                {
                    model.addAttribute("nickErrorMessage",(String)result.get("nickErrorMessage"));
                }

                // 비밀번호와 비밀번호 확인 값이 일치하지 않을 경우 -----------------------------
                if(result.containsKey("pwErrorMessage"))
                {
                    model.addAttribute("pwErrorMessage",(String)result.get("pwErrorMessage"));
                }

                return "edit/edit_info";
            }


        }
        // parameter 값이 유효한 값이 아닐 때  ------------------------------------------------------
        else if(!(boolean)paramValid.get("isValid"))
        {
            // 닉네임 유효성을 불만족 ------------------
            if(paramValid.containsKey("nickValidError"))
            {
                model.addAttribute("nickValidError",(String)paramValid.get("nickValidError"));
            }


            // 비밀번호 유효성을 불만족----------
            if(paramValid.containsKey("pwValidError"))
            {
                model.addAttribute("pwValidError",(String)paramValid.get("pwValidError"));
            }


            // 질문에 대한 답 불만족 ----------
            if(paramValid.containsKey("answerValidError"))
            {
                model.addAttribute("answerValidError",(String)paramValid.get("answerValidError"));
            }
            // 질문에 대한 답 불만족 ----------


            // 질문이 변경되었는데 답을 입력하지 않았을 때 -----------
            if(paramValid.containsKey("errorMessage"))
            {
                model.addAttribute("errorMessage", paramValid.get("errorMessage"));
            }

            return "edit/edit_info";
        }

        return "edit/editmain";
    }
}
