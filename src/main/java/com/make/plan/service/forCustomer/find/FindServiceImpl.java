package com.make.plan.service.forCustomer.find;


import com.make.plan.dto.findDTO.ByQandAandBirthdayDTO;
import com.make.plan.dto.findDTO.NickDTO;
import com.make.plan.entity.Question;
import com.make.plan.entity.User;
import com.make.plan.repository.AnswerRepository;
import com.make.plan.repository.QuestionRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.forCustomer.email.EmailSenderService;
import com.make.plan.service.forCustomer.email.serialNumberFactory.ForFindPw;
import com.make.plan.service.forCustomer.validationHandling.CryptoUtil;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class FindServiceImpl implements FindService{
    private final UserRepository userRepository;

    private final ValidateHandling validateHandling;

    private final EmailSenderService emailSenderService;

    private final MaskingService maskingService;

    private final ForFindPw forFindPw;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;


    /**
     * 1. 닉네임으로 id, email 찾기 ===================================================================================
     */
    @Override
    public Map<String, Object> findByNick(NickDTO dto) throws Exception
    {

        String nick = dto.getNick();

        Object userInfo = userRepository.findByNick(nick);

        Map<String, Object> result = new HashMap<>();

        /**
         *  Object[0] = id
         *  Object[1] = email
         *  Object[2] = status
         */
        Object[] info = (Object[]) userInfo;



        if(userInfo!=null)
        {
            String status = (String)info[2];
            if(!status.equals("expired"))
            {
                result = maskingService.masking(info);
            }
        }
        else
        {
            // 이상 발생시 Message 를 받을 변수
            result.put("exceptionMessage","Invalid Message");
        }
        return result;
    }

    /**
     * 1. 닉네임으로 id, email 찾기 ===================================================================================
     */



    /**
     * 2. == 비밀번호를 변경하고 Email 로 보내기 ====================================================================================
     */



    /** code 와 answer 를 받고 해당 답이 일치하는지 확인하는 method-------------------------------------------------------------
     * */
    public boolean answerCheckMethod(Long code, String answer){
        User user = User.builder()
                .code(code)
                .build();

        String getAnswer = answerRepository.getAnswerByCode(user);

        boolean answerValid = getAnswer.equals(answer);

        return answerValid;
    }


    /**
    code 와 context를 넣고 질문이 유효하는지 확인하는 method-------------------------------------------------------------
    **/
    public boolean contextCheckMethod(Long code, String context){
        User user = User.builder()
                .code(code)
                .build();

        Question qno = answerRepository.getQnoByCode(user);

        String userContext = questionRepository.getContextByQno(qno.getQno());

        boolean contextValid = userContext.equals(context);

        return contextValid;
    }
    /** code 를 받으면
     * email 값을 받아오는 method 생성-------------------------------------------------------------
     * */
    public String getEmail(Long code) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Object accountArray = userRepository.getIdAndEmailByCode(code);
        Object[] objectArray = (Object[]) accountArray;
        String email = (String) objectArray[1];
            email = CryptoUtil.decryptAES256(email,"Email");
            System.out.println(email);

        return email;
    }



    /**
     * User 의  Email 과 변경된 비밀번호를
     * user 의 Email 로 보내는 method
     * ----------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void sendNewPassword(String userInfo, ByQandAandBirthdayDTO dto) throws Exception {
        Map<String, Object> result = resultAccountValid(userInfo, dto);


        System.out.println(result.get("newPw"));
        System.out.println(result.get("email"));
        System.out.println(result.get("code"));


        String newPw = (String) result.get("newPw");
        String email = (String) result.get("email");

        String message = "Your new Password  : "+result.get("newPw")+"\n" +
                "\n Plaese Insert BlanK For Join Us";
        userRepository.changingPw(BCrypt.hashpw(newPw,BCrypt.gensalt()), (Long) result.get("code"));
        emailSenderService.sendMail("Here is New Password From Make Your Plan", email, message);
    }

    /**
     *
     * pw 가 기억이 나지 않는 경우
     * userInfo 와
     * user 의 개인 부가 정보 , 질문, 질문에 대한 답 , 생일을 받아서
     * user 인지 확인하고
     * email 과 새로운 비밀번호를 부여하는 method ----------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public Map<String, Object> resultAccountValid(String userInfo, ByQandAandBirthdayDTO dto) throws Exception {
        Map<String, Object> result = validateHandling.accountValidCheck(userInfo);

        // ErrorMessage 가 null 이 아니라면
        // userInfo 가 유효함으로
        System.out.println((String) result.get("errorMessage"));
        if(result.get("errorMessage")==null)
        {
            Long code = (Long)result.get("code");
            LocalDate userBirthDay = (LocalDate)result.get("birthday");

            // dto 에서 받아온 user 의 생일을 받아옴
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthday = LocalDate.parse(dto.getBirthday(), formatter);

            // 받아온 정보들이 맞는지 확인

            // 1. 생일이 맞는지 확인
            boolean isItBirthday = birthday.equals(userBirthDay);

            // 2. 질문에 대한 답이 맞는지 확인
            boolean isItAnswer = answerCheckMethod(code, dto.getAnswer());

            // 3. 선택한 질문이 맞는지 확인
            boolean isItContext = contextCheckMethod(code, dto.getContext());


            // 생일 , 질문 , 질문에 대한 답이 모두 일치한다면
            // 비밀번호를 변경하고 변경된 비밀번호를 email 로 보내야한다 .
            if(isItAnswer && isItBirthday && isItContext)
                {
                    // user 의 Email
                    String userEmail = getEmail(code);

                    // user 의 새로운 비밀번호
                    String newPassword = forFindPw.excuteGenerate();

                    result.put("email",userEmail);
                    result.put("newPw",newPassword);
                }
            // 생일 , 질문 , 질문에 대한 답 중 하나가 일치 하지 않음으로
            // ErrorMessage 를 생성해야한다.
            else
                {
                    result.put("infoErrorMessage","The Information what you entered is not collect");
                }
        }
        return result;
    }

    /**
     * 2. == 비밀번호를 변경하고 Email 로 보내기 ====================================================================================
     */
}
