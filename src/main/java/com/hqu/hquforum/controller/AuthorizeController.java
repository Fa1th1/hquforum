package com.hqu.hquforum.controller;

import com.hqu.hquforum.dto.AccessTokenDTO;
import com.hqu.hquforum.dto.GithubUser;
import com.hqu.hquforum.mapper.UserMapper;
import com.hqu.hquforum.model.User;
import com.hqu.hquforum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.net.httpserver.HttpServerImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Drf
 * @create 2021-03-26-2:08
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name ="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.insert(user);
            //登录成功，写入cookie和session
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";

        }else{
            //登陆失败，重新登录
            return "redirect:/";
        }
    }
}
