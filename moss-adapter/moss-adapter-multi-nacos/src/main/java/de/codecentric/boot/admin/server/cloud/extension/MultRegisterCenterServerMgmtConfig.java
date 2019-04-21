package de.codecentric.boot.admin.server.cloud.extension;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.google.common.collect.Maps;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.SnapshottingInstanceRepository;
import de.codecentric.boot.admin.server.eventstore.ConcurrentMapEventStore;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.registry.NacosRegistration;
import org.springframework.cloud.alibaba.nacos.registry.NacosServiceRegistry;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xujin.moss.common.util.ReactorUtils;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Configuration
public class MultRegisterCenterServerMgmtConfig {


    @Autowired
    private InetUtils inetUtils;

    private static Map<String, String> URL_MAP=new ConcurrentHashMap<String, String>();;

    private static final Logger log = LoggerFactory.getLogger(MultRegisterCenterServerMgmtConfig.class);

    @Autowired
    private InstanceRegistry registry;

    @Autowired
    private SnapshottingInstanceRepository snapshottingInstanceRepository;

    private static MultRegisterCenter multRegisterCenter;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MultRegisterCenterService multRegisterCenterService;

    public static MultRegisterCenter getMultRegisterCenter(){
        return multRegisterCenter;
    }

    @Bean
    @ConditionalOnMissingBean
    public NacosDiscoveryProperties nacosProperties() {
        return new NacosDiscoveryProperties();
    }

    @Bean
    public MultRegisterCenter initMultNacos() {
        URL_MAP.put("nacos1","127.0.0.1:8848");
        Map<String, NamingService> multEurekaMap = Maps.newConcurrentMap();
        Map<String, MossNacosAutoServiceRegistration> multRegistrationMap = Maps.newConcurrentMap();
        Map<NamingService, HeartbeatMonitor> multHeartbeatMonitorMap=new ConcurrentHashMap<NamingService, HeartbeatMonitor>();
        URL_MAP.entrySet().forEach(e -> {
            NacosDiscoveryProperties nacosDiscoveryProperties=new NacosDiscoveryProperties();
            nacosDiscoveryProperties.setService("halo-moss");
            nacosDiscoveryProperties.setServerAddr(e.getValue());
            try {
                NamingService namingService=NacosFactory.createNamingService(e.getValue());
                com.alibaba.nacos.api.naming.pojo.Instance instance = new com.alibaba.nacos.api.naming.pojo.Instance();
                instance.setIp(inetUtils.findFirstNonLoopbackHostInfo().getIpAddress());
                instance.setPort(-1);
                instance.setWeight(1);
                instance.setClusterName("DEFAULT");
                namingService.registerInstance("halo-moss", instance);
                this.context.publishEvent(
                        new InstanceRegisteredEvent<>(this, namingService));
                multEurekaMap.put(e.getKey(),namingService);
                multHeartbeatMonitorMap.put(namingService,new HeartbeatMonitor());
            } catch (NacosException e1) {
                e1.printStackTrace();
            }
            //NacosServiceRegistry serviceRegistry=new NacosServiceRegistry();
            //AutoServiceRegistrationProperties autoServiceRegistrationProperties=new AutoServiceRegistrationProperties();
            //MossNacosAutoServiceRegistration autoServiceRegistration = new MossNacosAutoServiceRegistration(serviceRegistry,autoServiceRegistrationProperties,registration,registration);
            //autoServiceRegistration.setRegistration(registration);
            //multRegistrationMap.put(e.getKey(), autoServiceRegistration);

        });
        multRegisterCenter = new MultRegisterCenter(multEurekaMap, multRegistrationMap,multHeartbeatMonitorMap);
        return multRegisterCenter;
    }


    public void addEureka(String registerCenterCode,String registerCenterUrl) {



    }


    /**
     * 动态删除一个注册中心
     * @param registerCenterCode
     */
    public void revomeEureka(String registerCenterCode){

    }


    /**
     * 根据注册中心标识删除实例
     * @param source
     */
    public  void remoStaleInstancesBySource(String source) {
        Flux<Instance> instanceFlux=registry.getInstances().filter(Instance::isRegistered).filter(instance->source.equalsIgnoreCase(instance.getRegistration().getSource()));
        List<Instance> instanceList = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().collect(Collectors.toList());
        for (Instance instance: instanceList) {
            snapshottingInstanceRepository.revomeInstance(instance.getId());
            ConcurrentMapEventStore InstanceEventStore=(ConcurrentMapEventStore)snapshottingInstanceRepository.getHaloEventStore();
            InstanceEventStore.getEventLog().remove(instance.getId());

        }

    }

}
