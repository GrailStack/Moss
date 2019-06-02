package de.codecentric.boot.admin.server.cloud.extension;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.SnapshottingInstanceRepository;
import de.codecentric.boot.admin.server.eventstore.ConcurrentMapEventStore;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.moss.registry.adapter.DiscoveryRegistryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.cloud.netflix.eureka.*;
import org.springframework.cloud.netflix.eureka.metadata.DefaultManagementMetadataProvider;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadata;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadataProvider;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import org.xujin.moss.common.util.ReactorUtils;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.cloud.commons.util.IdUtils.getDefaultInstanceId;

public class MultRegisterCenterServerMgmtConfig implements DiscoveryRegistryManager {
    private MultRegisterCenter multRegisterCenter;
    @Autowired
    private InstanceRegistry registry;
    @Autowired
    private SnapshottingInstanceRepository snapshottingInstanceRepository;
    @Autowired
    private ApplicationContext context;
    @Autowired(required = false)
    private ObjectProvider<HealthCheckHandler> healthCheckHandler;
    @Autowired(required = false)
    private AbstractDiscoveryClientOptionalArgs<?> optionalArgs;
    @Autowired
    private ConfigurableEnvironment env;
    @Autowired
    private InetUtils inetUtils;

    @Autowired
    public void setMultRegisterCenter(MultRegisterCenter multRegisterCenter) {
        this.multRegisterCenter = multRegisterCenter;
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {

        //This will force the creation of the EurkaClient bean if not already created
        //to make sure the client will be reregistered after a refresh event
        multRegisterCenter.multRegistrationMap.entrySet().forEach(e -> {
            MossEurekaAutoServiceRegistration autoRegistration = e.getValue();
            if (autoRegistration != null) {
                // register in case meta data changed
                autoRegistration.stop();
                autoRegistration.start();
            }
        });
    }

    public EurekaServiceRegistry eurekaServiceRegistry() {
        return new EurekaServiceRegistry();
    }

    public MossEurekaAutoServiceRegistration eurekaAutoServiceRegistration(ApplicationContext context, EurekaServiceRegistry registry,
                                                                           EurekaRegistration registration, EurekaRegistration registration1) {
        return new MossEurekaAutoServiceRegistration(context, registry, registration, registration1);
    }

    public EurekaClient eurekaClient(ApplicationInfoManager manager, EurekaClientConfig config) {
        return new CloudEurekaClient(manager, config, this.optionalArgs,
                this.context);
    }

    public EurekaClientConfigBean eurekaClientConfigBean(ConfigurableEnvironment env) {
        EurekaClientConfigBean client = new EurekaClientConfigBean();
        if ("bootstrap".equals(env.getProperty("spring.config.name"))) {
            // We don't register during bootstrap by default, but there will be another
            // chance later.
            client.setRegisterWithEureka(false);
        }
        return client;
    }

    //@Bean
    //@ConditionalOnMissingBean(value = ApplicationInfoManager.class, search = SearchStrategy.CURRENT)
    public ApplicationInfoManager eurekaApplicationInfoManager(
            EurekaInstanceConfig config) {
        InstanceInfo instanceInfo = new InstanceInfoFactory().create(config);
        return new ApplicationInfoManager(config, instanceInfo);
    }

    public EurekaRegistration eurekaRegistration(EurekaClient eurekaClient,
                                                 CloudEurekaInstanceConfig instanceConfig,
                                                 ApplicationInfoManager applicationInfoManager,
                                                 @Autowired(required = false) ObjectProvider<HealthCheckHandler> healthCheckHandler) {
        return EurekaRegistration.builder(instanceConfig)
                .with(applicationInfoManager)
                .with(eurekaClient)
                .with(healthCheckHandler)
                .build();
    }

    public ManagementMetadataProvider serviceManagementMetadataProvider() {
        return new DefaultManagementMetadataProvider();
    }

    private String getProperty(String property, ConfigurableEnvironment env) {
        return env.containsProperty(property) ? env.getProperty(property) : "";
    }

    /**
     * 构造 EurekaInstanceConfigBean
     *
     * @param inetUtils
     * @param env
     * @param managementMetadataProvider
     * @return
     */
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils, ConfigurableEnvironment env
            , ManagementMetadataProvider managementMetadataProvider) {
        String hostname = getProperty("eureka.instance.hostname", env);
        boolean preferIpAddress = Boolean.parseBoolean(getProperty("eureka.instance.prefer-ip-address", env));
        String ipAddress = getProperty("eureka.instance.ip-address", env);
        boolean isSecurePortEnabled = Boolean.parseBoolean(getProperty("eureka.instance.secure-port-enabled", env));

        String serverContextPath = env.getProperty("server.context-path", "/");
        int serverPort = Integer.valueOf(env.getProperty("server.port", env.getProperty("port", "8080")));

        Integer managementPort = env.getProperty("management.server.port", Integer.class);// nullable. should be wrapped into optional
        String managementContextPath = env.getProperty("management.server.servlet.context-path");// nullable. should be wrapped into optional
        Integer jmxPort = env.getProperty("com.sun.management.jmxremote.port", Integer.class);//nullable
        EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);

