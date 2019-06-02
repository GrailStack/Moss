package org.xujin.moss.autoconfigure;

import org.moss.registry.adapter.DiscoveryRegistryManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(name = {
        "de.codecentric.boot.admin.server.cloud.extension.MossServerDiscoveryAutoConfiguration"
})
public class MossServerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(DiscoveryRegistryManager.class)
    public DiscoveryRegistryManager defaultDiscoveryRegistryManager() {
        return new DefaultDiscoveryRegistryManager();
    }

    public static class DefaultDiscoveryRegistryManager implements DiscoveryRegistryManager {

        @Override
        public void removeRegistry(String code) {

        }

        @Override
        public void addRegistry(String code, String url) {

        }
    }
}
