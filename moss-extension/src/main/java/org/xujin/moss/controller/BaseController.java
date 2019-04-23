package org.xujin.moss.controller;

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

    private static final Log logger = LogFactory.getLog(BaseController.class);

    protected static final ThreadLocal<HttpServletRequest> requests = new ThreadLocal();
    protected static final ThreadLocal<HttpServletResponse> responses = new ThreadLocal();

    public BaseController() {
    }

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response) {
        requests.set(request);
        responses.set(response);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest)requests.get();
    }

   public String getUserNameByToken() {
       HttpServletRequest req=(HttpServletRequest)requests.get();
       String token = req.getHeader("Token");
       return  JwtUtil.getUsername(token);
   }

   public String getRegisterSource() {
        HttpServletRequest req=(HttpServletRequest)requests.get();
        String registerSource = req.getHeader("registerSource");
        return registerSource;
    }

}
