package com.example.community.controller;

import com.example.community.dto.AccessTokenDto;
import com.example.community.dto.GithubUser;
import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
import com.example.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String client_serect;
    @Value("${hithub.redirect.uri}")
    private String redirect_uri;
    @Autowired
    private GithubProvider githubProvider;
    @Autowired()
    private UserMapper userMapper;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        // 收到code，结束第一步，开始第二部
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setCode(code);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(client_serect);
        accessTokenDto.setState(state);
        accessTokenDto.setRedirect_uri(redirect_uri);
        String acccessToken =  githubProvider.getAccesstoken(accessTokenDto);// 第二步中的回来
        //  到这里是第三步开始
        GithubUser githubUser = githubProvider.getUser(acccessToken);
        if(githubUser!=null){
            User user = new User();
            request.getSession().setAttribute("user",githubUser);
            user.setAccountid(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            userMapper.insert(user);

            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
}
