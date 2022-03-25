package com.make.plan.service;

import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;

import java.util.List;

public interface FriendService {
    default Friend DtoToEntity(UserDTO userDTO, FriendDTO friendDTO){
        User user = User.builder().code(userDTO.getCode()).build();
        User response_user = User.builder().code(friendDTO.getResponse_u()).build();
        Friend friend = Friend.builder().fno(friendDTO.getFno()).request_u(user).response_u(response_user).status(friendDTO.getStatus()).build();
        return friend;
    }

    default FriendDTO EntityToDto(User user,Friend friend){
        Object object = friend.getResponse_u();
        long object1 = (long) object;
        FriendDTO friendDTO = FriendDTO.builder().fno(friend.getFno()).request_u(user.getCode()).response_u(object1).status(friend.getStatus()).modDate(friend.getModDate()).regDate(friend.getRegDate()).build();

        return friendDTO;
    }

    public List<User> userSearching(String keyword);



}
