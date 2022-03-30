package com.make.plan.service;


import com.make.plan.dto.UserDTO;
import com.make.plan.entity.User;
import com.make.plan.repository.FriendRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.forCustomer.find.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        System.out.println(searchUser_ID.size());

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
}
