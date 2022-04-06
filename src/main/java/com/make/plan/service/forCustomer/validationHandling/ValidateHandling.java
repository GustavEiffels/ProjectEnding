package com.make.plan.service.forCustomer.validationHandling;


import com.make.plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidateHandling {

    private  final UserRepository userRepository;

    public  Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }


    /**
     * 입력 받은 Account 가 유효한지 확인하는 method ----------------------------------------------------------------------------------------------------------------------------
     */
    public Map<String, Object> accountValidCheck(String account) throws Exception {
        Map<String, Object> result = new HashMap<>();
        account = emptyDelete(account);

        if(account.contains("@"))
        {
            result = emailValid(account);
        }
        else
        {
            result = idValid(account);
        }

        return result;
    }


    /**
     *  Account 가 Email 이라고 판별될 때 Email 의 유효성을 판별하는 method -----------------------------------------------------------------------------------------------------
     */
    Map<String, Object> emailValid(String email) throws Exception {
        Map<String, Object> result = new HashMap<>();

        // email 의 값이 유효한지를 판단
        boolean isItValid = false;


        // Email 의 정규성 검사
        if(Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",email))
        {
            /**
             * @Query(value = "select code, email, pw  nick, status, birthday from User")
             *     List<Object[]> findAllByEmail();
             */
            // userInfo[0] = code
            // userInfo[1] = email
            // userInfo[2] = pw
            // userInfo[3] = nick
            // userInfo[4] = status
            // userInfo[5] = birthday
            List<Object[]> userInfo = userRepository.findAllByEmail();


            for(Object[] info: userInfo){

                // user status 가 'expired' 이면 만료된 아이디임으로
                String status = (String)info[4];

                // status 의 값이  'expired' 가 아니라면
                if(!status.equals("expired"))
                {
                    String getEmail = (String)info[1];
                    if(CryptoUtil.decryptAES256(getEmail, "Email").equals(email))
                    {
                        isItValid=true;
                        result.put("code",(Long)info[0]);
                        result.put("pw",(String)info[2]);
                        result.put("nick",(String)info[3]);
                        result.put("status",(String)info[4]);
                        result.put("birthday",(LocalDate)info[5]);
                        break;
                    }
                }

            }
            // 반복문이 끝나더라도 만족하는 값이 없음으로
            if(!isItValid)
            {
                result.put("errorMessage","Not Valid Email, Please Check Again");
            }
        }

        // email 의 정규성을 통과하지 못함 ErrorMessage 발생
        else
        {
            result.put
                    (
                            "errorMessage",
                            "It doesn't fit the email format. Please rewrite the email format"
                    );
        }

        return result;
    }

    /** Account 가 id 이라고 판별될 때 id 값이 유효한 값인지 확인하는 method -----------------------------------------------------------------------------------------------------
     */
    Map<String, Object> idValid(String id){
        Map<String, Object> result = new HashMap<>();
        boolean isItValid = false;

        // 입력한 값이 정규식에 만족하는지 확인하기 위해서
        if(Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{6,11}$",id))
        {

            Object userInfo = userRepository.findAllById(id);

            //userInfo 가 null 이 아니라면
            if(userInfo!=null)
            {
                Object[] info = (Object[]) userInfo;
                /**
                 * @Query(value = "select u.code , u.id, u.pw, u.nick, u.status, u.birthday from User u where u.id =:id")
                 *     Object findAllById(@Param("id") String id);
                 */
                // info[0] = code
                // info[1] = id
                // info[2] = pw
                // info[3] = nick
                // info[4] = status
                // info[5] = birthday

                // status 가 유효한 값인지 확인
                String status  = (String) info[4];

                // status 값이 expired 가 아니라면
                if(!status.equals("expired"))
                {
                    isItValid = true;
                    result.put("code",(Long)info[0]);
                    result.put("pw",(String)info[2]);
                    result.put("nick",(String)info[3]);
                    result.put("status",(String)info[4]);
                    result.put("birthday",(LocalDate)info[5]);
                }
                // status 값이  expired 임으로 Error
                else
                {
                    result.put("errorMessage","This account Expired");
                }


            }

            // userInfo 가 null 이라면 존재하지 않는 account 임으로
            else
            {
                result.put("errorMessage", "No matching IDs, check again");
            }

        }

        // 입력한 값이 정규성에 만족하지 않기 때문에 ErrorMessage
        else if(!isItValid)
        {
            result.put("errorMessage","The ID format does not match. Please check again");
        }
        return result;
    }




    public boolean nickValid(String nick){
        boolean result = false;
        if(Pattern.matches("^[가-힣a-zA-Z0-9]{2,20}$",nick)){
            result = true;
        }
        return result;
    }

    public boolean pwValid(String pw) {
        boolean result = false;
        if (Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", pw))
        {
            result = true;
        }
        return result;
    }

    public boolean answerValid(String answer){
        boolean result = false;

        if(Pattern.matches("^[가-힣a-zA-Z0-9]{2,20}$",answer)){

            result = true;

        }

        return result;

    }


    /**
     *
     * @param email
     * @return
     * @throws Exception
     *
     *
     */
    boolean emailDupCheck(String email) throws Exception{
        /**
         *  email 이 중복된다면 false
         *
         */
        boolean result = false;
        List<String> list = userRepository.getAllEmail();;
        for(String i:list){

            String getEmail = CryptoUtil.decryptAES256(i, "Email");

            if(getEmail.equals(email)){

                result = true;
                break;

            }

        }
        return result;
    }
    boolean nickDupCheck(String nick){

        boolean result = false;
        List<String> list = userRepository.getAllNick();

        for(String i:list){

            if(i.equals(nick)){
                result = true;
            }
        }


        return result;
    }
    boolean idDupCheck(String id){



        boolean result = false;
        List<String> list = userRepository.getALLId();
        for(String i: list){

            if(i.equals(id)){
                result = true;
            }

        }



        return result;
    }

    /***
     *  id, email , nick  중복 검사 method + pw 확인  method
     * */



    public Map<String, String> joinValidation(String email, String id, String nick, String pw, String pwCheck) throws Exception {


        /**
         * 최종적으로 유효한 값인지 확인
         * true 이면 유효
         * fasle 이면 유효하지 않음
         */
        boolean valid = true;
        boolean emailResult = emailDupCheck(email);
        boolean idResult = idDupCheck(id);
        boolean nickResult = nickDupCheck(nick);

        /**
         * 비밀번호 체크는
         * true 일 때 서로 일치
         * false 일 때 서로 일치하지 않음
         */
        boolean pwResult = isPwEqual(pw, pwCheck);
        System.out.println(pw);
        System.out.println(pwCheck);

        /**
         *   map 생성
         *
         */
        Map<String, String> result = new HashMap<>();

        if(emailResult)
        {
            result.put("emailMessage","Someone use it already");
            valid = false;
        }

        if(idResult){
            result.put("idMessage","Someone use it already");
            valid = false;
        }

        if(nickResult)
        {
            result.put("nickMessage","Someone use it already");
            valid = false;
        }

        if(!pwResult)
        {
            result.put("pwMessage","Passwords do not match");
            valid = false;
        }

        if(valid)
        {
            result.put("result","okay");
        }
        else
        {
            result.put("result","no");
        }


        return result;
    }


    /**
     * 입력 받은 id , 혹은 email 의 공백 제거 -----------------------------------------------------------------------------
     */
    String emptyDelete(String account) {
        account = account.toLowerCase().replace(" ","");
        return account;
    }

    /** pw 와 pwCheck 가 일치하는지 판단하는 method
     */

    public boolean isPwEqual(String pw, String pwCheck)
    {
        boolean isValid = false;
        if(pw.equals(pwCheck))
        {
            isValid = true;
        }
        return isValid;
    }





    /**
     * true ---> 중복된 값이 없다
     * false ---> 중복된 값이 존재한다
     */

    public boolean nickDuplicateCheck(String nick, String currentNick) {
        boolean nickNotDuplicate = true;

        /**
         * 모든 nick name 을 들고 온다
         */
        List<String> nickDuplicateCheck = userRepository.getAllNick();

        for(String nickCheck:nickDuplicateCheck)
        {

            // nick 이 중복할 경우 ---------------------------------------
            if(nickCheck.equals(nick))
            {

                // 그 중복된 값이 현재 사용하고 있는 nick 인 경우 통과 -------------------------
                if(nickCheck.equals(currentNick))
                {
                    continue;

                    /** 그렇지 않다는 것은 다른누군가가 사용하고 있다는 말이기 때문에
                     * nickDuplicate 를 true 로 변경
                     * */
                }
                // 그 중복된 값이 현재 사용하고 있는 nick 이 아닌 경우 ----------------------------------
                // 다른 사용자가 있다는 의미
                else
                {
                    nickNotDuplicate = false;
                }

            }
        }
        return nickNotDuplicate;
    }

}
