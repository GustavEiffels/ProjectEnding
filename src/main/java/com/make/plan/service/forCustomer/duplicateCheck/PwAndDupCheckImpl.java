package com.make.plan.service.forCustomer.duplicateCheck;

import com.make.plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class PwAndDupCheckImpl implements PwAndDupCheck{

    @Override
    public boolean pwAndPwCheck(String pw, String pwCheck) {
        boolean pwSameCheck = false;
        if(pw.equals(pwCheck)){
            pwSameCheck = true;
        }
        return pwSameCheck;
    }


    private  final UserRepository userRepository;


    /***
     * return 결과과 true 이면 닉네임이 중복된다는 얘기
     * return 결과가 false 이면 닉네임 중복이 아니라는 신호
     */

    @Override
    public boolean nickDuplicateCheck(String nick, String currentNick) {
        boolean nickNotDuplicate = true;

        /** nick name 을 전부 list 로 들고온다
         *
         * currentNick 은 현재 nick
         * */
        List<String> nickDuplicateCheck = userRepository.getAllNick();

        for(String nickCheck:nickDuplicateCheck){

            /**
             * list 안의 요소 nickCheck 가 바꾸려고 하는 요소 nick 이랑 같다면
             * */
            if(nickCheck.equals(nick)){

                /** 이 상황에서 현재 닉네임이랑 nickCheck 랑 같다면 continue
                 * */
                if(nickCheck.equals(currentNick)){
                    continue;

                    /** 그렇지 않다는 것은 다른누군가가 사용하고 있다는 말이기 때문에
                     * nickDuplicate 를 true 로 변경
                     * */
                }else {
                    nickNotDuplicate = false;
                }
            }
        }
        return nickNotDuplicate;
    }
}
