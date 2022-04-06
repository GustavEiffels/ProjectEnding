package com.make.plan.repository;


import com.make.plan.entity.Answer;
import com.make.plan.entity.Question;
import com.make.plan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface AnswerRepository extends JpaRepository<Answer,Long> {

    @Query(value="select a.qno from Answer a where a.answer = :answer")
    List<Question> getQnoByUseContext(@Param("answer") String answer);

    @Query(value="select a.code from Answer a where a.qno = :qno")
    User getCodeByUseQno(@Param("qno") Question qno);



    /***
     *  code 를 입력을 받으면 answer return
     * */
    @Query(value="select a.answer from Answer a where a.code = :code")
    String getAnswerByCode(@Param("code")User code);



    @Query(value="select a.qno from Answer a where a.code =:code")
    Question getQnoByCode(@Param("code")User code);

    // User Code 를 입력하면 ano 가 출력 되도록 설정
    @Query(value = "select ano from Answer where code=:code")
    Long anoByCode(@Param("code")User code);

    // User Code를 입력하면 Answer Table 의 answer 를 수정할 수 있도록 생성
    @Modifying
    @Transactional
    @Query(value = "update Answer set answer=:answer where ano=:ano")
    int updateUserAnswer(@Param("ano") Long ano, @Param("answer")String answer);
}
