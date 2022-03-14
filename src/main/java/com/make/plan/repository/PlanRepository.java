package com.make.plan.repository;

import com.make.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    //이 곳에 추후 필요한 기능을 구현할 수 있는 쿼리문을 작성하고 메소드를 선언
}
