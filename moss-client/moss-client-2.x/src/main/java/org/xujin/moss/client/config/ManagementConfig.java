package org.xujin.moss.client.config;

import org.xujin.moss.client.endpoint.PlatformManagementWebServerFactoryCustomizer;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Author: xujin
 **/
@ManagementContextConfiguration(ManagementContextType.ANY)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ManagementConfig {

    @Bean
    public PlatformManagementWebServerFactoryCustomizer healthExposableEndpoint(){
        return new PlatformManagementWebServerFactoryCustomizer();
    }

}
