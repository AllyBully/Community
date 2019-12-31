package com.kmsoft.community.advice;
import com.alibaba.fastjson.JSON;
import com.kmsoft.community.dto.ResultDTO;
import com.kmsoft.community.exception.CustomizeErrorCode;
import com.kmsoft.community.exception.CustomizeException;
import okio.Utf8;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, HttpServletResponse response, Throwable e, Model model) {
        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回json
            ResultDTO resultDTO = null;
            if(e instanceof CustomizeException){
                resultDTO =  ResultDTO.errorOf((CustomizeException) e);
            }else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.setStatus(200);

                response.getWriter().write(JSON.toJSONString(resultDTO));
            } catch (IOException e1) {

            }
            return null;
        }else{
            //页面跳转
            if(e instanceof CustomizeException){
                model.addAttribute("message", e.getMessage());
            }else{
                model.addAttribute("message", "服务器冒烟了，要不稍后再试试？");
            }
            return new ModelAndView("error");
        }
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
