package org.xujin.moss.client.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.support.ServiceDiscoveryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.xujin.moss.client.endpoint.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties(LogFileRegistry.class)
public class AdminEndPointConfiguration {

    @Autowired
    private Environment env;


    private final Collection<PublicMetrics> publicMetrics;


    public AdminEndPointConfiguration(ObjectProvider<Collection<PublicMetrics>> publicMetrics) {
        this.publicMetrics = publicMetrics.getIfAvailable();
    }

    @Bean
    public JarDependenciesEndpoint jarDependenciesEndpoint() {
        return new JarDependenciesEndpoint(env);
    }

    @Bean
    public LogFileEndPoint logFileEndPoint(LogFileRegistry logFileRegistry) {
        return new LogFileEndPoint(env, logFileRegistry);
    }


    @Bean
    public AppInfoEndPoint appInfoEndPoint() {
        return new AppInfoEndPoint();
    }

    @Bean
    public MossMetricsEndpoint haloMetricsEndpoint(){
        List<PublicMetrics> publicMetrics = new ArrayList<PublicMetrics>();
        if (this.publicMetrics != null) {
            publicMetrics.addAll(this.publicMetrics);
        }
        Collections.sort(publicMetrics, AnnotationAwareOrderComparator.INSTANCE);
        return new MossMetricsEndpoint(publicMetrics);
    }

    @Bean
    public GCLogEndpoint gcLogEndpoint(){
        return new GCLogEndpoint();
    }

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public CacheManagerEndpoint cacheManagerEndpoint(){
        return new CacheManagerEndpoint();
    }


    @Bean
    public ServiceDiscoveryCustomizer defaultServiceDiscoveryCustomizer(CuratorFramework curator,
                                                                        ZookeeperDiscoveryProperties properties,
                                                                        InstanceSerializer<ZookeeperInstance> serializer) {
        return new AutoRegistrationCustomizer(curator, properties, serializer);
    }
    @Bean
    public MetaDataProvider metaDataProvider() {
        return new MetaDataProvider();
    }
}