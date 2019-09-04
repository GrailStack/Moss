package de.codecentric.boot.admin.server.cloud.extension;

import com.ecwid.consul.v1.ConsulClient;
import lombok.Data;
import org.springframework.cloud.client.discovery.event.HeartbeatMonitor;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class MultRegisterCenter {

    private Map<String, ConsulDiscoveryClient> multConsulMap = new ConcurrentHashMap<>();
    private Map<ConsulDiscoveryClient, String> multConsulCodeMap=new ConcurrentHashMap<>();
    private Map<ConsulDiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap=new ConcurrentHashMap<>();
    public MultRegisterCenter() {
    }

    public MultRegisterCenter(Map<String, ConsulDiscoveryClient> multConsulMap, Map<ConsulDiscoveryClient, HeartbeatMonitor> multHeartbeatMonitorMap) {
        this.multConsulMap = multConsulMap;
        this.multHeartbeatMonitorMap = multHeartbeatMonitorMap;
        this.multConsulCodeMap = multConsulMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    }
}
