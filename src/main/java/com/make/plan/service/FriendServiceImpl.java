package com.make.plan.service;


import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Friend;
import com.make.plan.entity.User;
import com.make.plan.repository.FriendRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.forCustomer.find.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ModelMapper modelMapper;
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

    //친구요청
    @Override
    public Long sendRequest(FriendDTO dto) {
        Friend friend = dtoToEntity(dto);
        friendRepository.save(friend);
        return friend.getFno();
    }

    @Override
    public List<FriendDTO> getFriendList(Long code) {
        List<Friend> list = friendRepository.getFriendByUser(code);
        List<FriendDTO> dtoList = list.stream().map(friend -> modelMapper.map(friend, FriendDTO.class)).collect(Collectors.toList());

        return dtoList;
    }


    @Transactional
    @Override
    public void modify(FriendDTO friendDTO) {
        Optional<Friend> friend = friendRepository.findById(friendDTO.getFno());
        if(friend.isPresent()) {
            friend.get().changeStatus(friendDTO.getStatus());
            friendRepository.save(friend.get());
        }
    }
}
