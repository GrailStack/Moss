package org.xujin.moss.client.endpoint;

import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;

public class PlatformManagementWebServerFactoryCustomizer implements WebServerFactoryCustomizer {

    @Override
    public void customize(WebServerFactory factory) {
        if (factory instanceof AbstractServletWebServerFactory) {
            AbstractServletWebServerFactory servletWebServerFactory = (AbstractServletWebServerFactory) factory;
            //管理端口8081,只给管控端口加 /health
            if(servletWebServerFactory.getPort()==8081){
                servletWebServerFactory.addInitializers(new HealthServletContextInitializer());
            }
        }
    }
}
