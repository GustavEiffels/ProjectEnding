package com.make.plan;

import com.make.plan.dto.UserDTO;
import com.make.plan.entity.Plan;

import com.make.plan.entity.User;
import com.make.plan.repository.FriendRepository;
import com.make.plan.repository.PlanRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.CalendarService;

import com.make.plan.service.FriendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@SpringBootTest
public class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

//    @Test
    public void insertPlans() {
        Random r = new Random();
        String[] colors = {"#dcced3", "#d1bec7", "#c7b0bc", "#876479", "#674559", "#4c3041"};

        IntStream.rangeClosed(1, 11).forEach(i -> {
            int priority = 1 + r.nextInt(5);
            int allDay = r.nextInt(2);
            int pDate = 10 + r.nextInt(16);
            int pHour = 10 + r.nextInt(12);
            int pMinute = 10 + r.nextInt(50);
            String textColor = "gray";
            String start = "2022-01-" + pDate + "T" + pHour + ":" + pMinute;
            String end = "2022-01-" + (pDate + priority) + "T" + pHour + ":" + pMinute;
            String backgroundColor = colors[priority - 1];
            String borderColor = colors[priority];
            if (allDay == 0) {
                end = "2022-01-" + pDate + "T" + (pHour + 1) + ":" + pMinute;;
            }
            if (priority > 2) {
                textColor = "white";
            }
            User code = User.builder()
                    .code(2L)
                    .build();
            System.out.println(code);

            Plan plan = Plan.builder()
                    .title("Title_yayababa" + i)
                    .description("Description_" + i)
                    .location("Location_" + i)
                    .priority(priority)
                    .allDay(allDay)
                    .start(LocalDateTime.parse(start))
                    .end(LocalDateTime.parse(end))
                    .backgroundColor(backgroundColor)
                    .borderColor(borderColor)
                    .textColor(textColor)
                    .user(code)
                    .build();
            planRepository.save(plan);
        });
    }

//    @Test
    public void LocalDateTimeParsingTest() {
        String date = "2022-03-01T00:00";
        System.out.println("date: " + date);
        LocalDateTime first = LocalDateTime.parse(date);
        System.out.println("first: " + first);
        date = first.toString();
        System.out.println("date: " + date);

    }

    @Autowired
    private CalendarService calendarService;


//    @Test
//    public List<CalendarDTO> test(){
//        User code =User.builder()
//                .code(2L)
//                .build();
//
//        String currentDate = "2022-03";
//
//        List<Plan> plans = planRepository.getUserPlan(code, currentDate);
//
//
//        return plans.stream().map(plan -> calendarService.entityToDTO(plan)).collect(Collectors.toList());
//
//    }


    @Autowired
    private UserRepository userRepository;

//    @Test
    public void userEmailList(){
        List<Object[]> userInfo = userRepository.findAllByEmail();

       List<String> userList = new ArrayList<String>();

       for (Object[] list : userInfo){
           userList.add(userInfo.get(1).toString());
       }


        // 객체의 값이 나와버림
        for (String user : userList){
            System.out.println(user.toString());
        }


//        Object[] userEmail = userInfo.get(1);


    }

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;

//    @Test
    public void friendAddRequest(){

        User myCode = User.builder()
                .code(2L)
                .build();

        User code = User.builder()
                .code(1L)
                .build();

        Map<String, Object> map = new HashMap<>();

        int rowCount = friendRepository.friendAdd(myCode, code);


        if(rowCount == 0){
            map.put("result", "NO!!!!!!!");
        }else{
            map.put("result", "OK!!!!");
        }

        System.out.println(map);


    }


    @Test
    @Transactional
    public void friendRequestList(){

        List<String> friendRequest = friendRepository.requestFriendAdd(1L);

        List<Map<String, Object>> result = friendRepository.requestFriendAddMapType(1L);
//        System.out.println(friendRequest.stream().map(friend -> friendService.EntityToDto(friend)).collect(Collectors.toList()));
//        System.out.println(friendRequest.toString());
//        System.out.println(friendRequest.get(0));
//        System.out.println(friendRequest.get(0));
//        System.out.println(friendRequest.get(1));

        System.out.println(result.get(0).keySet());
//        System.out.println(result.get(0).get("status"));
    }

//    @Test
    public void friendList(){

        List<Map<String, Object>> friendInfo = friendService.friendList(2L);
        System.out.println(friendInfo.get(0).keySet());


    }

    @Test
    public void friendInfoUpdateTest(){

        User myCode = User.builder()
                .code(2L)
                .build();

        User friendCode = User.builder()
                .code(4L)
                .build();

        int result = friendRepository.friendInfoUpdate(myCode, "수락", friendCode);
        System.out.println(result);

    }






}
