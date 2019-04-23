package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.security.jwt.JwtToken;
import org.xujin.moss.security.jwt.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class LoginController {

    @PostMapping("/login")
    public ResultData login(String username, String password) {
        try {
            String token= JwtUtil.createToken(username,password);
            Date tokenExpired = new Date(new Date().getTime() + JwtUtil.EXPIRE_TIME);
            JwtToken jwtToken = new JwtToken(token);
            Subject subject = SecurityUtils.getSubject();
            subject.login(jwtToken);
            Map<String, Object> userInfo = new HashMap<String, Object>();
            userInfo.put("token",token);
            userInfo.put("userName",username);
            userInfo.put("tokenExpired",tokenExpired.getTime());
            return ResultData.builder().data(userInfo).build();
        } catch (AuthenticationException e) {
            return ResultData.builder().code(200).msgCode("400").msgContent("login fail").build();
        }
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }

}
