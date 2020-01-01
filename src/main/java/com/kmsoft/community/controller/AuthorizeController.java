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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_Id(ClientId);
        accessTokenDTO.setRedirect_url(RedirectUrl);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(null != githubUser && githubUser.getId() != null){
            //登录成功
            //request.getSession().setAttribute("user", githubUser);
            List<User> userList = userService.FindByAccountId(String.valueOf(githubUser.getId()));
            //收集用户最新信息
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setGmtModified(System.currentTimeMillis());
            user.setBio(githubUser.getBio());
            user.setAvatarUrl(githubUser.getAvatar_url());
            if(userList.size() == 0){
                //录入新用户
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userService.addUser(user);
            }else{
                //更新用户
                User dbUser = userList.get(0);
                user.setId(dbUser.getId());
                userService.updateUser(user);
            }
            Cookie cookie = new Cookie("token", user.getToken());
            cookie.setMaxAge(7*24*60*60);
            response.addCookie(cookie);
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }

    @RequestMapping("/logout")
    public String loginOut(HttpServletRequest request,
                           HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
