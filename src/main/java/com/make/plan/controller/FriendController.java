package com.make.plan.controller;

import com.make.plan.dto.UserDTO;
import com.make.plan.entity.User;
import com.make.plan.service.FriendService;
import com.make.plan.service.forCustomer.find.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("calendar/member")
@Log4j2
@RequiredArgsConstructor
public class FriendController {

    private final UserService userService;

    private final FriendService friendService;


    @PostMapping("/friendSearch")
    public @ResponseBody List<UserDTO> getUserInfo(@RequestBody HashMap<String, Object> data){

        List<UserDTO> userInfo = friendService.userSearching(data.get("data").toString());

        return userInfo;

    }

    @PostMapping("/friendAdd")
    public @ResponseBody Map<String, Object> friendAdd(@RequestBody HashMap<String, Long> code, HttpSession session){
        System.out.println("friend code : " + code.get("code"));
        System.out.println("code datatype : " + code.get("code").getClass().getName());

        return friendService.friendAdd( (long)session.getAttribute("code"), code.get("code"));
    }






}
