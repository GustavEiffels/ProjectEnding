package com.make.plan;

import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;
import com.make.plan.repository.FriendRepository;
import com.make.plan.service.FriendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FriendTests {
    @Autowired
    FriendService friendService;
    @Autowired
    FriendRepository friendRepository;

    //친구요청
    @Test
    public void testSendRequest() {
        FriendDTO friendDTO = FriendDTO.builder()
                .request_u(4L)
                .response_u(1L)
                .build();
        System.out.println("친구요청성공: "+ friendService.sendRequest(friendDTO));
    }

    //상태변경
    //@Test
    public void testModify() {
        FriendDTO friendDTO = FriendDTO.builder().fno(2L).status("수락").build();
        System.out.println(friendDTO);
        friendService.modify(friendDTO);
    }


    //리스트
    @Test
    public List<FriendDTO> getFriendList() {
        List<FriendDTO> result = friendService.getFriendList(1L);
        System.out.println(result);
        return result;
    }


    @Test
    public void getFriends() {
        List<Friend> result = friendRepository.getFriendByUser(1L);
        System.out.println(result.toString());
    }


}
