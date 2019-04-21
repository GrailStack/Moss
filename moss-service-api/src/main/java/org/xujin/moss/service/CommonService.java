package org.xujin.moss.service;


import org.xujin.moss.model.AppModel;
import org.xujin.moss.vo.BasicDashboardVO;

public interface CommonService {

    /**
     * 根据应用的唯一Id查询
     * @param appId
     */
     AppModel findAppExtendInfo(String appId);

     BasicDashboardVO initBasicDashboard(String userName);

     boolean judgeIsProdEnv();

}
