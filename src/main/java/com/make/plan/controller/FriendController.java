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
import java.util.stream.Collectors;

@RestController
@RequestMapping("calendar/member")
@Log4j2
@RequiredArgsConstructor
public class FriendController {

    private final UserService userService;

    private final FriendService friendService;


    @PostMapping("/friendAdd")
    public @ResponseBody List<UserDTO> getUserInfo(@RequestBody HashMap<String, Object> data, Model model){

//        System.out.println(data);
//
//        System.out.println(data.get("data"));

//        String dataParsing = (String)data.get("data");

        List<UserDTO> userInfo = friendService.userSearching(data.get("data").toString());


//        System.out.println("send data searching result : " + userInfo);

//        model.addAttribute("userInfo", userInfo);

        return userInfo;

    }






}
