package com.make.plan.service.forCustomer.edit;

import com.make.plan.entity.Question;
import com.make.plan.entity.User;
import com.make.plan.repository.AnswerRepository;
import com.make.plan.repository.QuestionRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.forCustomer.validationHandling.CryptoUtil;
import com.make.plan.service.forCustomer.validationHandling.ValidateHandling;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
@Log4j2
@RequiredArgsConstructor
public class EditServiceImpl implements EditService{
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;


    private  final ValidateHandling validateHandling;

    //    @Query(value = "select id, email, gender, birthday from User where id = :nick ")
    @Override
    public Map<String, Object> bringUserInfo(String nick) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Object result = userRepository.bringUserData(nick);
        Object[] userInfo = (Object[]) result;


        String userId = (String)userInfo[0];
        String userEmail = (String)userInfo[1];
        userEmail = CryptoUtil.decryptAES256(userEmail,"Email");
        String userGender = (String)userInfo[2];
        LocalDate userBirthday = (LocalDate) userInfo[3];
        Long userCode = (Long)userInfo[4];



        Map<String, Object> users = new HashMap<>();
        users.put("userId", userId);
        users.put("userEmail", userEmail);
        users.put("userGender", userGender);
        users.put("userBirthday", userBirthday);
        users.put("userCode", userCode);
        return users;
    }

    @Override
    public String bringAnswerInfo(Long code) {
        User userCode  = User.builder()
                .code(code)
                .build();
        String userAnswer = answerRepository.getAnswerByCode(userCode);
        return userAnswer;
    }
    //
    @Override
    public String bringQuestionInfo(Long code) {
        User userCode  = User.builder()
                .code(code)
                .build();

        Question result = answerRepository.getQnoByCode(userCode);

        Long userQno = result.getQno();
        Question qno = Question.builder()
                .qno(userQno)
                .build();

        String userContext = questionRepository.getContextByQno(qno.getQno());
        return userContext;
    }

    /** 입력한 비밀번호가, 사용자의 비밀번호와 같은지 확인하는 method
     */
    @Override
    public boolean isPwEqual(String pw,Long code)
    {
        boolean isValid = false;

        String userPw = userRepository.getPw(code);

        if(BCrypt.checkpw(pw, userPw))
        {
            isValid = true;
        }

        return isValid;
    }


    /**
     * user 의 개인 정보 변경 method
     * - method 들이 합쳐져있음
     * /**
     *          * @return
     *          * key
     *          * result ---> true : 값을 정상적으로 전달
     *          * result ---> false : error 발생
     *          *
     *          * error
     *          * key
     *          * nickErrorMessage
     *          * pwErrorMessage
     *
     */
    @Override
    public Map<String, Object> changeUserInfo(HttpSession session,
                                              String currentNick,
                                              String nick,
                                              String pw,
                                              String pwCheck,
                                              String gender,
                                              String birthday,
                                              String answer,
                                              String context) {

        /**
         * @return
         * key
         * result ---> true : 값을 정상적으로 전달
         * result ---> false : error 발생
         *
         * error
         * key
         * nickErrorMessage
         * pwErrorMessage
         */
        Map<String, Object> userInfoUpdate = userInfoUpdate(currentNick,nick,pw,pwCheck,gender,birthday);

        // ------------------------------------- session 에서 user code 받는다
        Long code = (Long) session.getAttribute("code");

        // User Entity 혀태로 변경
        User userCode = User.builder()
                .code(code)
                .build();

        // ano를 구하기 위한 method --------------------------------------------------------------
        Long ano = answerRepository.anoByCode(userCode);



        // context 를 수정하기 위해서 AnswerRepository 를 사용해서 qno 를 return ----------------------------------
        Question questionQno = answerRepository.getQnoByCode(userCode);
        Long qno = questionQno.getQno();


        /**
         * answer 를 수정하기 위한 method -----------------------------------------------------
         * 0 < result ---> 변경 성공
         * 0 > result ---> 변경 실패
         */
        answerRepository.updateUserAnswer(ano, answer);


        /**
         * context 를 수정하기 위한 method -------------------------------------------------------
         * 0 < result ---> 변경 성공
         * 0 > result ---> 변경 실패
         */
        questionRepository.updateUserContext(qno, context);

        return userInfoUpdate;
    }


    /**
     * @return
     * key
     * result ---> true : 값을 정상적으로 전달
     * result ---> false : error 발생
     *
     * error
     * key
     * nickErrorMessage
     * pwErrorMessage
     */
    public Map<String, Object> userInfoUpdate(String currentNick,
                                              String nick,
                                              String pw,
                                              String pwCheck,
                                              String gender,
                                              String birthday)
    {


        /** 닉네임 유효성 검사
         * 중복 x --> true
         * 중복 o --> false
         * */
        boolean isNickValid = validateHandling.nickDuplicateCheck(nick, currentNick);


        // pw 와 pwCheck 가 서로 일치하는지 판별
        // true --> 일치
        // false --> 불일치
        boolean isPwCheckValid = false;

        // 비밀번호를 변경 여부를 판별
        // true --> 변경
        // false --> 변경하지 않음
        boolean isPwChange =true;

        // 비밀번호 ===========================================================
        // 비밀번호를 바꾸지 않는 경우 ----------------------------
        // 바꾸지 않는 이유에 method 가 다르게 적용
        if(pw.equals("noChange"))
        {
            // pwCheck 가 비어있는 경우
            if(pwCheck.isEmpty())
            {
                isPwCheckValid=true;
                isPwChange = false;
            }
        }

        // 비밀번호를 바꾸지 않는 경우 ---------------------------
        else
        {
            /**
             * true --> 일치
             * false ---> 불일치
             */
            isPwCheckValid = validateHandling.isPwEqual(pw,pwCheck);
            if(validateHandling.isPwEqual(pw,pwCheck))
            {
                pw = BCrypt.hashpw(pw,BCrypt.gensalt());
            }
        }



        // ------- 생일  Data  형식을 변경 -------------------------------------------------
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthdayChanged = LocalDate.parse(birthday, formatter);



        Map<String, Object> result = new HashMap<>();

        // 아이디 중복 없고, pw 확인 까지 서로 맞을 때 -------------------------------------------
        if( (isNickValid) && (isPwCheckValid) )
        {
            result.put("result",true);
            // 비밀번호 변경을 포함 할 때 --------------------------
            if(isPwChange)
            {
                userRepository.changeUserInfo(currentNick, nick, pw, gender, birthdayChanged);
            }
            // 비밀번호 변경을 포함하지 않을 때 ---------------------
            else
            {
                userRepository.changeUserInfoExceptPw(currentNick,nick,gender,birthdayChanged);
            }

        }
        // error 가 발생한 경우  -------------------------------------------------------------------------
        else
        {
            result.put("result",false);

            // 닉네임이 중복일 경우
            if(isNickValid==false)
            {
                result.put("nickErrorMessage", "someone Using that nick name, please setting another one");
            }
            if(isPwCheckValid==false)
            {
                // password 가 일치하지 않을 경우
                result.put("pwErrorMessage","The passwords do not match each other.");
            }
        }
        return result;
    }



    /**
     *  parameter 값이 null 인지 판별하는 method -----------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public Map<String, Object> parameterValidCheck(HttpSession session, String nick, String pw, String answer, String gender, String birthday, String context)
    {

        Map<String, Object> result = new HashMap<>();

        /** parameter 들의 값들이 null 일 경우
         *  session 에서 현재 값들을 가져와서 비어있는 값들에 적용 시킨다.
         * */

        // ErrorOrNot
        boolean isValid = true;

        //------------------------------------ nick 이 유효한지 확인
        if(nick.isEmpty())
        {
            // 입력 되지 않았다면 session 에서 nick 값을 받음
            nick = (String)session.getAttribute("nick");
        }
        else
        {
            /**
             *     true --> nick 값이 유효
             *     false --> nick 값이 유효하지 않음
             */
            if(!validateHandling.nickValid(nick))
            {
                result.put("nickValidError","Korean, English, numbers excluding special characters, 2~20 digits");
                isValid=false;

            }
        }
        //------------------------------------ nick 이 유효한지 확인

        //------------------------------------ pw가 유효한지 확인
        if(pw.isEmpty())
        {
            /**
             * pw 는 복호화할 수 없기 때문에 pw 가 null 일 때 다른 method 수행 시켜야함
             * */
            pw = "noChange";
        }
        else
        {
            /**    pwValid : pw 유효성 검사
             *     true --> pw 값이 유효
             *     false --> pw 값이 유효하지 않음
             */
            if(!validateHandling.pwValid(pw))
            {
                result.put("pwValidError","Password must be 8 to 16 characters in uppercase and lowercase letters, numbers, and special characters.");
                isValid=false;
            }
        }
        //------------------------------------ pw가 유효한지 확인


        // ------------------------------------ gender 가 유효한지


        if(gender==null)
        {
            gender=(String)session.getAttribute("gender");
            if(gender.equals("Male"))
            {
                gender = "m";
            }
            else
            {
                gender = "f";
            }
        }
        // ------------------------------------ gender 가 유효한지


        // ------------------------------------ birthday 가 유효한지
        if(birthday.isEmpty())
        {
            LocalDate getBirthday= (LocalDate) session.getAttribute("birthday");
            birthday = String.valueOf(getBirthday);
        }
        // ------------------------------------ birthday 가 유효한지


        // ------------------------------------ answer 가 null 인지 아닌지
        if(answer.isEmpty())
        {
            answer = (String) session.getAttribute("answer");
        }

        else
        {
            if(!validateHandling.answerValid(answer))
            {
                result.put("answerValidError", "answer type is Korean, numeric, English 2~20 characters");
                isValid=false;
            }
        }
        // ------------------------------------ answer 가 null 인지 아닌지


        // ------------------------------------ 질문이 null 인지 아닌지
        if(context.isEmpty())
        {
            context = (String)session.getAttribute("context");

            /** 질문 변경이 없다 ---> 현재 질문에 유지
             *
             * 그에 대한 답도 없다 ----> 질문에 다한 답 또한 유지
             * */
        }

        else
        {
            /**
             * 질문이 null 이 아니고 기존의 질문과 일치하지 않다면 ----> 즉 달라진다면
             * answer 도 무조건 달라져야하는데.
             * Answer 의 값이 null 이라면 error
             *
             * */
            if((!context.equals((String)session.getAttribute("context")))&&answer==null)
            {
                result.put("errorMessage", "If you want to change a question, please enter an answer to the question");
                isValid=false;
            }

            if((context.equals((String)session.getAttribute("context")))&&answer==null)
            {
                answer = (String) session.getAttribute("answer");
            }
        }
        // ------------------------------------ 질문이 null 인지 아닌지

        result.put("isValid", isValid);

        if(isValid)
        {
            result.put("nick", nick);
            result.put("pw", pw);
            result.put("answer", answer);
            result.put("gender", gender);
            result.put("birthday", birthday);
            result.put("context", context);
        }
        return result;
    }

    /** 비밀번호가 서로 일치하면 user 상태를 회원으로 변경하는 method -------------------------------------------------------------
     */
    @Override
    public int unSubCancel(String pw,  String pwCheck, Long code) {
        // 비밀번호가 서로 일치할 때 -------------------
        if( validateHandling.isPwEqual(pw,pwCheck) )
        {

            LocalDateTime modDate = LocalDateTime.now();
            // 회원 정보를 "회원" 으로 변경 ------------------
            return userRepository.unSubCancel("회원", BCrypt.hashpw(pw,BCrypt.gensalt()), modDate, code);
        }
        // 비밀번호가 서로 일치하지 않을 때 -----------------------
        else
        {
            return -1;
        }
    }
}
