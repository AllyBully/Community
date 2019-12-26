package com.kmsoft.community.controller;

import com.kmsoft.community.dto.AccessTokenDTO;
import com.kmsoft.community.dto.GithubUser;
import com.kmsoft.community.model.User;
import com.kmsoft.community.provider.GithubProvider;
import com.kmsoft.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserService userService;

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
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(null != githubUser){
            //登录成功
            request.getSession().setAttribute("user", githubUser);
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userService.addUser(user);
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }


}