        instance.setNonSecurePort(serverPort);
        instance.setInstanceId(getDefaultInstanceId(env));
        instance.setPreferIpAddress(preferIpAddress);
        instance.setSecurePortEnabled(isSecurePortEnabled);
        if (StringUtils.hasText(ipAddress)) {
            instance.setIpAddress(ipAddress);
        }

        if (isSecurePortEnabled) {
            instance.setSecurePort(serverPort);
        }

        if (StringUtils.hasText(hostname)) {
            instance.setHostname(hostname);
        }
        String statusPageUrlPath = getProperty("eureka.instance.status-page-url-path", env);
        String healthCheckUrlPath = getProperty("eureka.instance.health-check-url-path", env);

        if (StringUtils.hasText(statusPageUrlPath)) {
            instance.setStatusPageUrlPath(statusPageUrlPath);
        }
        if (StringUtils.hasText(healthCheckUrlPath)) {
            instance.setHealthCheckUrlPath(healthCheckUrlPath);
        }

        ManagementMetadata metadata = managementMetadataProvider.get(instance, serverPort,
                serverContextPath, managementContextPath, managementPort);

        if (metadata != null) {
            instance.setStatusPageUrl(metadata.getStatusPageUrl());
            instance.setHealthCheckUrl(metadata.getHealthCheckUrl());
            if (instance.isSecurePortEnabled()) {
                instance.setSecureHealthCheckUrl(metadata.getSecureHealthCheckUrl());
            }
            Map<String, String> metadataMap = instance.getMetadataMap();
            if (metadataMap.get("management.port") == null) {
                metadataMap.put("management.port", String.valueOf(metadata.getManagementPort()));
            }
        } else {
            //without the metadata the status and health check URLs will not be set
            //and the status page and health check url paths will not include the
            //context path so set them here
            if (StringUtils.hasText(managementContextPath)) {
                instance.setHealthCheckUrlPath(managementContextPath + instance.getHealthCheckUrlPath());
                instance.setStatusPageUrlPath(managementContextPath + instance.getStatusPageUrlPath());
            }
        }

