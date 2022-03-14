package com.make.plan.service.forCustomer.duplicateCheck;


public interface PwAndDupCheck {
    /***
     *  원래는 PasswordDto 받으려고 했는데
     *  JOin 에서 사용 할 때 난잡할 것 같아서 이렇게 생성
     */

    boolean pwAndPwCheck(String pw, String pwCheck);

    boolean nickDuplicateCheck(String nick, String currentNick);


}
