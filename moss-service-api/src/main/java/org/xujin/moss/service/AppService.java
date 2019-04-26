package org.xujin.moss.service;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.request.AppPageRequest;
import org.xujin.moss.model.AppModel;

import java.util.List;
import java.util.Map;

public interface AppService {

    void addApp(AppModel appModel) ;

    List<AppModel> findAllByParamter(String appId,String ownerName,String ownerId);


    PageResult<AppModel> findByPageVague(AppPageRequest appPageRequest);

    void update(AppModel appModel) ;

    ResultData getTraceTopology(String appName);

    Map<String,Object> getUseReport();

    boolean checkAppAndMappingName(String appName);

    int totalAppCount();

    int totalMyAppCount(String userName);

    AppModel findAppById(Long id);

}
