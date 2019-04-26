package org.xujin.moss.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.App;
import org.xujin.moss.enums.AppTakeOverEnum;
import org.xujin.moss.exception.ApplicationException;
import org.xujin.moss.mapper.AppMapper;
import org.xujin.moss.model.AppModel;
import org.xujin.moss.model.MenuModel;
import org.xujin.moss.model.MetaDataModel;
import org.xujin.moss.model.ReportModel;
import org.xujin.moss.model.trace.*;
import org.xujin.moss.request.AppPageRequest;
import org.xujin.moss.service.AppService;
import org.xujin.moss.service.DictService;
import org.xujin.moss.service.MenuService;
import org.xujin.moss.utils.BeanMapper;

import java.util.*;

/**
 * AppService实现
 * @author xujin
 */
@Service
public class AppServiceImpl implements AppService {

    private static final Log log = LogFactory.getLog(AppServiceImpl.class);

    @Value("${moss.skywalking.url}")
    private  String skyWalKing_Url;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DictService dictService;

    @Transactional
    @Override
    public void addApp(AppModel appModel) {
        appMapper.insert(BeanMapper.map(appModel,App.class));
    }

    /**
     * 查询所有的App
     * @param appId
     * @return
     */
    @Override
    public List<AppModel> findAllByParamter(String appId,String ownerName,String ownerId) {
        Map<String, Object> param = new HashMap<>();
        if(StringUtils.isNotEmpty(appId)){
            param.put("spring_application_name", appId);
        }
        if(StringUtils.isNotEmpty(ownerName)){
            param.put("owner_name", ownerName);
        }
        if(StringUtils.isNotEmpty(ownerId)){
            param.put("owner_id", ownerId);
        }
        List<App> apps = appMapper.selectByMap(param);
        return BeanMapper.mapList(apps,App.class,AppModel.class);
    }


