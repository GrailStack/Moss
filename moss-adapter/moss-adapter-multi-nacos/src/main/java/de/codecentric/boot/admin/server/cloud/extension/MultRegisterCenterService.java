package de.codecentric.boot.admin.server.cloud.extension;

import java.util.Map;

public interface MultRegisterCenterService {

    /**
     * 获取所有注册中心的URL
     * @return
     */
    Map<String, String> getRegisterCenterList();



}
