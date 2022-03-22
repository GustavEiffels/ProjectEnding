package com.make.plan.service.forCustomer.unSub;
import com.make.plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

/** 계정 탈퇴 시, 사용하는 method
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class UnSubServiceImpl implements UnSubService {
    private final UserRepository userRepository;


    /** 회원 탈퇴를 진행하는 method
     */
    @Override
    public boolean unSubscribing(String status, Long code)
    {
        boolean isUnSub =false;

        // 어떠한 오류로 인해 Update 가 안될 경우를 방지
        if(userRepository.unSub(status, code)>0)
            {
                isUnSub = true;
            }
        return isUnSub;
    }


    /** mode date를 변경하는 Method
     */
    @Override
    public void updateModDate(Long code)
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        userRepository.updateModDateTemp(localDateTime,code);
    }
}
