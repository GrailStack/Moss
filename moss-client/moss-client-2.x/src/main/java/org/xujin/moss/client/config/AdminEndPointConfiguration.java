package org.xujin.moss.client.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.support.ServiceDiscoveryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.xujin.moss.client.endpoint.*;

@Configuration
@EnableConfigurationProperties(LogFileRegistry.class)
public class AdminEndPointConfiguration {

    @Autowired
    private Environment env;

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
        return new MossMetricsEndpoint();
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
    public SimpleInfoContributor springBootVersionInfoContributor() {
        return new SimpleInfoContributor("spring-boot-version", SpringBootVersion.getVersion());
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
