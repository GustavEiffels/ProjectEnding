package com.make.plan.repository;

import com.make.plan.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {

    @Query(value = "select q.qno from Question q where q.context =:context")
    List<Long> findByContext(@Param("context")String context);

    @Query(value = "select q.context from Question q where q.qno =:qno")
    String getContextByQno(@Param("qno")Long qno);

    // qno 를 입력하면 Question table 의 context column 을 설정하게 해주는 method
    @Modifying
    @Transactional
    @Query(value = "update Question set context=:context where qno=:qno")
    int updateUserContext(@Param("qno")Long qno, @Param("context")String context);



}
