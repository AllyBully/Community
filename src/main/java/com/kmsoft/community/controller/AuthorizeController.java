package com.kmsoft.community.controller;

import com.kmsoft.community.dto.AccessTokenDTO;
import com.kmsoft.community.dto.GithubUser;
import com.kmsoft.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String ClientId;

    @Value("${github.redirect.url}")
    private String RedirectUrl;

    @Value("${github.client.secret}")
    private String clientSecret;
    @RequestMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_Id(ClientId);
        accessTokenDTO.setRedirect_url(RedirectUrl);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        if(null != user){
            //登录成功
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }


}
