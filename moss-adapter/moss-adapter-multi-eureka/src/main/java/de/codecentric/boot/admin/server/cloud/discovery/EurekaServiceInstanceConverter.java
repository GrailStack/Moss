package de.codecentric.boot.admin.server.cloud.discovery;

import de.codecentric.boot.admin.server.domain.entities.Instance;

import java.net.URI;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient.EurekaServiceInstance;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import com.netflix.appinfo.InstanceInfo;

/**
 * Converts {@link EurekaServiceInstance}s to {@link Instance}s
 *
 * @author Johannes Edmeier
 */
public class EurekaServiceInstanceConverter extends DefaultServiceInstanceConverter {

    @Override
    protected URI getHealthUrl(ServiceInstance instance) {
        Assert.isInstanceOf(EurekaServiceInstance.class, instance,
            "serviceInstance must be of type EurekaServiceInstance");

        InstanceInfo instanceInfo = ((EurekaServiceInstance) instance).getInstanceInfo();
        String healthUrl = instanceInfo.getSecureHealthCheckUrl();
        if (StringUtils.isEmpty(healthUrl)) {
            healthUrl = instanceInfo.getHealthCheckUrl();
        }
        return URI.create(healthUrl);
    }
}
