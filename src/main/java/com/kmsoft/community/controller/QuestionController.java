package com.kmsoft.community.controller;

import com.kmsoft.community.dto.CommentCreatDTO;
import com.kmsoft.community.dto.CommentDTO;
import com.kmsoft.community.dto.QuestionDTO;
import com.kmsoft.community.service.CommentService;
import com.kmsoft.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {

        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOS = commentService.listByQuestionId(id);

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOS);
        return "question";
    }
}
