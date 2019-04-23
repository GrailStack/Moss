package org.xujin.moss.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.xujin.moss.client.endpoint.*;

@Configuration
public class AdminEndPointConfiguration {

    @Autowired
    private Environment env;

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

}
