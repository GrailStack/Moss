package org.xujin.moss.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.xujin.moss.client.eureka.EurekaAutoRegistrationCustomizer;

@ConditionalOnBean(name = "org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean")
public class EurekaClientAutoConfiguartion {
    @Bean
    public EurekaAutoRegistrationCustomizer eurekaAutoRegistrationCustomizer() {
        return new EurekaAutoRegistrationCustomizer();
    }
}
