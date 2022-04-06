package com.make.plan.service.forCustomer.find;


import com.make.plan.service.forCustomer.validationHandling.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MaskingService {


    /**
     *  Object[0] = id
     *  Object[1] = email
     */
    Map<String,Object> masking(Object[] userInfo) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {

        Map<String, Object> maskingResult = new HashMap<>();

        // 1. id 값
        String id = (String)userInfo[0];

        // 2. email 값
        String email = (String)userInfo[1];


        email = maskingEmail(email);
        id = maskingId(id);


        maskingResult.put("email",email);
        maskingResult.put("id",id);


        return maskingResult;
    }


    /**
     * email 값 받아서 masking 하는 Method
     */
    String maskingEmail(String email) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        // Email 은 암호화가 되어있음으로 복호화를 실행
        String result = null;

        email = CryptoUtil.decryptAES256(email, "Email");

        // 복호화를 확인
        System.out.println("마스킹 되기 전 Email : " + email);

        // @를 중심으로 나누어 배열로 저장
        String[] emailFront = email.split("@");
        int frontLength = emailFront[0].length();


        if (frontLength > 3) {
            String noMask = emailFront[0].substring(0, 3);
            // 값을 확인
//            System.out.println(noMask);
            String Masking = "*".repeat(frontLength - 3);

            // 값을 확인
//            System.out.println(Masking);
            result = noMask + Masking;
        }

        int len_b = emailFront[1].indexOf(".");
        String masking_b = "*".repeat(len_b);
        String other = emailFront[1].substring(len_b);

        result = result + "@" + masking_b + other;
        return result;
    }

    /**
     * id  값 받아서 masking 하는 Method
     */
    String maskingId(String id){

        int frontLength = id.length();

        String noMasking = id.substring(0, 3);
        String masking = "*".repeat(frontLength - 3);

        String result = noMasking + masking;

        return result;
    }
}
