package com.make.plan.service;

import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;

import java.util.List;
import java.util.Map;

public interface FriendService {

    default Friend DtoToEntity(FriendDTO friendDTO){

        User user = User.builder().
                code(friendDTO.getRequest_u())
                .build();

        User response_user = User.builder().
                code(friendDTO.getResponse_u())
                .build();

        Friend friend = Friend.builder().
                fno(friendDTO.getFno()).
                request_u(user).
                response_u(response_user).
                status(friendDTO.getStatus())
                .build();
        return friend;
    }

    default FriendDTO EntityToDto(Friend friend){

        Object request_u = friend.getRequest_u();

        Long request = (Long)request_u;

        Object response_u = friend.getResponse_u();

        Long response = (Long)response_u;

//        User user = User.builder()
//                .code((long)friend.getRequest_u())
//                .build();
//
//        UserDTO userDTO = UserDTO.builder()
//                .code()
//                .build();




        FriendDTO friendDTO = FriendDTO.builder().
                fno(friend.getFno()).
                request_u(request).
                response_u(response).
                status(friend.getStatus()).
                modDate(friend.getModDate()).
                regDate(friend.getRegDate())
                .build();

        return friendDTO;
    }

    List<UserDTO> userSearching(String data);

    Map<String, Object> friendAdd(long code, Long code1);

    void autoFriendAdd(FriendDTO friendDTO);

    List<Map<String, Object>> friendList(long code);

    List<String> friendRequestList(long code);

    List<Map<String, Object>> friendRequestListMapType(long code);

    void friendInfoUpdate(long code, String statusUp, Long response);
}
