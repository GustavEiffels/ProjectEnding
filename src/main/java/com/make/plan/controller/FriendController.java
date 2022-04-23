package com.make.plan.controller;

import com.make.plan.dto.FriendDTO;
import com.make.plan.dto.UserDTO;
import com.make.plan.service.FriendService;
import com.make.plan.service.forCustomer.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@RequestMapping("/member")
@Controller
@Log4j2
@RequiredArgsConstructor
public class FriendController {

    private final UserService userService;

    private final FriendService friendService;


    @PostMapping("/friendSearch")
    public @ResponseBody List<UserDTO> getUserInfo(@RequestBody HashMap<String, Object> data){

        List<UserDTO> userInfo = friendService.userSearching(data.get("data").toString());
        System.out.println(userInfo);

        return userInfo;

    }


    /*@PostMapping("/friendAdd")
    public @ResponseBody Map<String, Object> friendAdd(@RequestBody HashMap<String, Long> code, HttpSession session){
        System.out.println("friend code : " + code.get("code"));
        System.out.println("code datatype : " + code.get("code").getClass().getName());

        return friendService.friendAdd( (long)session.getAttribute("code"), code.get("code"));
    }*/

    // 친구등록에 현재 사용하고 있는 메서드 , 위 매서드와 서비스는 동일하나 리턴에 대한 부분이 다름
    // return 설정을 해서 ajax에서 데이터 받아서 성공 여부 처리 하는 방식으로 수정 필요
    @PostMapping("/friendAdd")
    public @ResponseBody Map<String, Object> friendAdd(@RequestBody FriendDTO friendDTO, HttpSession session){
        friendDTO.setRequest_u((Long) session.getAttribute("code"));
        friendService.autoFriendAdd(friendDTO);

        return null;
    }


    @RequestMapping(value = "", method = {RequestMethod.GET,RequestMethod.POST})
    public String friendInfo(HttpSession session, Model model){

        List<Map<String, Object>> friendList = friendService.friendList((long)session.getAttribute("code"));

        model.addAttribute("friendList", friendList);

        List<Map<String, Object>> result = friendService.friendRequestListMapType((long)session.getAttribute("code"));

        model.addAttribute("friendRequestInfo", result);

        return "member/list";
    }


    /*@RequestMapping(value = "", method = RequestMethod.GET)
    public String friendInfo(HttpSession session, Model model){

        return "member/list";
    }

    @RequestMapping(value = "/", method = {RequestMethod.POST})
    public void friendInfo(HttpSession session, Model model, @RequestBody HashMap<String, Object> tabID){

        System.out.println(tabID.get("currentTabId"));



        List<Map<String, Object>> result = friendService.friendRequestListMapType((long)session.getAttribute("code"));

        model.addAttribute("friendRequestInfo", result);

    }*/

    @PostMapping("/update")
    public @ResponseBody void friendInfoUpdate(@RequestBody HashMap<String, Object> status, HttpSession session, Model model){

        String statusData = status.get("status").toString().substring(0, status.get("status").toString().length()-1);
        String code = status.get("status").toString().substring(status.get("status").toString().length()-1);

        Long response = Long.parseLong(code);
        
        String statusUp = statusData;

        if(statusData.equals("accept")){
            statusUp = "수락";
        }else if(statusData.equals("refuse")){
            statusUp = "거절";
        }else if(statusData.equals("delete")){
            statusUp = "삭제";
        }

        friendService.friendInfoUpdate((long)session.getAttribute("code"), statusUp, response);

    }










}
