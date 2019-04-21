package org.xujin.moss.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;

public class AdminEndpointApplicationRunListener implements SpringApplicationRunListener {

    private static final Log logger = LogFactory.getLog(AdminEndpointApplicationRunListener.class);

    @Resource
    private ManagementServerProperties managementServerProperties;

    private static final String SPRINGBOOT_MANAGEMENT_ENABLED_KEY = "management.security.enabled";

    private static final boolean SPRINGBOOT_MANAGEMENT_ENABLED_VALUE = false;

    private static final String SPRINGBOOT_MANAGEMENT_PORT_KEY = "management.port";

    private static final int SPRINGBOOT_MANAGEMENT_PORT_VALUE = 8081;

    private static final String ENDPOINTS_ENABLED = " management.endpoints.enabled";


    private static final String SPRINGBOOT_MANAGEMENT_CONTEXT_PATH_KEY ="management.context-path" ;

    public static final String SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE ="/actuator" ;

    private  static  final  String GIT_MODE="management.info.git.mode";

    private  static  final  String GIT_FULL="full";

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
        Properties props = new Properties();
        if (managementServerProperties == null) {
            props.put(SPRINGBOOT_MANAGEMENT_ENABLED_KEY, SPRINGBOOT_MANAGEMENT_ENABLED_VALUE);
            props.put(SPRINGBOOT_MANAGEMENT_PORT_KEY, getManagementPort(env));
            props.put(ENDPOINTS_ENABLED, true);
            props.put(GIT_MODE,GIT_FULL);
//            props.put(SPRINGBOOT_MANAGEMENT_CONTEXT_PATH_KEY,SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE);
            props.put(SPRINGBOOT_MANAGEMENT_CONTEXT_PATH_KEY,"/");

            Arrays.asList("metricsInfo",
                    "autoconfig",
                    "metrics",
                    "infoLogFile",
                    "errorLogFile",
                    "jardeps",
                    "appInfo",
//                    "health",
                    "env",
                    "metrics",
                    "trace",
                    "httptrace",
                    "dump",
                    "threaddump",
                    "jolokia",
                    "info",
                    "logfile",
                    //"refresh",
                    "flyway",
                    "liquibase",
                    "heapdump",
                    "loggers",
                    "auditevents",
                    "mappings",
                    "scheduledtasks",
                    "configprops",
                    "caches",
                    "beans").forEach(id->{
                    props.put("endpoints." +id+
                            ".path",SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE+"/"+id);
            });

        }
        env.getPropertySources().addLast(new PropertiesPropertySource("endpoint", props));

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
    public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {

    }

    @Override
    public void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {

    }

    public AdminEndpointApplicationRunListener(SpringApplication application, String[] args) {
    }
}
