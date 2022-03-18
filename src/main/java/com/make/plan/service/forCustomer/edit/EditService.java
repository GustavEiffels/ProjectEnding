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

        boolean bringPwForRetire(String pw, String nick);

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


    int unSubScribeCancle(String pw, String pwCheck, Long code);
}
