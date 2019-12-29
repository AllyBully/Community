package com.kmsoft.community.service.impl;

import com.kmsoft.community.dto.PaginationDTO;
import com.kmsoft.community.dto.QuestionDTO;
import com.kmsoft.community.mapper.QuestionMapper;
import com.kmsoft.community.mapper.UserMapper;
import com.kmsoft.community.model.Question;
import com.kmsoft.community.model.User;
import com.kmsoft.community.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public int addQuestion(Question question) {
        return questionMapper.insertSelective(question);
    }

    @Override
    public PaginationDTO FindQuestionList(Integer page, Integer size) {
        return FindQuestionList(page,size,null);
    }

    @Override
    public PaginationDTO FindQuestionList(Integer page ,Integer size, String userId) {
        Integer totalCount = 0;
        if(userId == null)
            totalCount = questionMapper.count();
        else
            totalCount = questionMapper.countByCreator(userId);
        Integer totalPage = (int) Math.ceil((double)totalCount/size);
        if(page < 1) page = 1;
        if(page > totalPage) page = totalPage;

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalPage, page, size);


        Map<String ,Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("startRow",(page-1)*size);
        paramMap.put("pageSize",size);
        List<Question> questions = new ArrayList<>();
        if(userId == null)
            questions = questionMapper.selectByPage(paramMap);
        else{
            paramMap.put("creator",userId);
            questions = questionMapper.selectByCreator(paramMap);
        }
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByAccountId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOS);


        return paginationDTO;
    }




}
