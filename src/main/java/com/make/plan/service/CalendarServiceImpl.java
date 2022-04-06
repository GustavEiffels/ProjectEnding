package com.make.plan.service;

import com.make.plan.dto.CalendarDTO;
import com.make.plan.entity.Plan;
import com.make.plan.entity.User;
import com.make.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final PlanRepository planRepository;

    @Override
    public List<CalendarDTO> getAll() {
        List<Plan> plans = planRepository.findAll();
        log.info("here is CalendarServiceImpl. plans: " + plans);
        return plans.stream().map(plan -> entityToDTO(plan)).collect(Collectors.toList());
    }

    @Override
    public CalendarDTO register(CalendarDTO calendarDTO) {
        Plan plan = dtoToEntity(calendarDTO);
        planRepository.save(plan);
        return entityToDTO(plan);
    }

    @Override
    public CalendarDTO modify(CalendarDTO calendarDTO) {
        Optional<Plan> result = planRepository.findById(calendarDTO.getId());
        Plan plan;
        Plan data = dtoToEntity(calendarDTO);
        if(result.isPresent()){
            plan = result.get();
            plan.changeTitle(data.getTitle());
            plan.changeDescription(data.getDescription());
            plan.changeLocation(data.getLocation());
            plan.changePriority(data.getPriority());
            plan.changeAllDay(data.getAllDay());
            plan.changeStart(data.getStart());
            plan.changeEnd(data.getEnd());
            plan.changeBackgroundColor(data.getBackgroundColor());
            plan.changeBorderColor(data.getBorderColor());
            plan.changeTextColor(data.getTextColor());

            planRepository.save(plan);
            return entityToDTO(plan);
        }else {
            return null;
        }

    }

    @Override
    public Long remove(Long id) {
        planRepository.deleteById(id);
        return id;
    }

    @Override
    public List<CalendarDTO> getcurrentplan(User code, String currentDate)
    {
        List<Plan> plans = planRepository.getUserPlan(code, currentDate);
        log.info("here is CalendarServiceImpl. plans: " + plans);
        return plans.stream().map(plan -> entityToDTO(plan)).collect(Collectors.toList());
    }
}
