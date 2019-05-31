//package de.codecentric.boot.admin.server.cloud.extension;
//
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
//import org.springframework.context.SmartLifecycle;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//public class MultRegisterCenter implements SmartLifecycle {
//
//    private Map<String, DiscoveryClient> multEurekaMap=new ConcurrentHashMap<String, DiscoveryClient>();
//
//    private Map<DiscoveryClient, String> multEurekaCodeMap=new ConcurrentHashMap<DiscoveryClient, String>();
//
//    private Map<DiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap=new ConcurrentHashMap<DiscoveryClient, HeartbeatMonitor>();
//
//
//    protected Map<String, MossAutoServiceRegistration> multRegistrationMap;
//
//    public MultRegisterCenter() {
//    }
//
//    public MultRegisterCenter(Map<String, DiscoveryClient> multEurekaMap) {
//        this.multEurekaMap = multEurekaMap;
//        this.multEurekaCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
//    }
//
//    public MultRegisterCenter(Map<String, DiscoveryClient> multEurekaMap, Map<String, MossAutoServiceRegistration> multRegistrationMap) {
//        this.multEurekaMap = multEurekaMap;
//        this.multRegistrationMap = multRegistrationMap;
//        this.multEurekaCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
//    }
//
//    public MultRegisterCenter(Map<String, DiscoveryClient> multEurekaMap, Map<String, MossAutoServiceRegistration> multRegistrationMap,
//                              Map<DiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap) {
//        this.multEurekaMap = multEurekaMap;
//        this.multRegistrationMap = multRegistrationMap;
//        this.multEurekaCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
//        this.multHeartbeatMonitorMap=multHeartbeatMonitorMap;
//    }
//
//
//    public Map<String, DiscoveryClient> getMultEurekaMap() {
//        return multEurekaMap;
//    }
//
//    public void setMultEurekaMap(Map<String, DiscoveryClient> multEurekaMap) {
//        this.multEurekaMap = multEurekaMap;
//    }
//
//    public Map<String, MossAutoServiceRegistration> getMultRegistrationMap() {
//        return multRegistrationMap;
//    }
//
//    public void setMultRegistrationMap(Map<String, MossAutoServiceRegistration> multRegistrationMap) {
//        this.multRegistrationMap = multRegistrationMap;
//    }
//
//    public synchronized void shutdown() {
//        // multEurekaMap.entrySet().forEach(e -> e.getValue().shutdown());
//
//    }
//
//    @Override
//    public void start() {
//        multRegistrationMap.entrySet().forEach(e -> e.getValue().start());
//    }
//
//    @Override
//    public void stop() {
//        multRegistrationMap.entrySet().forEach(e -> e.getValue().stop());
//    }
//
//    @Override
//    public boolean isRunning() {
//        for (Map.Entry<String, MossAutoServiceRegistration> registration : multRegistrationMap.entrySet()) {
//            if (!registration.getValue().isRunning()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public Map<DiscoveryClient, String> getMultEurekaCodeMap() {
//        return multEurekaCodeMap;
//    }
//
//    public void setMultEurekaCodeMap(Map<DiscoveryClient, String> multEurekaCodeMap) {
//        this.multEurekaCodeMap = multEurekaCodeMap;
//    }
//
//    public Map<DiscoveryClient, HeartbeatMonitor> getMultHeartbeatMonitorMap() {
//        return multHeartbeatMonitorMap;
//    }
//
//    public void setMultHeartbeatMonitorMap(Map<DiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap) {
//        this.multHeartbeatMonitorMap = multHeartbeatMonitorMap;
//    }
//}
