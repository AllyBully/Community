package com.kmsoft.community.controller;

import com.kmsoft.community.dto.QuestionDTO;
import com.kmsoft.community.model.Question;
import com.kmsoft.community.model.User;
import com.kmsoft.community.service.QuestionService;
import com.kmsoft.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Long id,
                       HttpServletRequest request,
                       Model model){
        User user = (User)request.getSession().getAttribute("user");
        QuestionDTO questionDTO = questionService.getById(id);
        System.out.println(user.getAccountId());
        System.out.println(questionDTO.getUser().getAccountId());
        System.out.println(user.getAccountId().equals(questionDTO.getUser().getAccountId()));
        if(!user.getAccountId().equals(questionDTO.getUser().getAccountId()))
            return "redirect:/";
        model.addAttribute("title", questionDTO.getTitle());
        model.addAttribute("description", questionDTO.getDescription());
        model.addAttribute("tag", questionDTO.getTag());
        model.addAttribute("id", questionDTO.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name="title",required=false) String title,
                            @RequestParam(name="description" ,required=false) String description,
                            @RequestParam(name="tag" ,required=false) String tag,
                            @RequestParam(name="id" ,required=false) Long id,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if(title == null || title == ""){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getAccountId());
        question.setId(id);
        if(id == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionService.addQuestion(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            questionService.updateQuestion(question);
        }


        return "redirect:/";
    }
}
