package com.make.plan.service.forCustomer.find;


import com.make.plan.dto.findDTO.ByQandAandBirthdayDTO;
import com.make.plan.dto.findDTO.NickDTO;


import java.util.Map;

public interface FindService {

    /**@param dto
     * @return
     * @throws Exception
     * -------------------------------- findByNick : 닉네임으로 아이디 , Email 찾기
     */
     Map<String, Object> findByNick(NickDTO dto) throws Exception;



    /** 2. pw 변경 ==================================================================================
     * 입력한 id 혹은 email 과 입력한 user 정보가 맞다면 비밀번호를 변경 하고 email 로 변경된 password 를 전송
     * **/
    Map<String , Object> resultAccountValid(String userInfo, ByQandAandBirthdayDTO dto) throws Exception;

    void sendNewPassword(String userInfo, ByQandAandBirthdayDTO dto) throws Exception;
    /** 2. pw 변경 ==================================================================================
     *
     */

}
