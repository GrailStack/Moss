package org.xujin.moss.client.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.metadata.DefaultManagementMetadataProvider;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadata;
import org.xujin.moss.client.config.MetaDataProvider;

import java.util.Map;

public class EurekaAutoRegistrationCustomizer extends DefaultManagementMetadataProvider {
    private static final String MANAGEMENT_PORT = "management.port";
    private static final String PID = "pid";
    @Autowired
    MetaDataProvider metaDataProvider;

    @Override
    public ManagementMetadata get(EurekaInstanceConfigBean instance, int serverPort, String serverContextPath, String managementContextPath, Integer managementPort) {
        customizeMetadata(instance);
        ManagementMetadata managementMetadata = super.get(instance,
                serverPort,
                serverContextPath,
                managementContextPath,
                managementPort);

        return managementMetadata;
    }


    private String instanceId(EurekaInstanceConfigBean instance) {

        String processId = metaDataProvider.getProcessId();
        return new StringBuilder(processId) // pid@ip:port
                        .append('@')
                        .append(instance.getHostName(false))
                        .append(':')
                        .append(metaDataProvider.getServerPort())
                        .toString();

    }
    private void customizeMetadata(EurekaInstanceConfigBean instance) {
        instance.setInstanceId(instanceId(instance));
        Map<String, String> metadata = instance.getMetadataMap();
        String processId = metaDataProvider.getProcessId();
        if(metadata != null) {
            metadata.put(MANAGEMENT_PORT, metaDataProvider.getManagementPort() + "");
            metadata.put(PID, processId);
        }
    }
}
