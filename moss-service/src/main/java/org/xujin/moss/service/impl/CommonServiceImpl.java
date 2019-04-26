package org.xujin.moss.service.impl;

import org.xujin.moss.model.AppModel;
import org.xujin.moss.vo.BasicDashboardVO;
import org.xujin.moss.model.UserRoleModel;
import org.xujin.moss.service.AppService;
import org.xujin.moss.service.CommonService;
import org.xujin.moss.service.ProjectService;
import org.xujin.moss.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Common Service
 * @author xujin
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private Environment environment;

    @Autowired
    private AppService appService;


     @Autowired
     private ProjectService projectService;

    @Autowired
    private UserRolesService userRolesService;

    /**
     * 获取App的扩展信息
     * @param appId
     * @return
     */
    @Override
    public AppModel findAppExtendInfo(String appId) {
        List<AppModel> list= appService.findAllByParamter(appId,"","");
        if(null==list||list.size()==0){
           return null;
        }
        AppModel appModel=list.get(0);
        return appModel;
    }

    /**
     * 初始化Dashboard的基本信息
     * @return
     */
    @Override
    public BasicDashboardVO initBasicDashboard(String userName) {
        BasicDashboardVO basicDashboardVO = new BasicDashboardVO();
        basicDashboardVO.setAppNum(appService.totalAppCount());
        basicDashboardVO.setProjectNum(projectService.totalProjectCount());
        basicDashboardVO.setMyAppNum(appService.totalMyAppCount(userName));
        basicDashboardVO.setMyProjectNum(projectService.totalProjectCountByOwnerId(userName));
        List<UserRoleModel> userRoleModelList=userRolesService.getByUsername(userName);
        if(null!=userRoleModelList&&userRoleModelList.size()>0){
            basicDashboardVO.setRole(userRoleModelList.get(0).getRole());
        }
        return basicDashboardVO;
    }


    /**
     * 判断是否生产环境
     * @return
     */
    @Override
    public boolean judgeIsProdEnv() {
        String env=environment.getProperty("spring.profiles.active");
        if("PROD".equals(env)){
            return true ;
        }
        return false;
    }




}
