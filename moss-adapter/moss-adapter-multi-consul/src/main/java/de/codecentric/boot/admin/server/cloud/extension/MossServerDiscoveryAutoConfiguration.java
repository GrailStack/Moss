package de.codecentric.boot.admin.server.cloud.extension;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerMarkerConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.entities.SnapshottingInstanceRepository;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.moss.registry.adapter.MultRegisterCenterService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConditionalOnSingleCandidate(DiscoveryClient.class)
@ConditionalOnBean(AdminServerMarkerConfiguration.Marker.class)
@AutoConfigureAfter(value = AdminServerAutoConfiguration.class, name = {
        "org.springframework.cloud.consul.discovery.ConsulDiscoveryClientConfiguration",
        "org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration"})
@Slf4j
public class MossServerDiscoveryAutoConfiguration {

    @ConditionalOnMissingBean({ServiceInstanceConverter.class})
    @Bean
    public DefaultServiceInstanceConverter serviceInstanceConverter() {
        return new DefaultServiceInstanceConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConsulDiscoveryProperties consulDiscoveryProperties(InetUtils inetUtils) {
        return new ConsulDiscoveryProperties(inetUtils);
    }


    @Bean
    @Primary
    public MultRegisterCenterServerMgmtConfig multRegisterCenterServerMgmtConfig(InstanceRegistry registry, SnapshottingInstanceRepository snapshottingInstanceRepository, ConsulDiscoveryProperties consulDiscoveryProperties) {
        return new MultRegisterCenterServerMgmtConfig(registry, snapshottingInstanceRepository, consulDiscoveryProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public MossInstanceDiscoveryListener instanceDiscoveryListener(ServiceInstanceConverter serviceInstanceConverter,
                                                                   InstanceRegistry registry,
                                                                   InstanceRepository repository) {
        MossInstanceDiscoveryListener listener = new MossInstanceDiscoveryListener(registry, repository);
        listener.setConverter(serviceInstanceConverter);
        listener.setIgnoredServices(Sets.newHashSet("consul"));
        return listener;
    }


    @Bean
    public MultRegisterCenter initMultConsul(MultRegisterCenterService multRegisterCenterService,
                                             MultRegisterCenterServerMgmtConfig mgmtConfig) {
        Map<String, String> URL_MAP;
        MultRegisterCenter multRegisterCenter;
        log.info("start init MultRegisterCenter");
        URL_MAP = multRegisterCenterService.getRegisterCenterList();
        if (URL_MAP.isEmpty()) {
            multRegisterCenter = new MultRegisterCenter(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
            return multRegisterCenter;
        }
        Map<String, ConsulDiscoveryClient> multConsulMap = Maps.newConcurrentMap();
        Map<ConsulDiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap = Maps.newConcurrentMap();


        URL_MAP.entrySet().forEach(e -> {
            log.info("start init consul server:{}", e.getKey() + "-" + e.getValue());
            ConsulDiscoveryClient consulClient = mgmtConfig.consulClient(e.getValue());
            multConsulMap.put(e.getKey(), consulClient);
            multHeartbeatMonitorMap.put(consulClient, new HeartbeatMonitor());
            log.info("init consul server:{} end!", e.getKey() + "-" + e.getValue());
        });
        multRegisterCenter = new MultRegisterCenter(multConsulMap, multHeartbeatMonitorMap);
        log.info("init MultRegisterCenter End!");
        return multRegisterCenter;
    }


}
