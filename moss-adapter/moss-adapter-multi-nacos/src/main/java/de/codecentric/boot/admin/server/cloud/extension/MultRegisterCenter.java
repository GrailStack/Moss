package de.codecentric.boot.admin.server.cloud.extension;

import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.context.SmartLifecycle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MultRegisterCenter {

    private Map<String, NamingService> multNacosMap=new ConcurrentHashMap<String, NamingService>();

    private Map<NamingService, String> multNacosCodeMap=new ConcurrentHashMap<NamingService, String>();

    private Map<NamingService, HeartbeatMonitor> multHeartbeatMonitorMap=new ConcurrentHashMap<NamingService, HeartbeatMonitor>();

    protected Map<String, MossNacosAutoServiceRegistration> multRegistrationMap;

    public MultRegisterCenter() {
    }
    public MultRegisterCenter(Map<String, NamingService> multEurekaMap) {
        this.multNacosMap = multEurekaMap;
        this.multNacosCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    public MultRegisterCenter(Map<String, NamingService> multEurekaMap, Map<String, MossNacosAutoServiceRegistration> multRegistrationMap) {
        this.multNacosMap = multEurekaMap;
        this.multRegistrationMap = multRegistrationMap;
        this.multNacosCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    public MultRegisterCenter(Map<String, NamingService> multEurekaMap, Map<String, MossNacosAutoServiceRegistration> multRegistrationMap,
                              Map<NamingService, HeartbeatMonitor> multHeartbeatMonitorMap) {
        this.multNacosMap = multEurekaMap;
        this.multRegistrationMap = multRegistrationMap;
        this.multNacosCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
        this.multHeartbeatMonitorMap=multHeartbeatMonitorMap;
    }

    public Map<String, NamingService> getMultNacosMap() {
        return multNacosMap;
    }

    public void setMultNacosMap(Map<String, NamingService> multNacosMap) {
        this.multNacosMap = multNacosMap;
    }

    public Map<NamingService, String> getMultNacosCodeMap() {
        return multNacosCodeMap;
    }

    public void setMultNacosCodeMap(Map<NamingService, String> multNacosCodeMap) {
        this.multNacosCodeMap = multNacosCodeMap;
    }

    public Map<NamingService, HeartbeatMonitor> getMultHeartbeatMonitorMap() {
        return multHeartbeatMonitorMap;
    }

    public void setMultHeartbeatMonitorMap(Map<NamingService, HeartbeatMonitor> multHeartbeatMonitorMap) {
        this.multHeartbeatMonitorMap = multHeartbeatMonitorMap;
    }

    public Map<String, MossNacosAutoServiceRegistration> getMultRegistrationMap() {
        return multRegistrationMap;
    }

    public void setMultRegistrationMap(Map<String, MossNacosAutoServiceRegistration> multRegistrationMap) {
        this.multRegistrationMap = multRegistrationMap;
    }

}
