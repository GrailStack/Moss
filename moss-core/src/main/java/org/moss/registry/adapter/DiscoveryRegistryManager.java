package org.moss.registry.adapter;

public interface DiscoveryRegistryManager {
    void removeRegistry(String code);
    void addRegistry(String code, String url);
}
