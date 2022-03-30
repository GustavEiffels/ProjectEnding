package com.make.plan.service;

import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;

import java.util.List;

public interface FriendService {

//    default Friend DtoToEntity(UserDTO userDTO, FriendDTO friendDTO){
//        User user = User.builder().code(userDTO.getCode()).build();
//        User response_user = User.builder().code(friendDTO.getResponse_u()).build();
//        Friend friend = Friend.builder().fno(friendDTO.getFno()).request_u(user).response_u(response_user).status(friendDTO.getStatus()).build();
//        return friend;
//    }

    default Friend dtoToEntity(FriendDTO dto) {
        User request_u = User.builder()
                .code(dto.getRequest_u())
                .build();
        User response_u = User.builder()
                .code(dto.getResponse_u())
                .build();
        Friend friend = Friend.builder()
                .fno(dto.getFno())
                .status(dto.getStatus())
                .request_u(request_u)
                .response_u(response_u).build();
        return friend;
    }
//    default FriendDTO EntityToDto(User user,Friend friend){
//        Object object = friend.getResponse_u();
//        long object1 = (long) object;
//        FriendDTO friendDTO = FriendDTO.builder()
//                .fno(friend.getFno())
//                .request_u(user.getCode())
//                .response_u(object1)
//                .status(friend.getStatus())
//                .modDate(friend.getModDate())
//                .regDate(friend.getRegDate()).build();
//
//        return friendDTO;
//    }

    default FriendDTO entityToDTO(Friend friend, User request_u) {
        FriendDTO dto = FriendDTO.builder()
                .fno(friend.getFno())
                .status(friend.getStatus())
                .regDate(friend.getRegDate())
                .modDate(friend.getModDate())
                .request_u(request_u.getCode())
                //.response_u(response_u.getCode())
                //.request_nick(request_u.getNick()) 수정예정
                .build();
        return dto;
    }
    List<UserDTO> userSearching(String data);

    //친구요청
    Long sendRequest(FriendDTO friendDTO);
    //친구목록
    List<FriendDTO> getFriendList(Long code);
    //요청상태 변경
    void modify(FriendDTO friendDTO);

}
