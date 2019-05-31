package org.xujin.moss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.xujin.moss.security.jwt.JwtUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BaseController用于统一获取信息
 */
public class BaseController {
    @Autowired
    HttpServletRequest httpServletRequest;


   public String getUserNameByToken() {
       HttpServletRequest req=(HttpServletRequest)httpServletRequest;
       String token = req.getHeader("Token");
       return JwtUtil.getUsername(token);
   }

   public String getRegisterSource() {
        HttpServletRequest req=(HttpServletRequest)httpServletRequest;
        String registerSource = req.getHeader("registerSource");
        return registerSource;
    }

}
