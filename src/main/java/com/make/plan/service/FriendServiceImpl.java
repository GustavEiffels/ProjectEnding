package com.make.plan.service;


import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;
import com.make.plan.repository.FriendRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.forCustomer.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class FriendServiceImpl implements FriendService {

    private final UserService userService;

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> userSearching(String data) {

        data = "%"+data+"%";

        List<User> searchUser_ID = userRepository.userSearching(data);

        if(searchUser_ID.size() != 0){

            return searchUser_ID.stream().map(user -> {
                try {
                    return userService.entityToDto(user);
                } catch (Exception e) {
                    log.info(e.getLocalizedMessage());
                }
                return null;
            }).collect(Collectors.toList());
        }else {
            List<User> searchUser_NICK = userRepository.userSearching_NICK(data);
            return searchUser_NICK.stream().map(user -> {
                try {
                    return userService.entityToDto(user);
                } catch (Exception e) {
                    log.info(e.getLocalizedMessage());
                }
                return null;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public Map<String, Object> friendAdd(long code, Long code1) {

        User myCode = User.builder()
                .code(code)
                .build();

        User responseCode = User.builder()
                .code(code1)
                .build();

        int result = friendRepository.friendAdd(myCode, responseCode);

        Map<String, Object> map = new HashMap<>();

        map.put("result","friend request sending!!!");

        if(result != 0){
            map.put("result", "friend request send succes!!!");
        }else{
            map.put("result", "friend request send fail");
        }

        return map;
    }

    @Override
    public void autoFriendAdd(FriendDTO friendDTO) {
        Friend friend = DtoToEntity(friendDTO);
        friendRepository.save(friend);
    }

    @Override
    public List<Map<String, Object>> friendList(long code) {
        List<Map<String, Object>> friendList = friendRepository.friendList(code);

        return friendList;
    }

    @Override
    public List<String> friendRequestList(long code) {
        List<String> friendRequest = friendRepository.requestFriendAdd(code);
        return friendRequest;
    }

    @Override
    public List<Map<String, Object>> friendRequestListMapType(long code) {

        List<Map<String, Object>> result = friendRepository.requestFriendAddMapType(code);

        return result;
    }

    @Override
    public void friendInfoUpdate(long code, String statusUp, Long response) {

        User myCode = User.builder()
                .code(code)
                .build();

        User friendCode = User.builder()
                .code(response)
                .build();


        int result = friendRepository.friendInfoUpdate(myCode, statusUp, friendCode);

        if(result != 0){
            log.info("friend Status Info update OK!!!");
        }else{
            log.info("friend Status Info update failed......");
        }


    }


}
