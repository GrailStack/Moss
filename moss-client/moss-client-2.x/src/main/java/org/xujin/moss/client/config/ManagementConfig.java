package org.xujin.moss.client.config;

import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

/**
 * @Author: xujin
 **/
@ManagementContextConfiguration(ManagementContextType.ANY)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ManagementConfig {

}
