package com.kmsoft.community.service;

import com.kmsoft.community.dto.PaginationDTO;
import com.kmsoft.community.model.Question;

public interface QuestionService {
    int addQuestion(Question question);
    PaginationDTO FindQuestionList(Integer page, Integer size);
    PaginationDTO FindQuestionList(Integer page, Integer size, String userId);
}
