package de.codecentric.boot.admin.server.cloud.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultRegisterCenterServerMgmtConfig {

    private static final Logger log = LoggerFactory.getLogger(MultRegisterCenterServerMgmtConfig.class);

    /**
     * 动态添加一个注册中心
     * @param registerCenterCode
     * @param registerCenterUrl
     */
    public void addEureka(String registerCenterCode,String registerCenterUrl) {


    }


    /**
     * 动态删除一个注册中心
     * @param registerCenterCode
     */
    public void revomeEureka(String registerCenterCode){
        revomeEurekaClientByCode(registerCenterCode);
        revomeServiceRegistration(registerCenterCode);
        remoStaleInstancesBySource(registerCenterCode);



    }

    public void revomeServiceRegistration(String registerCenterCode) {

    }

    public void revomeEurekaClientByCode(String registerCenterCode) {

    }

    /**
     * 根据注册中心标识删除实例
     * @param source
     */
    public  void remoStaleInstancesBySource(String source) {


    }

}
