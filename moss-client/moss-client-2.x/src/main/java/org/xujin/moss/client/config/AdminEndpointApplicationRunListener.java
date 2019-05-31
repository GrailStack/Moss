package org.xujin.moss.client.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AdminEndpointApplicationRunListener implements SpringApplicationRunListener {



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
        if("bootstrap".equals(env.getProperty("spring.config.name"))) {
            List<EnvironmentCustomizer> environmentCustomizers =
            SpringFactoriesLoader.loadFactories(EnvironmentCustomizer.class, AdminEndpointApplicationRunListener.class.getClassLoader());
            if(CollectionUtils.isEmpty(environmentCustomizers)) return;
            for(EnvironmentCustomizer customizer: environmentCustomizers) {
                customizer.customize(env);
            }
        }

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }


    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

        Collection<ApplicationContextCustomizer> applicationContextCustomizers =
                Arrays.asList(new DiscoveryClientRegistrationInvoker());
        for(ApplicationContextCustomizer customizer: applicationContextCustomizers) {
            customizer.customize(context);
        }
    }

    @Override
    public void starting() {
    }
    public static boolean isEmbeddedServletServer(Environment env) {
        return StringUtils.isEmpty(env.getProperty(ContextLoader.CONFIG_LOCATION_PARAM));
    }
}
