package org.xujin.moss.client.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/**
 * 内嵌 Web 容器模式运行时，指定一些 Management 的属性配置
 */
public class ManagementEnvironmentCustomizer implements EnvironmentCustomizer<ConfigurableEnvironment> {

    private static final String SPRINGBOOT_MANAGEMENT_PORT_KEY = "management.port";

    private static final int SPRINGBOOT_MANAGEMENT_PORT_VALUE = 8081;

    private static final Log logger = LogFactory.getLog(AdminEndpointApplicationRunListener.class);
    private static final String DEFAULT_PROPERTY = "META-INF/moss-client/bootstrap.properties";
    @Override
    public void customize(ConfigurableEnvironment env) {
        try {
            Properties props;
            ClassPathResource resource = new ClassPathResource(DEFAULT_PROPERTY);
            props = PropertiesLoaderUtils.loadProperties(resource);
            props.put(SPRINGBOOT_MANAGEMENT_PORT_KEY, getManagementPort(env));
            env.getPropertySources().addLast(new PropertiesPropertySource("managementProperties", props));
        } catch (IOException e) {
            logger.error("Failed to load " + DEFAULT_PROPERTY);
        }
    }

    private int getManagementPort(ConfigurableEnvironment env) {
        if (!"prod".equalsIgnoreCase(env.getProperty("spring.profiles.active"))) {
            try {
                //不是生产环境，使用Socket去连接如果能连接上表示端口被占用
                InetAddress Address = InetAddress.getByName("127.0.0.1");
                Socket socket = new Socket(Address, SPRINGBOOT_MANAGEMENT_PORT_VALUE);
                logger.info(SPRINGBOOT_MANAGEMENT_PORT_VALUE+":port is used,return:0");
                return 0;
            } catch (IOException e) {
                logger.info(SPRINGBOOT_MANAGEMENT_PORT_VALUE+":port is not used");
            }
        }
        return SPRINGBOOT_MANAGEMENT_PORT_VALUE;
    }
}
