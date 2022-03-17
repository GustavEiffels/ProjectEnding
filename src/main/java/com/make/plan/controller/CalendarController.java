package com.make.plan.controller;

import com.make.plan.dto.CalendarDTO;
import com.make.plan.entity.Plan;
import com.make.plan.entity.User;
import com.make.plan.repository.PlanRepository;
import com.make.plan.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@Log4j2
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;


    @GetMapping("/getlist/{year}/{month}")
    public ResponseEntity<List<CalendarDTO>> getlist(HttpSession session, @PathVariable Integer year, @PathVariable Integer month) {
        User code = User.builder()
                .code((Long)session.getAttribute("code"))
                .build();

        month = month+1;

        // currentDate update
        String currentDate = year.toString() + ( (month / 10 > 0) ? "-" + month : ("-0" + month) ) + "-01%";
        log.info(currentDate);


//        String currentDate = year.toString() + month.toString();

        List<CalendarDTO> plans = calendarService.getcurrentplan(code, currentDate);



        return new ResponseEntity<List<CalendarDTO>>(plans, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<List<CalendarDTO>> getlistInit(HttpSession session) {
        User code = User.builder()
                .code((Long)session.getAttribute("code"))
                .build();

        LocalDate currentDate = LocalDate.now();

        List<CalendarDTO> plans = calendarService.getcurrentplan(code, currentDate.toString());


        return new ResponseEntity<List<CalendarDTO>>(plans, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<CalendarDTO> registerPlan(@RequestBody CalendarDTO calendarDTO, HttpSession session) {
        log.info("here is CalendarController. calendarDTO: " + calendarDTO);
        Long code = (Long)session.getAttribute("code");

        calendarDTO.setCode((Long)session.getAttribute("code"));

        CalendarDTO result = calendarService.register(calendarDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<CalendarDTO> modifyPlan(@RequestBody CalendarDTO calendarDTO) {
        log.info("let\'s modify plan. calendarDTO: " + calendarDTO);
        CalendarDTO result = calendarService.modify(calendarDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Long> removePlan(@PathVariable Long id) {
        log.info("let\' delete plan. id: " + id);
        calendarService.remove(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
