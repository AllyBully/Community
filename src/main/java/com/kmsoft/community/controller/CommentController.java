package com.kmsoft.community.controller;

import com.kmsoft.community.dto.CommentDTO;
import com.kmsoft.community.dto.ResultDTO;
import com.kmsoft.community.exception.CustomizeErrorCode;
import com.kmsoft.community.model.Comment;
import com.kmsoft.community.model.User;
import com.kmsoft.community.service.CommentService;
import com.kmsoft.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(null == user)
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getAccountId());
        comment.setLikeCount(0L);
        commentService.addComment(comment);
        return ResultDTO.successOf();
    }
}
