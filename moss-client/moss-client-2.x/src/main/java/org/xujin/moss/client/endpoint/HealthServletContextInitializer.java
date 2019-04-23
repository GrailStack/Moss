package org.xujin.moss.client.endpoint;

import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Program: moss
 * @Description:
 * @Author: xujin
 * @Create: 2019/2/25 14:51
 **/
public class HealthServletContextInitializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                req.getRequestDispatcher("/actuator/health").forward(req,resp);
            }
        };
        ServletRegistration.Dynamic reg = servletContext.addServlet("healthPlatform", servlet);
        reg.addMapping("/health");
    }

}
