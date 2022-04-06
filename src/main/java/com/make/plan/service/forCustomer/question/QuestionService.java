package com.make.plan.service.forCustomer.question;


import com.make.plan.dto.QuestionDTO;
import com.make.plan.entity.Question;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public interface QuestionService {
    default Question dtoToEntity(QuestionDTO dto){
        Question question = Question.builder()
                .qno(dto.getQno())
                .context(dto.getContext())
                .build();
        return question;
    }

    default QuestionDTO entityToDto(Question entity){
        QuestionDTO dto = QuestionDTO.builder()
                .qno(entity.getQno())
                .context(entity.getContext())
                .build();
        return dto;
    }

    public default Map<String, String> qvalidateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("qvalid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;


    }
    public Question insertQuestionTableTest(QuestionDTO dto);
}
