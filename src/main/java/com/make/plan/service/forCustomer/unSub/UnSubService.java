package com.make.plan.service.forCustomer.unSub;


/** 계정 탈퇴 시, 사용하는 method
 */
public interface UnSubService {

    /** 회원 탈퇴를 진행하는 method ----------
     */
    boolean unSubscribing(String status, Long code);


    /** modeDate를 변경하는 method
     */
    void updateModDate(Long code );
}

