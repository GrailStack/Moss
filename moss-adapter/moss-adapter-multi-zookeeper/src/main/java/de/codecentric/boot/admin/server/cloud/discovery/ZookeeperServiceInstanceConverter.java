package de.codecentric.boot.admin.server.cloud.discovery;

import de.codecentric.boot.admin.server.domain.entities.Instance;

import java.net.URI;
import org.springframework.cloud.client.ServiceInstance;


import org.springframework.cloud.zookeeper.discovery.ZookeeperServiceInstance;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Converts {@link ZookeeperServiceInstance}s to {@link Instance}s
 *
 * @author Trivia
 */
public class ZookeeperServiceInstanceConverter extends DefaultServiceInstanceConverter {
    private static final String KEY_HEALTH_PATH = "health.path";

    /**
     * Default context-path to be appended to the url of the discovered service for the
     * managment-url.
     */
    private String managementContextPath = "/actuator";
    /**
     * Default path of the health-endpoint to be used for the health-url of the discovered service.
     */
    private String healthEndpointPath = "health";
    @Override
    protected URI getHealthUrl(ServiceInstance instance) {
        Assert.isInstanceOf(ZookeeperServiceInstance.class, instance,
            "serviceInstance must be of type ZookeeperServiceInstance");
        String healthPath = instance.getMetadata().get(KEY_HEALTH_PATH);
        if (isEmpty(healthPath)) {
            healthPath = healthEndpointPath;
        }

        return UriComponentsBuilder.fromUri(getManagementUrl(instance)).path("/").path(healthPath).build().toUri();
    }
}
