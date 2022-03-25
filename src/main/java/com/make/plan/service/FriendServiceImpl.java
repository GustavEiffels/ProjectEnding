package com.make.plan.service;

import com.make.plan.dto.FriendDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;
import com.make.plan.repository.FriendRepository;
import com.make.plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;

    @Override
    public List<User> userSearching(String data) {

//        keyword = "%"+keyword+"%";

        List<User> searchUser_ID = userRepository.userSearching(data);

        if(searchUser_ID != null){
            return searchUser_ID;
        }else{
            List<User> searchUser_NICK = userRepository.userSearching_NICK(data);
            return searchUser_NICK;
        }
    }
}
