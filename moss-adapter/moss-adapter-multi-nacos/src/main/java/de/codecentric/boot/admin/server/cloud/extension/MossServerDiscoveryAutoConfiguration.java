/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.boot.admin.server.cloud.extension;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
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

    @Bean
    @ConditionalOnMissingBean({ServiceInstanceConverter.class})
    public DefaultServiceInstanceConverter serviceInstanceConverter() {
        return new DefaultServiceInstanceConverter();
    }

}
