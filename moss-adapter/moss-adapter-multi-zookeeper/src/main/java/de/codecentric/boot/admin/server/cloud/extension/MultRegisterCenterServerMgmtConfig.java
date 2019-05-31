//package de.codecentric.boot.admin.server.cloud.extension;
//
//import com.google.common.collect.Maps;
//import de.codecentric.boot.admin.server.domain.entities.Instance;
//import de.codecentric.boot.admin.server.domain.entities.SnapshottingInstanceRepository;
//import de.codecentric.boot.admin.server.eventstore.ConcurrentMapEventStore;
//import de.codecentric.boot.admin.server.services.InstanceRegistry;
//import org.apache.curator.x.discovery.ServiceDiscovery;
//import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
//import org.apache.curator.x.discovery.details.ServiceDiscoveryImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
//import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
//import org.springframework.cloud.client.serviceregistry.Registration;
//import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
//import org.springframework.cloud.commons.util.InetUtils;
//import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
//import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryClient;
//import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
//import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
//import org.springframework.cloud.zookeeper.discovery.dependency.ZookeeperDependencies;
//import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
//import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.EventListener;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.xujin.moss.common.util.ReactorUtils;
//import reactor.core.publisher.Flux;
//
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URL;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//@Configuration
//public class MultRegisterCenterServerMgmtConfig {
//
//    private static final Logger log = LoggerFactory.getLogger(MultRegisterCenterServerMgmtConfig.class);
//    private static Map<String, String> URL_MAP = new ConcurrentHashMap<String, String>();
//    private static MultRegisterCenter multRegisterCenter;
//    @Autowired
//    private InstanceRegistry registry;
//
////    @Autowired(required = false)
////    private ObjectProvider<HealthCheckHandler> healthCheckHandler;
////
////
////    @Autowired(required = false)
////    private AbstractDiscoveryClientOptionalArgs<?> optionalArgs;
//    @Autowired
//    private SnapshottingInstanceRepository snapshottingInstanceRepository;
//    @Autowired
//    private ApplicationContext context;
//    @Autowired
//    private MultRegisterCenterService multRegisterCenterService;
//    @Autowired
//    private ConfigurableEnvironment env;
//    @Autowired
//    private InetUtils inetUtils;
//    @Autowired(required = false)
//    ZookeeperDependencies zookeeperDependencies;
//    @Autowired(required = false)
//    ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;
//    @Autowired
//    AutoServiceRegistrationProperties autoServiceRegistrationProperties;
//
//    @Autowired(required = false)
//    ServiceDiscovery<ZookeeperInstance> curatorServiceDiscovery;
//
//    public static MultRegisterCenter getMultRegisterCenter() {
//        return multRegisterCenter;
//    }
//
//    public static String getCodeByClient(ZookeeperDiscoveryClient client) {
//        return multRegisterCenter.getMultEurekaCodeMap().get(client);
//    }
//
//    public static HeartbeatMonitor getHeartbeatMonitorByClient(ZookeeperDiscoveryClient client) {
//        return multRegisterCenter.getMultHeartbeatMonitorMap().get(client);
//    }
//
//
//    @Bean(destroyMethod = "shutdown")
//    public MultRegisterCenter initMultiZookeeper() {
//        log.info("start init multiple RegisterCenter");
//        URL_MAP = multRegisterCenterService.getRegisterCenterList();
//        if (URL_MAP.isEmpty()) {
//            multRegisterCenter = new MultRegisterCenter(new ConcurrentHashMap<>(),
//                    new ConcurrentHashMap<>(),
//                    new ConcurrentHashMap<>());
//            return multRegisterCenter;
//        }
//        Map<String, DiscoveryClient> multEurekaMap = Maps.newConcurrentMap();
//        Map<String, MossAutoServiceRegistration> multRegistrationMap = Maps.newConcurrentMap();
//        Map<DiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap = new ConcurrentHashMap<>();
//        for(Map.Entry<String/* Registry Name */, String/* Registry URL*/> e : URL_MAP.entrySet()) {
//
//            log.info("start init discovery server : {} / {}", e.getKey(), e.getValue());
//            URL address;
//            try {
//                address = URI.create(e.getValue()).toURL();
//            } catch (MalformedURLException e1) {
//                log.error("{} with {}", e.getKey(), e1.getMessage());
//                continue;
//
//            }
////            ManagementMetadataProvider managementMetadataProvider = serviceManagementMetadataProvider();
////            ZookeeperDiscoveryProperties zookeeperDiscoveryProperties = eurekaClientConfigBean(env, inetUtils);
////
////            configBean.getServiceUrl().clear();
////            configBean.getServiceUrl().put(EurekaClientConfigBean.DEFAULT_ZONE, e.getValue());
////            EurekaInstanceConfigBean instanceConfigBean = eurekaInstanceConfigBean(inetUtils, env, managementMetadataProvider);
////            instanceConfigBean.setEnvironment(env);
////
////            ApplicationInfoManager manager = eurekaApplicationInfoManager(instanceConfigBean);
//            ServiceDiscovery serviceDiscovery = serviceDiscovery(address.getProtocol());
//            DiscoveryClient eurekaClient = discoveryClient(address.getProtocol(), serviceDiscovery);
//            Registration registration = registration(address.getProtocol());
//            MossAutoServiceRegistration autoServiceRegistration = autoServiceRegistration(serviceRegistry(address.getProtocol(), serviceDiscovery), autoServiceRegistrationProperties, registration);
//            multEurekaMap.put(e.getKey(), eurekaClient);
//            multRegistrationMap.put(e.getKey(), autoServiceRegistration);
//            multHeartbeatMonitorMap.put(eurekaClient, new HeartbeatMonitor());
//            log.info("init discovery server:{} / {} end!", e.getKey(), e.getValue());
//        };
//        multRegisterCenter = new MultRegisterCenter(multEurekaMap, multRegistrationMap, multHeartbeatMonitorMap);
//        log.info("init MultRegisterCenter End!");
//        return multRegisterCenter;
//    }
//    @EventListener(RefreshScopeRefreshedEvent.class)
//    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
//
//        //This will force the creation of the EurkaClient bean if not already created
//        //to make sure the client will be reregistered after a refresh event
//        multRegisterCenter.multRegistrationMap.entrySet().forEach(e -> {
//            MossAutoServiceRegistration autoRegistration = e.getValue();
//            if (autoRegistration != null) {
//                // register in case meta data changed
//                autoRegistration.stop();
//                autoRegistration.start();
//            }
//        });
//    }
//
//
//    private ServiceDiscovery serviceDiscovery(String protocol) {
//        switch (protocol) {
//            case "zookeeper":
//                return curatorServiceDiscovery;
//            case "nacos":
//                return null;
//            case "eureka":
//                return null;
//        }
//        return null;
//    }
//
//    private Registration registration(String protocol) {
//        switch (protocol) {
//            case "zookeeper":
//                return ServiceInstanceRegistration.builder().build();
//            case "nacos":
//                return null;
//            case "eureka":
//                return null;
//        }
//        return null;
//    }
//
//    public ServiceRegistry serviceRegistry(String protocol, ServiceDiscovery serviceDiscovery) {
//        switch (protocol) {
//            case "zookeeper": return new ZookeeperServiceRegistry(serviceDiscovery);
//            case "nacos": return null;
//            case "eureka": return null;
//        }
//        return null;
//    }
//
//    public MossAutoServiceRegistration autoServiceRegistration(ServiceRegistry serviceRegistry,
//                                                               AutoServiceRegistrationProperties properties,
//                                                               Registration registration) {
//        return new MossAutoServiceRegistration(serviceRegistry, properties, registration);
//    }
//
//    public DiscoveryClient discoveryClient(String protocol, ServiceDiscovery serviceDiscovery) {
//        switch (protocol) {
//            case "zookeeper": {
//
//                return new ZookeeperDiscoveryClient(serviceDiscovery, zookeeperDependencies, zookeeperDiscoveryProperties);
//            }
//            case "nacos": return null;
//            case "eureka": return null;
//        }
//        return null;
//
//    }
//
//    //
//    public ZookeeperDiscoveryProperties eurekaClientConfigBean(ConfigurableEnvironment env, InetUtils inetUtils) {
//        ZookeeperDiscoveryProperties client = new ZookeeperDiscoveryProperties(inetUtils);
//        if ("bootstrap".equals(env.getProperty("spring.config.name"))) {
//            // We don't register during bootstrap by default, but there will be another
//            // chance later.
//            client.setRegister(false);
//        }
//        return client;
//    }
////
////    //@Bean
////    //@ConditionalOnMissingBean(value = ApplicationInfoManager.class, search = SearchStrategy.CURRENT)
////    public ApplicationInfoManager eurekaApplicationInfoManager(
////            EurekaInstanceConfig config) {
////        InstanceInfo instanceInfo = new InstanceInfoFactory().create(config);
////        return new ApplicationInfoManager(config, instanceInfo);
////    }
////
////    public EurekaRegistration eurekaRegistration(EurekaClient eurekaClient,
////                                                 CloudEurekaInstanceConfig instanceConfig,
////                                                 ApplicationInfoManager applicationInfoManager,
////                                                 @Autowired(required = false) ObjectProvider<HealthCheckHandler> healthCheckHandler) {
////        return EurekaRegistration.builder(instanceConfig)
////                .with(applicationInfoManager)
////                .with(eurekaClient)
////                .with(healthCheckHandler)
////                .build();
////    }
////
////    public ManagementMetadataProvider serviceManagementMetadataProvider() {
////        return new DefaultManagementMetadataProvider();
////    }
//
//    private String getProperty(String property, ConfigurableEnvironment env) {
//        return env.containsProperty(property) ? env.getProperty(property) : "";
//    }
//
//    /**
//     * 构造 EurekaInstanceConfigBean
//     *
//     * @param inetUtils
//     * @param env
//     * @param managementMetadataProvider
//     * @return
//     */
////    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils, ConfigurableEnvironment env
////            , ManagementMetadataProvider managementMetadataProvider) {
////        String hostname = getProperty("eureka.instance.hostname", env);
////        boolean preferIpAddress = Boolean.parseBoolean(getProperty("eureka.instance.prefer-ip-address", env));
////        String ipAddress = getProperty("eureka.instance.ip-address", env);
////        boolean isSecurePortEnabled = Boolean.parseBoolean(getProperty("eureka.instance.secure-port-enabled", env));
////
////        String serverContextPath = env.getProperty("server.context-path", "/");
////        int serverPort = Integer.valueOf(env.getProperty("server.port", env.getProperty("port", "8080")));
////
////        Integer managementPort = env.getProperty("management.server.port", Integer.class);// nullable. should be wrapped into optional
////        String managementContextPath = env.getProperty("management.server.servlet.context-path");// nullable. should be wrapped into optional
////        Integer jmxPort = env.getProperty("com.sun.management.jmxremote.port", Integer.class);//nullable
////        EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);
////
////        instance.setNonSecurePort(serverPort);
////        instance.setInstanceId(getDefaultInstanceId(env));
////        instance.setPreferIpAddress(preferIpAddress);
////        instance.setSecurePortEnabled(isSecurePortEnabled);
////        if (StringUtils.hasText(ipAddress)) {
////            instance.setIpAddress(ipAddress);
////        }
////
////        if (isSecurePortEnabled) {
////            instance.setSecurePort(serverPort);
////        }
////
////        if (StringUtils.hasText(hostname)) {
////            instance.setHostname(hostname);
////        }
////        String statusPageUrlPath = getProperty("eureka.instance.status-page-url-path", env);
////        String healthCheckUrlPath = getProperty("eureka.instance.health-check-url-path", env);
////
////        if (StringUtils.hasText(statusPageUrlPath)) {
////            instance.setStatusPageUrlPath(statusPageUrlPath);
////        }
////        if (StringUtils.hasText(healthCheckUrlPath)) {
////            instance.setHealthCheckUrlPath(healthCheckUrlPath);
////        }
////
////        ManagementMetadata metadata = managementMetadataProvider.get(instance, serverPort,
////                serverContextPath, managementContextPath, managementPort);
////
////        if (metadata != null) {
////            instance.setStatusPageUrl(metadata.getStatusPageUrl());
////            instance.setHealthCheckUrl(metadata.getHealthCheckUrl());
////            if (instance.isSecurePortEnabled()) {
////                instance.setSecureHealthCheckUrl(metadata.getSecureHealthCheckUrl());
////            }
////            Map<String, String> metadataMap = instance.getMetadataMap();
////            if (metadataMap.get("management.port") == null) {
////                metadataMap.put("management.port", String.valueOf(metadata.getManagementPort()));
////            }
////        } else {
////            //without the metadata the status and health check URLs will not be set
////            //and the status page and health check url paths will not include the
////            //context path so set them here
////            if (StringUtils.hasText(managementContextPath)) {
////                instance.setHealthCheckUrlPath(managementContextPath + instance.getHealthCheckUrlPath());
////                instance.setStatusPageUrlPath(managementContextPath + instance.getStatusPageUrlPath());
////            }
////        }
////
////        setupJmxPort(instance, jmxPort);
////        return instance;
////    }
////
////    private void setupJmxPort(EurekaInstanceConfigBean instance, Integer jmxPort) {
////        Map<String, String> metadataMap = instance.getMetadataMap();
////        if (metadataMap.get("jmx.port") == null && jmxPort != null) {
////            metadataMap.put("jmx.port", String.valueOf(jmxPort));
////        }
////    }
//
//
//    /**
//     * 动态添加一个注册中心
//     *
//     * @param registerCenterCode
//     * @param registerCenterUrl
//     */
//    public void addEureka(String registerCenterCode, String registerCenterUrl) {
//
////        ManagementMetadataProvider managementMetadataProvider = serviceManagementMetadataProvider();
////        EurekaClientConfigBean configBean = eurekaClientConfigBean(env);
////        configBean.getServiceUrl().clear();
////        configBean.getServiceUrl().put(EurekaClientConfigBean.DEFAULT_ZONE, registerCenterUrl);
////        EurekaInstanceConfigBean instanceConfigBean = eurekaInstanceConfigBean(inetUtils, env, managementMetadataProvider);
////        instanceConfigBean.setEnvironment(env);
////        instanceConfigBean.setAppname(instanceConfigBean.getAppname());
////        ApplicationInfoManager manager = eurekaApplicationInfoManager(instanceConfigBean);
////
////
////        /**
////         * 添加EurekaClient,如果有就先删除，再添加
////         */
////        Map<String, ZookeeperDiscoveryClient> multEurekaMap = MultRegisterCenterServerMgmtConfig.getMultRegisterCenter().getMultEurekaMap();
////        revomeEurekaClientByCode(registerCenterCode);
////        ZookeeperDiscoveryClient eurekaClient = eurekaClient(manager, configBean);
////        multEurekaMap.put(registerCenterCode, eurekaClient);
////        multRegisterCenter.getMultEurekaCodeMap().put(eurekaClient,registerCenterCode);
////
////        /**
////         * 添加autoServiceRegistration,如果有就先删除，再添加
////         */
////        Map<String, MossZookeeperAutoServiceRegistration> multRegistrationMap= multRegisterCenter.getMultRegistrationMap();
////        revomeServiceRegistration(registerCenterCode);
////        EurekaRegistration registration = eurekaRegistration(eurekaClient, instanceConfigBean, manager, healthCheckHandler);
////        MossZooKeeperAutoServiceRegistration autoServiceRegistration = eurekaAutoServiceRegistration(context, eurekaServiceRegistry(), registration,registration);
////        autoServiceRegistration.start();
////        multRegistrationMap.put(registerCenterCode, autoServiceRegistration);
////
////        /**
////         * 添加 HeartbeatMonitor
////         */
////        Map<ZookeeperDiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap= multRegisterCenter.getMultHeartbeatMonitorMap();
////        multHeartbeatMonitorMap.remove(registerCenterCode);
////        multHeartbeatMonitorMap.put(eurekaClient,new HeartbeatMonitor());
//
//    }
//
//
//    /**
//     * 动态删除一个注册中心
//     *
//     * @param registerCenterCode
//     */
//    public void revomeEureka(String registerCenterCode) {
//        revomeEurekaClientByCode(registerCenterCode);
//        revomeServiceRegistration(registerCenterCode);
//        remoStaleInstancesBySource(registerCenterCode);
//
//
//    }
//
//    public void revomeServiceRegistration(String registerCenterCode) {
//        Map<String, MossAutoServiceRegistration> multRegistrationMap = multRegisterCenter.getMultRegistrationMap();
//        MossAutoServiceRegistration mossEurekaAutoServiceRegistration = multRegisterCenter.getMultRegistrationMap().get(registerCenterCode);
//        if (null != mossEurekaAutoServiceRegistration) {
//            mossEurekaAutoServiceRegistration.stop();
//            multRegistrationMap.remove(registerCenterCode);
//        }
//    }
//
//    public void revomeEurekaClientByCode(String registerCenterCode) {
//        Map<String, DiscoveryClient> multEurekaMap = multRegisterCenter.getMultEurekaMap();
//        DiscoveryClient oldEurekClient = multEurekaMap.get(registerCenterCode);
//        if (null != oldEurekClient) {
//            multRegisterCenter.getMultEurekaCodeMap().remove(oldEurekClient);
//            // oldEurekClient.shutdown();
//            multEurekaMap.remove(registerCenterCode);
//            multRegisterCenter.getMultHeartbeatMonitorMap().remove(oldEurekClient);
//        }
//    }
//
//    /**
//     * 根据注册中心标识删除实例
//     *
//     * @param source
//     */
//    public void remoStaleInstancesBySource(String source) {
//        Flux<Instance> instanceFlux = registry.getInstances().filter(Instance::isRegistered).filter(instance -> source.equalsIgnoreCase(instance.getRegistration().getSource()));
//        List<Instance> instanceList = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().collect(Collectors.toList());
//        for (Instance instance : instanceList) {
//            snapshottingInstanceRepository.revomeInstance(instance.getId());
//            ConcurrentMapEventStore InstanceEventStore = (ConcurrentMapEventStore) snapshottingInstanceRepository.getHaloEventStore();
//            InstanceEventStore.getEventLog().remove(instance.getId());
//
//        }
//
//    }
//
//}
