package org.xujin.moss.client.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

public class AdminEndpointApplicationRunListener implements SpringApplicationRunListener {

    private static final Log logger = LogFactory.getLog(AdminEndpointApplicationRunListener.class);

    private static final String SPRINGBOOT_MANAGEMENT_ENABLED_KEY = "management.server.security.enabled";

    private static final boolean SPRINGBOOT_MANAGEMENT_ENABLED_VALUE = false;
    private static final String SPRINGBOOT_MANAGEMENT_PORT_KEY = "management.server.port";
    private static final int SPRINGBOOT_MANAGEMENT_PORT_VALUE = 8081;
    private static final String ENDPOINTS_ENABLED = "management.endpoints.enabled-by-default";
    private static final String ENDPOINTS_JMX_INCLUDE = "management.endpoints.jmx.exposure.include";
    private static final String ENDPOINTS_WEB_INCLUDE = "management.endpoints.web.exposure.include";
    private static final String INCLUDE_ALL = "*";

    private  static  final  String GIT_MODE="management.info.git.mode";

    private  static  final  String GIT_FULL="full";

    @Resource
    private ManagementServerProperties managementServerProperties;

    public AdminEndpointApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
        Properties props = new Properties();
        if (managementServerProperties == null) {
            props.put(SPRINGBOOT_MANAGEMENT_ENABLED_KEY, SPRINGBOOT_MANAGEMENT_ENABLED_VALUE);
            props.put(SPRINGBOOT_MANAGEMENT_PORT_KEY, getManagementPort(env));
            props.put(ENDPOINTS_ENABLED, true);
            props.put(ENDPOINTS_JMX_INCLUDE, INCLUDE_ALL);
            props.put(ENDPOINTS_WEB_INCLUDE, INCLUDE_ALL);
            props.put(GIT_MODE,GIT_FULL);
        }
        env.getPropertySources().addLast(new PropertiesPropertySource("endpoint", props));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

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

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void starting() {
    }

}
