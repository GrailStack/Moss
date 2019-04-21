package org.xujin.moss.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementContextConfiguration;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.xujin.moss.endpoint.*;

import java.util.*;

@ManagementContextConfiguration
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
    public InfoLogFileEndPoint infoLogFileEndPoin() {
        return new InfoLogFileEndPoint();
    }

    @Bean
    public ErrorLogFileEndPoint errorLogFileEndPoint() {
        return new ErrorLogFileEndPoint();
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
    public AnotherHealthEndpoint anotherHealthEndpoint(HealthAggregator healthAggregator,
                                                       Map<String, HealthIndicator> healthIndicators){
        return new AnotherHealthEndpoint(healthAggregator,healthIndicators);
    }

}