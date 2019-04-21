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

import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ListView;
import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.alibaba.nacos.NacosServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.discovery.event.ParentHeartbeatEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.PatternMatchUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Moss管理的实例发现监听
 * xujin
 */
public class MossInstanceDiscoveryListener {

    private static final Logger log = LoggerFactory.getLogger(MossInstanceDiscoveryListener.class);
    private static final String SOURCE = "discovery";
    ThreadLocal<NamingService> namingServiceThreadLocal = new ThreadLocal<NamingService>();
    private final InstanceRegistry registry;
    private final InstanceRepository repository;
    private final HeartbeatMonitor monitor = new HeartbeatMonitor();
    private ServiceInstanceConverter converter = new DefaultServiceInstanceConverter();

    /**
     * Set of serviceIds to be ignored and not to be registered as application. Supports simple
     * patterns (e.g. "foo*", "*foo", "foo*bar").
     */
    private Set<String> ignoredServices = new HashSet<>();

    /**
     * Set of serviceIds that has to match to be registered as application. Supports simple
     * patterns (e.g. "foo*", "*foo", "foo*bar"). Default value is everything
     */
    private Set<String> services = new HashSet<>(Collections.singletonList("*"));

    public MossInstanceDiscoveryListener(
                                     InstanceRegistry registry,
                                     InstanceRepository repository) {
        this.registry = registry;
        this.repository = repository;
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        Map<String, MossNacosAutoServiceRegistration> map= MultRegisterCenterServerMgmtConfig.getMultRegisterCenter().getMultRegistrationMap();
        map.entrySet().forEach(e -> {
            namingServiceThreadLocal.set(e.getValue().getRegistration().getNacosNamingService());
            discover();
            namingServiceThreadLocal.remove();
        });
    }

    @EventListener
    public void onInstanceRegistered(InstanceRegisteredEvent<?> event) {
        discover();
    }

    @EventListener
    public void onParentHeartbeat(ParentHeartbeatEvent event) {
        discoverIfNeeded(event.getValue());
    }

    @EventListener
    public void onApplicationEvent(HeartbeatEvent event) {
        discoverIfNeeded(event.getValue());
    }

    private void discoverIfNeeded(Object value) {
        if (this.monitor.update(value)) {
            discover();
        }
    }

    protected void discover() {
        Flux.fromIterable(getServicesByNacos())
                .filter(this::shouldRegisterService)
                .flatMapIterable(this::getInstances)
                .flatMap(this::registerInstance)
                .collect(Collectors.toSet())
                .flatMap(this::removeStaleInstances)
                .subscribe(v -> { }, ex -> log.error("Unexpected error.", ex));
    }

    protected Mono<Void> removeStaleInstances(Set<InstanceId> registeredInstanceIds) {
        return repository.findAll()
                .filter(Instance::isRegistered)
                .filter(instance -> SOURCE.equals(instance.getRegistration().getSource()))
                .map(Instance::getId)
                .filter(id -> !registeredInstanceIds.contains(id))
                .doOnNext(id -> log.info("Instance ({}) missing in DiscoveryClient services ", id))
                .flatMap(registry::deregister)
                .then();
    }

    public List<ServiceInstance> getInstances(String serviceId) {
        try {
            List<com.alibaba.nacos.api.naming.pojo.Instance> instances = namingServiceThreadLocal.get()
                    .selectInstances(serviceId, true);
            return hostToServiceInstanceList(instances, serviceId);
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "Can not get hosts from nacos server. serviceId: " + serviceId, e);
        }
    }

    private static List<ServiceInstance> hostToServiceInstanceList(
            List<com.alibaba.nacos.api.naming.pojo.Instance> instances, String serviceId) {
        List<ServiceInstance> result = new ArrayList<ServiceInstance>(instances.size());
        for (com.alibaba.nacos.api.naming.pojo.Instance instance : instances) {
            result.add(hostToServiceInstance(instance, serviceId));
        }
        return result;
    }

    private static ServiceInstance hostToServiceInstance(com.alibaba.nacos.api.naming.pojo.Instance instance,
                                                         String serviceId) {
        NacosServiceInstance nacosServiceInstance = new NacosServiceInstance();
        nacosServiceInstance.setHost(instance.getIp());
        nacosServiceInstance.setPort(instance.getPort());
        nacosServiceInstance.setServiceId(serviceId);
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put("instanceId", instance.getInstanceId());
        metadata.put("weight", instance.getWeight() + "");
        metadata.put("healthy", instance.isHealthy() + "");
        metadata.put("cluster", instance.getClusterName() + "");
        metadata.putAll(instance.getMetadata());
        nacosServiceInstance.setMetadata(metadata);
        return nacosServiceInstance;
    }


    public List<String> getServicesByNacos() {
        try {
            ListView<String> services =namingServiceThreadLocal.get()
                    .getServicesOfServer(1, Integer.MAX_VALUE);
            return services.getData();
        }
        catch (Exception e) {
            log.error("get service name from nacos server fail,", e);
            return Collections.emptyList();
        }
    }

    protected boolean shouldRegisterService(final String serviceId) {
        boolean shouldRegister = matchesPattern(serviceId, services) && !matchesPattern(serviceId, ignoredServices);
        if (!shouldRegister) {
            log.debug("Ignoring discovered service {}", serviceId);
        }
        return shouldRegister;
    }

    protected boolean matchesPattern(String serviceId, Set<String> patterns) {
        return patterns.stream().anyMatch(pattern -> PatternMatchUtils.simpleMatch(pattern, serviceId));
    }

    protected Mono<InstanceId> registerInstance(ServiceInstance instance) {
        try {
            Registration registration = converter.convert(instance).toBuilder().source(SOURCE).build();
            log.debug("Registering discovered instance {}", registration);
            return registry.register(registration);
        } catch (Exception ex) {
            log.error("Couldn't register instance for service ({})", toString(instance), ex);
        }
        return Mono.empty();
    }

    protected String toString(ServiceInstance instance) {
        String httpScheme = instance.isSecure() ? "https" : "http";
        return String.format("serviceId=%s, instanceId=%s, url= %s://%s:%d",
                instance.getServiceId(),
                instance.getScheme() != null ? instance.getScheme() : httpScheme,
                instance.getHost(),
                instance.getPort()
        );
    }

    public void setConverter(ServiceInstanceConverter converter) {
        this.converter = converter;
    }

    public void setIgnoredServices(Set<String> ignoredServices) {
        this.ignoredServices = ignoredServices;
    }

    public Set<String> getIgnoredServices() {
        return ignoredServices;
    }

    public Set<String> getServices() {
        return services;
    }

    public void setServices(Set<String> services) {
        this.services = services;
    }
}
