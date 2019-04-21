package de.codecentric.boot.admin.server.cloud.extension;

import de.codecentric.boot.admin.server.cloud.discovery.EurekaServiceInstanceConverter;
import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerMarkerConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnSingleCandidate(DiscoveryClient.class)
@ConditionalOnBean(AdminServerMarkerConfiguration.Marker.class)
@AutoConfigureAfter(value = AdminServerAutoConfiguration.class, name = {
    "org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration",
    "org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration"})
public class MossServerDiscoveryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MossInstanceDiscoveryListener instanceDiscoveryListener(ServiceInstanceConverter serviceInstanceConverter,
                                                               InstanceRegistry registry,
                                                               InstanceRepository repository) {
        MossInstanceDiscoveryListener listener = new MossInstanceDiscoveryListener(registry, repository);
        listener.setConverter(serviceInstanceConverter);
        return listener;
    }

    @Configuration
    @ConditionalOnMissingBean({ServiceInstanceConverter.class})
    public static class EurekaConverterConfiguration {
        @Bean
        public EurekaServiceInstanceConverter serviceInstanceConverter() {
            return new EurekaServiceInstanceConverter();
        }
    }


}