        setupJmxPort(instance, jmxPort);
        return instance;
    }

    private void setupJmxPort(EurekaInstanceConfigBean instance, Integer jmxPort) {
        Map<String, String> metadataMap = instance.getMetadataMap();
        if (metadataMap.get("jmx.port") == null && jmxPort != null) {
            metadataMap.put("jmx.port", String.valueOf(jmxPort));
        }
    }


    /**
     * 动态添加一个注册中心
     *
     * @param registerCenterCode
     * @param registerCenterUrl
     */
    public void addEureka(String registerCenterCode, String registerCenterUrl) {

        ManagementMetadataProvider managementMetadataProvider = serviceManagementMetadataProvider();
        EurekaClientConfigBean configBean = eurekaClientConfigBean(env);
        configBean.getServiceUrl().clear();
        configBean.getServiceUrl().put(EurekaClientConfigBean.DEFAULT_ZONE, registerCenterUrl);
        EurekaInstanceConfigBean instanceConfigBean = eurekaInstanceConfigBean(inetUtils, env, managementMetadataProvider);
        instanceConfigBean.setEnvironment(env);
        instanceConfigBean.setAppname(instanceConfigBean.getAppname());
        ApplicationInfoManager manager = eurekaApplicationInfoManager(instanceConfigBean);


        /**
         * 添加EurekaClient,如果有就先删除，再添加
         */
        Map<String, EurekaClient> multEurekaMap = multRegisterCenter.getMultEurekaMap();
        revomeEurekaClientByCode(registerCenterCode);
        EurekaClient eurekaClient = eurekaClient(manager, configBean);
        multEurekaMap.put(registerCenterCode, eurekaClient);
        multRegisterCenter.getMultEurekaCodeMap().put(eurekaClient, registerCenterCode);

        /**
         * 添加autoServiceRegistration,如果有就先删除，再添加
         */
        Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap = multRegisterCenter.getMultRegistrationMap();
        revomeServiceRegistration(registerCenterCode);
        EurekaRegistration registration = eurekaRegistration(eurekaClient, instanceConfigBean, manager, healthCheckHandler);
        MossEurekaAutoServiceRegistration autoServiceRegistration = eurekaAutoServiceRegistration(context, eurekaServiceRegistry(), registration, registration);
        autoServiceRegistration.start();
        multRegistrationMap.put(registerCenterCode, autoServiceRegistration);

        /**
         * 添加 HeartbeatMonitor
         */
        Map<EurekaClient, HeartbeatMonitor> multHeartbeatMonitorMap = multRegisterCenter.getMultHeartbeatMonitorMap();
        multHeartbeatMonitorMap.remove(registerCenterCode);
        multHeartbeatMonitorMap.put(eurekaClient, new HeartbeatMonitor());

    }


    /**
     * 动态删除一个注册中心
     *
     * @param registerCenterCode
     */
    public void revomeEureka(String registerCenterCode) {
        revomeEurekaClientByCode(registerCenterCode);
        revomeServiceRegistration(registerCenterCode);
        remoStaleInstancesBySource(registerCenterCode);


    }

    public void revomeServiceRegistration(String registerCenterCode) {
        Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap = multRegisterCenter.getMultRegistrationMap();
        MossEurekaAutoServiceRegistration mossEurekaAutoServiceRegistration = multRegisterCenter.getMultRegistrationMap().get(registerCenterCode);
        if (null != mossEurekaAutoServiceRegistration) {
            mossEurekaAutoServiceRegistration.stop();
            multRegistrationMap.remove(registerCenterCode);
        }
    }

    public void revomeEurekaClientByCode(String registerCenterCode) {
        Map<String, EurekaClient> multEurekaMap = multRegisterCenter.getMultEurekaMap();
        EurekaClient oldEurekClient = multEurekaMap.get(registerCenterCode);
        if (null != oldEurekClient) {
            multRegisterCenter.getMultEurekaCodeMap().remove(oldEurekClient);
            oldEurekClient.shutdown();
            multEurekaMap.remove(registerCenterCode);
            multRegisterCenter.getMultHeartbeatMonitorMap().remove(oldEurekClient);
        }
    }

    /**
     * 根据注册中心标识删除实例
     *
     * @param source
     */
    public void remoStaleInstancesBySource(String source) {
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
        this.revomeEureka(code);
    }

    @Override
    public void addRegistry(String code, String url) {
        this.addEureka(code, url);
    }
}
