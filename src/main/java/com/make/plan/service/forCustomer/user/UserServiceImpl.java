package com.make.plan.service.forCustomer.user;


import com.make.plan.dto.UserDTO;
import com.make.plan.entity.User;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.forCustomer.validationHandling.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User join(UserDTO dto) throws Exception {


        User entity = dtoToEntity(dto);
        userRepository.save(entity);
        dto.setCode(entity.getCode());
        return entity;
    }

    @SneakyThrows
    @Override
    public String dec(Long code)
    {
        User user = userRepository.findByCode(code);
        String decryptComplete =  CryptoUtil.decryptAES256(user.getId(),"Id");
        return decryptComplete;
    }

    @Override
    public Boolean checkid(String id) {
        return null;
    }



    @Override
    public Boolean checkEmail(String email) {
        return null;
    }



}
