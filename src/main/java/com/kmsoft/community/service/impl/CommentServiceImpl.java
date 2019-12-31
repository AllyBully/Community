package com.kmsoft.community.service.impl;

import com.kmsoft.community.enums.CommentTypeEnum;
import com.kmsoft.community.exception.CustomizeErrorCode;
import com.kmsoft.community.exception.CustomizeException;
import com.kmsoft.community.mapper.CommentMapper;
import com.kmsoft.community.mapper.QuestionMapper;
import com.kmsoft.community.mapper.QuestionMapperEx;
import com.kmsoft.community.model.Comment;
import com.kmsoft.community.model.Question;
import com.kmsoft.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionMapperEx questionMapperEx;

    @Override
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
}
