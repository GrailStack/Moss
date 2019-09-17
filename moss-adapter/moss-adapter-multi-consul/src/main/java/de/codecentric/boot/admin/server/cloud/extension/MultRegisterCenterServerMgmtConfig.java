package de.codecentric.boot.admin.server.cloud.extension;

import com.ecwid.consul.v1.ConsulClient;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.SnapshottingInstanceRepository;
import de.codecentric.boot.admin.server.eventstore.ConcurrentMapEventStore;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.moss.registry.adapter.DiscoveryRegistryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.xujin.moss.common.util.ReactorUtils;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultRegisterCenterServerMgmtConfig implements DiscoveryRegistryManager {
    @Autowired
    private MultRegisterCenter multRegisterCenter;
    @Autowired
    private ApplicationContext applicationContext;
    private InstanceRegistry registry;
    private SnapshottingInstanceRepository snapshottingInstanceRepository;
    private ConsulDiscoveryProperties consulDiscoveryProperties;

    public MultRegisterCenterServerMgmtConfig(InstanceRegistry registry, SnapshottingInstanceRepository snapshottingInstanceRepository, ConsulDiscoveryProperties consulDiscoveryProperties) {
        this.registry = registry;
        this.snapshottingInstanceRepository = snapshottingInstanceRepository;
        this.consulDiscoveryProperties = consulDiscoveryProperties;
    }

    private String getProperty(String property, ConfigurableEnvironment env) {
        return env.containsProperty(property) ? env.getProperty(property) : "";
    }


    /**
     * 动态添加一个注册中心
     *
     * @param registerCenterCode
     * @param registerCenterUrl
     */
    public void addConsul(String registerCenterCode, String registerCenterUrl) {

        /**
         * 添加ConsulClient,如果有就先删除，再添加
         */
        Map<String, ConsulDiscoveryClient> multConsulMap = multRegisterCenter.getMultConsulMap();
        removeConsulClientByCode(registerCenterCode);
        ConsulDiscoveryClient consulClient = consulClient(registerCenterUrl);
        multConsulMap.put(registerCenterCode, consulClient);
        multRegisterCenter.getMultConsulCodeMap().put(consulClient, registerCenterCode);

        /**
         * 添加 HeartbeatMonitor
         */
        Map<ConsulDiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap = multRegisterCenter.getMultHeartbeatMonitorMap();
        multHeartbeatMonitorMap.remove(registerCenterCode);
        multHeartbeatMonitorMap.put(consulClient, new HeartbeatMonitor());
        applicationContext.publishEvent(new RegisterCenterRefreshEvent(consulClient));
    }


    /**
     * 动态删除一个注册中心
     *
     * @param registerCenterCode
     */
    public void removeConsul(String registerCenterCode) {
        removeConsulClientByCode(registerCenterCode);
        removeStaleInstancesBySource(registerCenterCode);
    }


    public void removeConsulClientByCode(String registerCenterCode) {
        Map<String, ConsulDiscoveryClient> multConsulMap = multRegisterCenter.getMultConsulMap();
        ConsulDiscoveryClient oldConsulClient = multConsulMap.get(registerCenterCode);
        if (null != oldConsulClient) {
            multRegisterCenter.getMultConsulCodeMap().remove(oldConsulClient);
            multRegisterCenter.getMultHeartbeatMonitorMap().remove(oldConsulClient);
            multConsulMap.remove(registerCenterCode);
        }
    }

    /**
     * 根据注册中心标识删除实例
     *
     * @param source
     */
    public void removeStaleInstancesBySource(String source) {
        Flux<Instance> instanceFlux = registry.getInstances().filter(Instance::isRegistered).filter(instance -> source.equalsIgnoreCase(instance.getRegistration().getSource()));
        List<Instance> instanceList = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().collect(Collectors.toList());
        for (Instance instance : instanceList) {
            snapshottingInstanceRepository.revomeInstance(instance.getId());
            ConcurrentMapEventStore InstanceEventStore = (ConcurrentMapEventStore) snapshottingInstanceRepository.getHaloEventStore();
            InstanceEventStore.getEventLog().remove(instance.getId());
        }

    }

    @Override
    public void removeRegistry(String code) {
        this.removeConsul(code);
    }

    @Override
    public void addRegistry(String code, String url) {
        this.addConsul(code, url);
    }


    public ConsulDiscoveryClient consulClient(String url) {
        return new ConsulDiscoveryClient(new ConsulClient(url), consulDiscoveryProperties);
    }
}
