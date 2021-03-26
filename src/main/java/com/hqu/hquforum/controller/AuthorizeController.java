package com.hqu.hquforum.controller;

import com.hqu.hquforum.dto.AccessTokenDTO;
import com.hqu.hquforum.dto.GithubUser;
import com.hqu.hquforum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.net.httpserver.HttpServerImpl;

import javax.servlet.http.HttpServletRequest;

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
        GithubUser user = githubProvider.getUser(accessToken);
        if (user != null){
            request.getSession().setAttribute("user",user);
            return "redirect:/";
            //登录成功，写入cookie和session
        }else{
            //登陆失败，重新登录
            return "redirect:/";
        }
    }
}
