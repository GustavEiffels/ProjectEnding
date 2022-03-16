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


    @GetMapping("/list")
    public ResponseEntity<List<CalendarDTO>> getlist(HttpSession session) {
        User code = User.builder()
                .code((Long)session.getAttribute("code"))
                .build();


        List<CalendarDTO> plans = calendarService.getcurrentplan(code);


        return new ResponseEntity<List<CalendarDTO>>(plans, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<CalendarDTO> registerPlan(@RequestBody CalendarDTO calendarDTO) {
        log.info("here is CalendarController. calendarDTO: " + calendarDTO);
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
