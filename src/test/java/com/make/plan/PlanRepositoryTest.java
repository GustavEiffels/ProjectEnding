package com.make.plan;

import com.make.plan.dto.CalendarDTO;
import com.make.plan.entity.Plan;

import com.make.plan.entity.User;
import com.make.plan.repository.PlanRepository;
import com.make.plan.repository.UserRepository;
import com.make.plan.service.CalendarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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







}
