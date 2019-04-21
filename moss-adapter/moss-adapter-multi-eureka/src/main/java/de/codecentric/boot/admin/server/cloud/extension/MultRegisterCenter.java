package de.codecentric.boot.admin.server.cloud.extension;

import com.netflix.discovery.EurekaClient;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.context.SmartLifecycle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MultRegisterCenter implements SmartLifecycle {

    private Map<String, EurekaClient> multEurekaMap=new ConcurrentHashMap<String, EurekaClient>();

    private Map<EurekaClient, String> multEurekaCodeMap=new ConcurrentHashMap<EurekaClient, String>();

    private Map<EurekaClient, HeartbeatMonitor> multHeartbeatMonitorMap=new ConcurrentHashMap<EurekaClient, HeartbeatMonitor>();


    protected Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap;

    public MultRegisterCenter() {
    }

    public MultRegisterCenter(Map<String, EurekaClient> multEurekaMap) {
        this.multEurekaMap = multEurekaMap;
        this.multEurekaCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    public MultRegisterCenter(Map<String, EurekaClient> multEurekaMap, Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap) {
        this.multEurekaMap = multEurekaMap;
        this.multRegistrationMap = multRegistrationMap;
        this.multEurekaCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }

    public MultRegisterCenter(Map<String, EurekaClient> multEurekaMap, Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap,
                              Map<EurekaClient, HeartbeatMonitor> multHeartbeatMonitorMap) {
        this.multEurekaMap = multEurekaMap;
        this.multRegistrationMap = multRegistrationMap;
        this.multEurekaCodeMap = multEurekaMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
        this.multHeartbeatMonitorMap=multHeartbeatMonitorMap;
    }


    public Map<String, EurekaClient> getMultEurekaMap() {
        return multEurekaMap;
    }

    public void setMultEurekaMap(Map<String, EurekaClient> multEurekaMap) {
        this.multEurekaMap = multEurekaMap;
    }

    public Map<String, MossEurekaAutoServiceRegistration> getMultRegistrationMap() {
        return multRegistrationMap;
    }

    public void setMultRegistrationMap(Map<String, MossEurekaAutoServiceRegistration> multRegistrationMap) {
        this.multRegistrationMap = multRegistrationMap;
    }

    public synchronized void shutdown() {
        multEurekaMap.entrySet().forEach(e -> e.getValue().shutdown());

    }

    @Override
    public void start() {
        multRegistrationMap.entrySet().forEach(e -> e.getValue().start());
    }

    @Override
    public void stop() {
        multRegistrationMap.entrySet().forEach(e -> e.getValue().stop());
    }

    @Override
    public boolean isRunning() {
        for (Map.Entry<String, MossEurekaAutoServiceRegistration> registration : multRegistrationMap.entrySet()) {
            if (!registration.getValue().isRunning()) {
                return false;
            }
        }
        return true;
    }

    public Map<EurekaClient, String> getMultEurekaCodeMap() {
        return multEurekaCodeMap;
    }

    public void setMultEurekaCodeMap(Map<EurekaClient, String> multEurekaCodeMap) {
        this.multEurekaCodeMap = multEurekaCodeMap;
    }

    public Map<EurekaClient, HeartbeatMonitor> getMultHeartbeatMonitorMap() {
        return multHeartbeatMonitorMap;
    }

    public void setMultHeartbeatMonitorMap(Map<EurekaClient, HeartbeatMonitor> multHeartbeatMonitorMap) {
        this.multHeartbeatMonitorMap = multHeartbeatMonitorMap;
    }
}
