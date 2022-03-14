package com.make.plan.service.forCustomer.answer;


import com.make.plan.dto.AnswerDTO;
import com.make.plan.entity.Answer;
import com.make.plan.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public Long selfcheckinggood(AnswerDTO dto) {
        Answer entity = dtoToEntity(dto);
        answerRepository.save(entity);
        return entity.getAno();
    }
}
