package org.xujin.moss.client.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 内嵌 Web 容器模式运行时，指定一些 Management 的属性配置
 */
public class ManagementEnvironmentCustomizer implements EnvironmentCustomizer<ConfigurableEnvironment> {
    private static final Log logger = LogFactory.getLog(AdminEndpointApplicationRunListener.class);
    private static final String DEFAULT_PROPERTY = "META-INF/moss-client/bootstrap.properties";
    @Override
    public void customize(ConfigurableEnvironment env) {
        try {
            Properties props;
            ClassPathResource resource = new ClassPathResource(DEFAULT_PROPERTY);
            props = PropertiesLoaderUtils.loadProperties(resource);
            env.getPropertySources().addLast(new PropertiesPropertySource("managementProperties", props));
        } catch (IOException e) {
            logger.error("Failed to load " + DEFAULT_PROPERTY);
        }
    }
}
