package org.xujin.moss.client.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.support.DefaultServiceDiscoveryCustomizer;

import java.util.Map;

public class AutoRegistrationCustomizer extends DefaultServiceDiscoveryCustomizer {
    private static final String MANAGEMENT_PORT = "management.port";
    private static final String PID = "pid";
    @Autowired
    MetaDataProvider metaDataProvider;

    public AutoRegistrationCustomizer(CuratorFramework curator, ZookeeperDiscoveryProperties properties, InstanceSerializer<ZookeeperInstance> serializer) {
        super(curator, properties, serializer);
    }

    @Override
    public ServiceDiscovery<ZookeeperInstance> customize(ServiceDiscoveryBuilder<ZookeeperInstance> builder) {
        Map<String, String> metadata = super.properties.getMetadata();
        String processId = metaDataProvider.getProcessId();
        String instanceId = // pid@ip:port
                new StringBuilder(processId)
                        .append('@')
                        .append(properties.getInstanceHost())
                        .append(':')
                        .append(metaDataProvider.getServerPort())
                        .toString();
        super.properties.setInstanceId(instanceId);
        if(metadata != null) {
            metadata.put(MANAGEMENT_PORT, metaDataProvider.getManagementPort() + "");
            metadata.put(PID, processId);
        }
        ServiceDiscovery<ZookeeperInstance> customize = super.customize(builder);
        return customize;
    }
}
