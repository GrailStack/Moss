package de.codecentric.boot.admin.server.cloud.discovery;


import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.Registration;

import org.springframework.cloud.client.ServiceInstance;

/**
 * Converts {@link ServiceInstance}s to {@link Instance}s.
 *
 * @author Johannes Edmeier
 */
public interface ServiceInstanceConverter {

    /**
     * Converts a service instance to a application instance to be registered.
     *
     * @param instance the service instance.
     * @return Instance
     */
    Registration convert(ServiceInstance instance);
}
