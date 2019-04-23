package org.xujin.moss.endpoint;

import org.xujin.moss.client.endpoint.dependency.analyzer.JarDependencies;
import org.xujin.moss.utils.Analyzer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static org.xujin.moss.config.AdminEndpointApplicationRunListener.SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE;

/**
 * 应用用了哪些版本
 * @author xujin
 */
public class AppInfoEndPoint extends AbstractNamedMvcEndpoint {

    private static final Log logger = LogFactory.getLog(AppInfoEndPoint.class);

    @Autowired
    private Environment env;

    private Map<String, Object> cache = new ConcurrentHashMap<>();

    public AppInfoEndPoint() {
        super("appInfo", SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE+"/appInfo", true);
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isSensitive() {
        return true;
    }


    @GetMapping
    @ResponseBody
    public Object invoke() {
        return cache;
    }

    private String nullToEmpty(Object o){
        if(null==o){
            return "";
        }
        return o.toString();
    }

    @PostConstruct
    public void init() {
        CompletableFuture.runAsync(() -> {
            String appName = env.getProperty("spring.application.name", "");
            try {
                JarDependencies dependencies = Analyzer.getAllPomInfo();
                cache.put("appName", nullToEmpty(appName));
                cache.put("springBootVersion",nullToEmpty(dependencies.getSpringBootVersion()));
                cache.put("springCloudVersion",nullToEmpty(dependencies.getSpringCloudVersion()));
                List<HashMap<String, String>> list = new ArrayList<>();
                dependencies.getPomInfos().forEach(p -> {
                    if (p.getGroupId().equals("org.springframework.cloud")
                        && p.getArtifactId().startsWith("spring-cloud")) {
                        HashMap<String, String> kv = new HashMap<>();
                        kv.put(p.getArtifactId(), p.getVersion());
                        list.add(kv);
                    }
                });
                cache.put("using", list);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
