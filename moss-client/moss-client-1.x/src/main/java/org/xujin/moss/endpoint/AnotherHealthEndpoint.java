package org.xujin.moss.endpoint;

import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static org.xujin.moss.config.AdminEndpointApplicationRunListener.SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE;

/**
 * @Description: 提供 "/actuator/health"
 * @Author: xujin
 **/
public class AnotherHealthEndpoint  extends AbstractNamedMvcEndpoint {
    private final HealthIndicator healthIndicator;

    /**
     * Time to live for cached result, in milliseconds.
     */
    private long timeToLive = 1000;

    /**
     * Create a new {@link AnotherHealthEndpoint} instance.
     * @param healthAggregator the health aggregator
     * @param healthIndicators the health indicators
     */
    public AnotherHealthEndpoint(HealthAggregator healthAggregator,
                          Map<String, HealthIndicator> healthIndicators) {
        super("anotherHealth", SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE+"/health", true);
        Assert.notNull(healthAggregator, "HealthAggregator must not be null");
        Assert.notNull(healthIndicators, "HealthIndicators must not be null");
        CompositeHealthIndicator healthIndicator = new CompositeHealthIndicator(
                healthAggregator);
        for (Map.Entry<String, HealthIndicator> entry : healthIndicators.entrySet()) {
            healthIndicator.addHealthIndicator(getKey(entry.getKey()), entry.getValue());
        }
        this.healthIndicator = healthIndicator;
    }

    /**
     * Time to live for cached result. This is particularly useful to cache the result of
     * this endpoint to prevent a DOS attack if it is accessed anonymously.
     * @return time to live in milliseconds (default 1000)
     */
    public long getTimeToLive() {
        return this.timeToLive;
    }

    /**
     * Set the time to live for cached results.
     * @param timeToLive the time to live in milliseconds
     */
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * Invoke all {@link HealthIndicator} delegates and collect their health information.
     */
    @GetMapping
    @ResponseBody
    public Health invoke() {
        return this.healthIndicator.health();
    }

    /**
     * Turns the bean name into a key that can be used in the map of health information.
     * @param name the bean name
     * @return the key
     */
    private String getKey(String name) {
        int index = name.toLowerCase().indexOf("healthindicator");
        if (index > 0) {
            return name.substring(0, index);
        }
        return name;
    }
}
