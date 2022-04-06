package com.make.plan.service.forCustomer.answer;


import com.make.plan.dto.AnswerDTO;
import com.make.plan.entity.Answer;
import com.make.plan.entity.Question;
import com.make.plan.entity.User;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public interface AnswerService {

    default Answer dtoToEntity(AnswerDTO answerDTO)
    {
        User user = User.builder()
                .code(answerDTO.getCode())
                .build();
        Question question = Question.builder()
                .qno(answerDTO.getQno())
                .build();
        Answer answer = Answer.builder()
                .ano(answerDTO.getAno())
                .code(user)
                .qno(question)
                .answer(answerDTO.getAnswer())
                .build();
        return answer;
    }

    default AnswerDTO entityToDto(Answer entity, User code , Question qno)
    {
        AnswerDTO dto = AnswerDTO.builder()
                .ano(entity.getAno())
                .code(code.getCode())
                .qno(qno.getQno())
                .answer(entity.getAnswer())
                .build();
        return dto;
    }

    public default Map<String, String> avalidateHandling(Errors errors)
    {
        Map<String, String> avalidatorResult = new HashMap<>();

        for ( FieldError error : errors.getFieldErrors() )
        {
            String validKeyName = String.format("avalid_%s", error.getField());
            avalidatorResult.put(validKeyName, error.getDefaultMessage());
        }
       return avalidatorResult;
    }
    public Long selfcheckinggood(AnswerDTO dto);

}
