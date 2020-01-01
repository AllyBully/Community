package com.kmsoft.community.service.impl;

import com.kmsoft.community.dto.CommentDTO;
import com.kmsoft.community.enums.CommentTypeEnum;
import com.kmsoft.community.exception.CustomizeErrorCode;
import com.kmsoft.community.exception.CustomizeException;
import com.kmsoft.community.mapper.CommentMapper;
import com.kmsoft.community.mapper.QuestionMapper;
import com.kmsoft.community.mapper.QuestionMapperEx;
import com.kmsoft.community.mapper.UserMapper;
import com.kmsoft.community.model.*;
import com.kmsoft.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionMapperEx questionMapperEx;

    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public int addComment(Comment comment) {
        if(comment.getParentId() == null || comment.getParentId()==0)
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_ERROR);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            return commentMapper.insert(comment);
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            int result = commentMapper.insert(comment);
            if(result == 1){
                question.setCommentCount(1);
                questionMapperEx.incComment(question);
            }
            return result;
        }
    }

    @Override
    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if(comments.size() == 0)
            return new ArrayList<>();
        //获取去重的评论人
        Set<String> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<String> userAccountId = new ArrayList<>();
        userAccountId.addAll(commentators);

        //获取评论人并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdIn(userAccountId);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
