package org.xujin.moss.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * 在外置 Tomcat 下，Spring Cloud Discovery Client 的注册
 * 依赖 ServletWebServerInitializedEvent 事件，官方暂时没有
 * 修复计划，只能手动修复。
 * 通过监听 Spring Applicaiton finished 的回调
 * <p>
 * 此外，在外置 Tomcat 下，ManagementContext 无法另起端口，只能
 * 复用默认端口。通过 MXBeanServer 应该可以开启另一个监听端口，既然
 * 官方暂时未支持，我们也暂时不去修复。
 */
public class DiscoveryClientRegistrationInvoker implements
        ApplicationContextCustomizer<ConfigurableApplicationContext>,
        Ordered {
    @Autowired
    ServiceInstance serviceInstance;
    @Override
    public void customize(ConfigurableApplicationContext context) {
        if (context instanceof EmbeddedWebApplicationContext
                && !AdminEndpointApplicationRunListener.isEmbeddedServletServer(context.getEnvironment())) {
            MetaDataProvider metaDataProvider = context.getBean(MetaDataProvider.class);
            EmbeddedServletContainer embeddedServletContainer = new EmbeddedServletContainer() {

                @Override
                public void start() throws EmbeddedServletContainerException {

                }

                @Override
                public void stop() throws EmbeddedServletContainerException {

                }

                @Override
                public int getPort() {
                    return metaDataProvider.getServerPort();
                }
            };
            context.publishEvent(new EmbeddedServletContainerInitializedEvent((EmbeddedWebApplicationContext) context, embeddedServletContainer));
        }
    }

    @Override
    public int getOrder() {
        return -1; // Priority of AbstractDiscoveryLifecycle
    }

}
