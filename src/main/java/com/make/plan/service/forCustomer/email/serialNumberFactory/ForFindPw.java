package com.make.plan.service.forCustomer.email.serialNumberFactory;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ForFindPw {
    private int serialLength = 12;
    private final char[] passwordTableUpper =  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'};
    private final char[] passwordTableLower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z',};
    private final char[] passwordTableNumber = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

    private final char[] passwordTableSpecial ={'`','~','!','@','#','$','%','^','&','*'};
    public String excuteGenerate() {
        Random random = new Random(System.currentTimeMillis());
        int tablelengthUpper = passwordTableUpper.length;
        int tablelengthLower = passwordTableLower.length;
        int tablelengthNumber = passwordTableNumber.length;
        int tablelengthSpecial = passwordTableSpecial.length;
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < serialLength/4; i++) {
            buf.append(passwordTableUpper[random.nextInt(tablelengthUpper)]);
            buf.append(passwordTableLower[random.nextInt(tablelengthLower)]);
            buf.append(passwordTableNumber[random.nextInt(tablelengthNumber)]);
            buf.append(passwordTableSpecial[random.nextInt(tablelengthSpecial)]);
        }
        return buf.toString();
    }

    public int getPwdLength() {
        return serialLength;
    }

    public void setPwdLength(int serialLength) {
        this.serialLength = serialLength;
    }
}
