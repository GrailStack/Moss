package org.xujin.moss.client.config;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * 在外置 Tomcat 下，Spring Cloud Discovery Client 的注册
 * 依赖 ServletWebServerInitializedEvent 事件，官方暂时没有
 * 修复计划，只能手动修复。
 *
 * 通过监听 Spring Applicaiton Running 的回调
 *
 * 此外，在外置 Tomcat 下，ManagementContext 无法另起端口，只能
 * 复用默认端口。通过 MXBeanServer 应该可以开启另一个监听端口，既然
 * 官方暂时未支持，我们也暂时不去修复。
 */
public class DiscoveryClientRegistrationInvoker implements
        ApplicationContextCustomizer<ConfigurableApplicationContext>,
        Ordered {
    @Override
    public void customize(ConfigurableApplicationContext context) {
        if(context instanceof ServletWebServerApplicationContext
                && !AdminEndpointApplicationRunListener.isEmbeddedServletServer(context.getEnvironment())) {
            MetaDataProvider metaDataProvider = context.getBean(MetaDataProvider.class);
            WebServer webServer = new WebServer() {
                @Override
                public void start() throws WebServerException {

                }

                @Override
                public void stop() throws WebServerException {

                }

                @Override
                public int getPort() {
                    return metaDataProvider.getServerPort();
                }
            };
            context.publishEvent(
                    new ServletWebServerInitializedEvent(
                            webServer,
                            new ServletWebServerApplicationContext())
            );
        }
    }
    @Override
    public int getOrder() {
        return -1; // Priority of AbstractAutoServiceRegistration
    }
}
