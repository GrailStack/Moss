package de.codecentric.boot.admin.server.cloud.extension;

import org.springframework.context.ApplicationEvent;

/**
 * @author yangfan
 * @date 2019/09/03
 */
public class RegisterCenterRefreshEvent extends ApplicationEvent {

    public RegisterCenterRefreshEvent(Object source) {
        super(source);
    }
}
