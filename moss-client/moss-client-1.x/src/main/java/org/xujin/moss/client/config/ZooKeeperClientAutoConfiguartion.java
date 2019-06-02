package org.xujin.moss.client.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.support.CuratorServiceDiscoveryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.xujin.moss.client.zookeeper.ZooKeeperAutoRegistrationCustomizer;

@ConditionalOnClass(name = "org.apache.curator.framework.CuratorFramework")
@AutoConfigureBefore(CuratorServiceDiscoveryAutoConfiguration.class)
public class ZooKeeperClientAutoConfiguartion {
    @Bean
    public ZooKeeperAutoRegistrationCustomizer zooKeeperAutoRegistrationCustomizer(CuratorFramework curator,
                                                                                   ZookeeperDiscoveryProperties properties,
                                                                                   InstanceSerializer<ZookeeperInstance> serializer) {
        return new ZooKeeperAutoRegistrationCustomizer(curator, properties, serializer);
    }
}
