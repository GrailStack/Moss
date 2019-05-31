package org.xujin.moss.client.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xujin.moss.client.config.AdminEndpointApplicationRunListener;
import org.xujin.moss.client.utils.Analyzer;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

/**
 * Jar分析的EndPoint
 * @author xujin
 */
public class JarDependenciesEndpoint  extends AbstractNamedMvcEndpoint {
    private static final Log logger = LogFactory.getLog(JarDependenciesEndpoint.class);
    private Environment env;
    private Object cachedObject;

    public JarDependenciesEndpoint(Environment env) {
        super("jardeps", "/jardeps", true);
        this.env = env;
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