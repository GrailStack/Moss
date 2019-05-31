package org.xujin.moss.client.config;

import org.springframework.boot.actuate.autoconfigure.ManagementContextConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

/**
 * @Author: xujin
 **/
@ManagementContextConfiguration
@ConditionalOnWebApplication
public class ManagementConfig {

}
