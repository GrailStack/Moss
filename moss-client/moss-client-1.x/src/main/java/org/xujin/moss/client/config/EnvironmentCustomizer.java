package org.xujin.moss.client.config;

import org.springframework.core.env.Environment;

public interface EnvironmentCustomizer<T extends Environment> {
    void customize(T t);
}
