package com.make.plan.service.forCustomer.edit;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

public interface EditService {
    Map<String, Object> bringUserInfo(String nick) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException;

    String bringAnswerInfo(Long code);
    //
    String bringQuestionInfo(Long code);

    /** 입력한 비밀번호가, 사용자의 비밀번호와 같은지 확인하는 method
     */
    boolean isPwEqual(String pw, Long code);

    // 실제로 값을 바꾸는 method
    Map<String, Object> changeUserInfo(HttpSession session,String currentNick,
                                       String nickCh,
                                       String pw,
                                       String pwCheck,
                                       String gender,
                                       String birthday,
                                       String answer,
                                       String context);

    Map<String , Object> parameterValidCheck(HttpSession session,
                                             String nick,
                                             String pw,
                                             String answer,
                                             String gender,
                                             String birthday,
                                             String context
    );

    /** 비밀번호가 서로 일치하면 user 상태를 회원으로 변경하는 method -------------------------------------------------------------
     */
    int unSubCancel(String pw, String pwCheck, Long code);
}