    /**
     * 根据查询条件查询App并分页
     * @param appPageRequest
     * @return
     */
    @Override
    public PageResult<AppModel> findByPageVague(AppPageRequest appPageRequest) {
        Page pageRequest = new Page(appPageRequest.getPageNo(), appPageRequest.getPageSize());
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(appPageRequest.getProjectName())){
            queryWrapper.like("project_name", appPageRequest.getProjectName());
        }
        if(StringUtils.isNotEmpty(appPageRequest.getStatus())){
            queryWrapper.eq("status",Integer.valueOf(appPageRequest.getStatus()).intValue());
        }
        if(StringUtils.isNotEmpty(appPageRequest.getName())){
            queryWrapper.like("app_id", appPageRequest.getName().toLowerCase());
        }
        if(StringUtils.isNotEmpty(appPageRequest.getTakeOver())){
            queryWrapper.eq("take_over",Long.valueOf(appPageRequest.getTakeOver()).intValue());
        }
        IPage<App> page=appMapper.selectPage(pageRequest, queryWrapper);
        List<AppModel> list= BeanMapper.mapList(page.getRecords(),App.class,AppModel.class);
        PageResult<AppModel> pageResult=new PageResult<AppModel>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setList(list);
        pageResult.setTotalPage(page.getSize());
        return pageResult;
    }

    @Override
    @Transactional
    public void update(AppModel appModel)  {
        appMapper.updateById(BeanMapper.map(appModel, App.class));
    }

    @Override
    public ResultData getTraceTopology(String appName) {
        ResultData resultData = ResultData.builder().build();
        //Skywalking的URL
        String traceBaseUrl = skyWalKing_Url;
        //查询出SkyWalking中的所有应用信息
        List<Application> applications = findAllApplications(traceBaseUrl);
        String targetApplicationId = null;
        for (Application application : applications) {
            //当name与Skywalking的应用名相同,返回对应的ID
            if (appName.equalsIgnoreCase(application.getLabel())) {
                targetApplicationId = application.getKey();
                break;
            }
        }
        //根据SKywalking中的traceAppId获取对应的拓扑信息
        if (null != targetApplicationId) {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            RequestPayload requestPayload = payloadBuilder(targetApplicationId);
            String requestJson = JSON.toJSONString(requestPayload);
            HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
            try {
                String applicationsForSkywalkingUrl = traceBaseUrl + "/api/trace/applications";
                String data = restTemplate.postForObject(applicationsForSkywalkingUrl,  entity, String.class);
                log.info(data);
                TopologyResult topologyResult = JSON.parseObject(data, TopologyResult.class);
                if (topologyResult == null) {
                    log.info("call /api/trace/applications has exception");
                    resultData= ResultData.builder().code(200).msgCode("500").msgContent("call /api/trace/applications has exception").build();
                }
                resultData= ResultData.builder().data(topologyResult.getData()).build();
            } catch (RestClientException e) {
                log.info("call application trace has exception" + e.getMessage());
            }
        }
        return resultData;
    }

    @Override
    public Map<String,Object> getUseReport() {
        Map<String,Object> map=new HashMap<>();
        map.put("count", appMapper.totalCount());
        List<ReportModel>  scList=new ArrayList<ReportModel>();
        List<ReportModel>  sbList=new ArrayList<ReportModel>();

        List<MetaDataModel> sclist= dictService.findDictDataByDictCodeAndStatus(Constants.DICT_DATA_STATUS_TRUE,Constants.SPRING_CLOUD_VERSION);
        for (MetaDataModel model:sclist) {
            ReportModel reportModel=new ReportModel();
            reportModel.setName(model.getName());
            reportModel.setCount(appMapper.totalUseScVersionCount(Integer.parseInt(model.getValue())));
            scList.add(reportModel);
        }
        map.put("springCloudVersions",scList);


        List<MetaDataModel> sblist= dictService.findDictDataByDictCodeAndStatus(Constants.DICT_DATA_STATUS_TRUE,Constants.SPRING_BOOT_VERSION);
        for (MetaDataModel model:sblist) {
            ReportModel reportModel=new ReportModel();
            reportModel.setName(model.getName());
            reportModel.setCount(appMapper.totalUseSbVersionCount(Integer.parseInt(model.getValue())));
            sbList.add(reportModel);
        }
        map.put("sprintBootVersions",sbList);

        List<ReportModel>  takeOverList=new ArrayList<ReportModel>();
        for (AppTakeOverEnum appTakeOverEnum : AppTakeOverEnum.values()) {
            ReportModel reportModel=new ReportModel();
            reportModel.setName(appTakeOverEnum.getDesc());
            reportModel.setCount(appMapper.totalTakeOverCount(appTakeOverEnum.getValue()));
            takeOverList.add(reportModel);
        }
        map.put("takeOver",takeOverList);
        return map;
    }

    @Transactional
    @Override
    public boolean checkAppAndMappingName(String appName) {
        Map<String, Object> param = new HashMap<>();
        if(StringUtils.isNotEmpty(appName)){
            param.put("app_id", appName.toLowerCase());
        }
        List<App> apps = appMapper.selectByMap(param);
        if(null==apps||apps.size()==0){
            return false;
        }
        App app=apps.get(0);
        app.setSpringApplicationName(appName.toLowerCase());
        appMapper.updateById(app);
        return true;

    }

    @Override
    public int totalAppCount() {
        return appMapper.totalCount();
    }

    @Override
    public int totalMyAppCount(String userName) {
        return appMapper.totalMyAppCount(userName);
    }

    @Override
    public AppModel findAppById(Long id) {
        return BeanMapper.map(appMapper.selectById(id),AppModel.class);
    }


    /**
     * 通过访问skywalking的地址获取skywalking中的AppId
     * @param traceBaseUrl
     * @return
     */
    private List<Application> findAllApplications(String traceBaseUrl) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        RequestPayload requestPayload = payloadBuilder();
        String requestJson = JSON.toJSONString(requestPayload);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        try {
            String applicationsForSkywalkingUrl = traceBaseUrl + "/api/applications";
            String data = restTemplate.postForObject(applicationsForSkywalkingUrl,  entity, String.class);
            log.info(data);
            ApplicationsResult applicationsResult = JSON.parseObject(data, ApplicationsResult.class);
            if (applicationsResult == null) {
                log.info("call /api/applications has exception");
                throw new ApplicationException("call /api/applications has exception");
            }
            return applicationsResult.getData().getApplications();
        } catch (RestClientException e) {
            log.info("call applications has exception" + e.getMessage());
        }

        return null;
    }

    private static RequestPayload payloadBuilder() {
        String queryGraphql = "query applications($duration: Duration!) {\n" +
                "  applications: getAllApplication(duration: $duration) {\n" +
                "    key: id\n" +
                "    label: name\n" +
                "  }\n" +
                "}";
        RequestPayload requestPayload = new RequestPayload();
        requestPayload.setQuery(queryGraphql);
        Variables variables = new Variables();
        variables.setDuration(durationBuilder());
        requestPayload.setVariables(variables);
        return requestPayload;
    }

    private static RequestPayload payloadBuilder(String applicationId) {
        String queryGraphql = "query Application($applicationId: ID!, $duration: Duration!) {\n" +
                "    getClusterTopology: getApplicationTopology(applicationId: $applicationId, duration: $duration) {\n" +
                "      nodes {\n" +
                "        id\n" +
                "        name\n" +
                "        type\n" +
                "        ... on ApplicationNode {\n" +
                "          sla\n" +
                "          cpm\n" +
                "          avgResponseTime\n" +
                "          apdex\n" +
                "          isAlarm\n" +
                "          numOfServer\n" +
                "          numOfServerAlarm\n" +
                "          numOfServiceAlarm\n" +
                "        }\n" +
                "      }\n" +
                "      calls {\n" +
                "        source\n" +
                "        target\n" +
                "        isAlert\n" +
                "        callType\n" +
                "        cpm\n" +
                "        avgResponseTime\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        RequestPayload requestPayload = new RequestPayload();
        requestPayload.setQuery(queryGraphql);
        Variables variables = new Variables();
        variables.setDuration(durationBuilder());
        variables.setApplicationId(applicationId);
        requestPayload.setVariables(variables);
        return requestPayload;
    }

    private static Duration durationBuilder() {
        Date endDate = new Date();
        String end = DateFormatUtils.format(endDate, "yyyy-MM-dd HHmm");
        Date StartDate = DateUtils.addMinutes(endDate, -15);
        String start = DateFormatUtils.format(StartDate, "yyyy-MM-dd HHmm");

        Duration duration = new Duration();
        duration.setStart(start);
        duration.setEnd(end);
        duration.setStep("MINUTE");
        return duration;
    }
}
