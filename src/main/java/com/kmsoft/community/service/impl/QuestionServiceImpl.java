package com.kmsoft.community.service.impl;

import com.kmsoft.community.dto.PaginationDTO;
import com.kmsoft.community.dto.QuestionDTO;
import com.kmsoft.community.exception.CustomizeErrorCode;
import com.kmsoft.community.exception.CustomizeException;
import com.kmsoft.community.mapper.QuestionMapper;
import com.kmsoft.community.mapper.QuestionMapperEx;
import com.kmsoft.community.mapper.UserMapper;
import com.kmsoft.community.model.Question;
import com.kmsoft.community.model.QuestionExample;
import com.kmsoft.community.model.User;
import com.kmsoft.community.model.UserExample;
import com.kmsoft.community.service.QuestionService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapperEx questionMapperEx;

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
            totalCount = (int)questionMapper.countByExample(new QuestionExample());
        else{
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andCreatorEqualTo(userId);
            totalCount = (int)questionMapper.countByExample(questionExample);
        }
        Integer totalPage = (int) Math.ceil((double)totalCount/size);
        if(page < 1) page = 1;
        if(page > totalPage) page = totalPage;

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(totalPage, page, size);
        Integer offset = (page-1)*size;
        List<Question> questions;
        if(userId == null)
            questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        else{
            QuestionExample example = new QuestionExample();
            example.createCriteria().andCreatorEqualTo(userId);
            questions = questionMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        }
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(users.get(0));
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOS);


        return paginationDTO;
    }

    @Override
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(example);
        questionDTO.setUser(users.get(0));
        return questionDTO;
    }

    @Override
    public int updateQuestion(Question question) {
        int result = questionMapper.updateByPrimaryKeySelective(question);
        if(result != 1)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        return result;
    }

    @Override
    public int incView(Long id) {
        Question newQuestion = new Question();
        newQuestion.setId(id);
        newQuestion.setViewCount(1);
        return questionMapperEx.incView(newQuestion);
    }


}
