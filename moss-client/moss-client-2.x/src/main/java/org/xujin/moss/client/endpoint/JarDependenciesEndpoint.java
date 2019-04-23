package org.xujin.moss.client.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xujin.moss.client.utils.Analyzer;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

@RestControllerEndpoint(id = "jardeps")
public class JarDependenciesEndpoint {
    private static final Log logger = LogFactory.getLog(JarDependenciesEndpoint.class);
    private Environment env;

    private Object cachedObject;

    public JarDependenciesEndpoint(Environment env) {
        this.env = env;
    }

    @RequestMapping(method = {RequestMethod.GET})
    public Object invoke() {
        try {
            if (cachedObject == null) {
                cachedObject = Analyzer.getAllPomInfo();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return cachedObject;
    }

    @PostConstruct
    public void init() {
        CompletableFuture.runAsync(() -> {
            try {
                cachedObject = Analyzer.getAllPomInfo();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
