//package org.xujin.moss.service.impl;
//
//import de.codecentric.boot.admin.server.cloud.extension.MultRegisterCenterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.xujin.moss.constant.Constants;
//import org.xujin.moss.entity.RegisterCenter;
//import org.xujin.moss.mapper.RegisterCenterMapper;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class SwitchMultEurekaServiceImpl  implements MultRegisterCenterService {
//
//    @Autowired
//    private RegisterCenterMapper registerCenterMapper;
//
//    @Override
//    public Map<String, String> getRegisterCenterList() {
//
//        Map<String, String> map=new HashMap<>();
//        List<RegisterCenter> registerCenters= registerCenterMapper.findRegisterCenterListByStatus(Constants.REGISTER_CENTER_ENABLE);
//        for (RegisterCenter registerCenter:registerCenters) {
//            map.put(registerCenter.getCode(),registerCenter.getUrl());
//        }
//        return map;
//    }
//
//
//}
