package de.codecentric.boot.admin.server.cloud.extension;

import com.google.common.collect.Maps;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.discovery.EurekaClient;
import de.codecentric.boot.admin.server.cloud.discovery.EurekaServiceInstanceConverter;
import de.codecentric.boot.admin.server.cloud.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerMarkerConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.moss.registry.adapter.MultRegisterCenterService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadataProvider;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ConditionalOnSingleCandidate(DiscoveryClient.class)
@ConditionalOnBean(AdminServerMarkerConfiguration.Marker.class)
@AutoConfigureAfter(value = AdminServerAutoConfiguration.class, name = {
        "org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration",
        "org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration"})
@Slf4j
public class MossServerDiscoveryAutoConfiguration {
    @Autowired
    private ConfigurableEnvironment env;
    @Autowired
    private InetUtils inetUtils;
    @Autowired
    private ApplicationContext context;
    @Autowired(required = false)
    private ObjectProvider<HealthCheckHandler> healthCheckHandler;

    @Bean
    @ConditionalOnMissingBean
    public MossInstanceDiscoveryListener instanceDiscoveryListener(ServiceInstanceConverter serviceInstanceConverter,
                                                                   InstanceRegistry registry,
                                                                   InstanceRepository repository) {
        MossInstanceDiscoveryListener listener = new MossInstanceDiscoveryListener(registry, repository);
        listener.setConverter(serviceInstanceConverter);
        return listener;
    }

    @Bean(destroyMethod = "shutdown")
    public MultRegisterCenter initMultEureka(MultRegisterCenterService multRegisterCenterService,
                                             MultRegisterCenterServerMgmtConfig mgmtConfig) {
        Map<String, String> URL_MAP;
        MultRegisterCenter multRegisterCenter;
        log.info("start init MultRegisterCenter");
        URL_MAP = multRegisterCenterService.getRegisterCenterList();
        if (URL_MAP.isEmpty()) {
            multRegisterCenter = new MultRegisterCenter(new ConcurrentHashMap<String, EurekaClient>(),
                    new ConcurrentHashMap<String, MossEurekaAutoServiceRegistration>(),
                    new ConcurrentHashMap<EurekaClient, HeartbeatMonitor>());
            return multRegisterCenter;
        }
        Map<String, EurekaClient> multEurekaMap = Maps.newConcurrentMap();
        Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap = Maps.newConcurrentMap();
        Map<EurekaClient, HeartbeatMonitor> multHeartbeatMonitorMap = new ConcurrentHashMap<EurekaClient, HeartbeatMonitor>();
        URL_MAP.entrySet().forEach(e -> {
            log.info("start init eureka server:{}", e.getKey() + "-" + e.getValue());
            ManagementMetadataProvider managementMetadataProvider
                    = mgmtConfig.serviceManagementMetadataProvider();
            EurekaClientConfigBean configBean
                    = mgmtConfig.eurekaClientConfigBean(env);
            configBean.getServiceUrl().clear();
            configBean.getServiceUrl().put(EurekaClientConfigBean.DEFAULT_ZONE, e.getValue());
            EurekaInstanceConfigBean instanceConfigBean
                    = mgmtConfig.eurekaInstanceConfigBean(inetUtils, env, managementMetadataProvider);
            instanceConfigBean.setEnvironment(env);
            ApplicationInfoManager manager
                    = mgmtConfig.eurekaApplicationInfoManager(instanceConfigBean);
            EurekaClient eurekaClient
                    = mgmtConfig.eurekaClient(manager, configBean);
            EurekaRegistration registration
                    = mgmtConfig.eurekaRegistration(eurekaClient, instanceConfigBean, manager, healthCheckHandler);
            MossEurekaAutoServiceRegistration autoServiceRegistration
                    = mgmtConfig.eurekaAutoServiceRegistration(context, mgmtConfig.eurekaServiceRegistry(), registration, registration);
            multEurekaMap.put(e.getKey(), eurekaClient);
            multRegistrationMap.put(e.getKey(), autoServiceRegistration);
            multHeartbeatMonitorMap.put(eurekaClient, new HeartbeatMonitor());
            log.info("init eureka server:{} end!", e.getKey() + "-" + e.getValue());
        });
        multRegisterCenter = new MultRegisterCenter(multEurekaMap, multRegistrationMap, multHeartbeatMonitorMap);
        log.info("init MultRegisterCenter End!");
        return multRegisterCenter;
    }

    @Bean
    @Primary
    public MultRegisterCenterServerMgmtConfig multRegisterCenterServerMgmtConfig() {
        return new MultRegisterCenterServerMgmtConfig();
    }

    @Configuration
    @ConditionalOnMissingBean({ServiceInstanceConverter.class})
    public static class EurekaConverterConfiguration {
        @Bean
        public EurekaServiceInstanceConverter serviceInstanceConverter() {
            return new EurekaServiceInstanceConverter();
        }
    }


}
