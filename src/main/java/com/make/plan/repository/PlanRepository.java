package com.make.plan.repository;

import com.make.plan.dto.CalendarDTO;
import com.make.plan.entity.Plan;
import com.make.plan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query(value = "select * from Plan where code= :code and start BETWEEN DATE_ADD(:currentDate, INTERVAL -1 MONTH) And DATE_ADD(:currentDate, INTERVAL 1 MONTH)", nativeQuery = true)
    List<Plan> getUserPlan(@Param("code") User code, @Param("currentDate")String currentDate);

}
