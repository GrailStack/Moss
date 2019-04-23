package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.enums.AppStatusEnum;
import org.xujin.moss.request.AppMqTraceRequest;
import org.xujin.moss.request.AppPageRequest;
import org.xujin.moss.model.AppModel;
import org.xujin.moss.model.DOMSelectModel;
import org.xujin.moss.model.DOMSelectOptionsModel;
import org.xujin.moss.service.AppService;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 应用相关的管理接口
 * @author xujin
 */
@RestController
@RequestMapping("/admin/app")
public class AppController {


    @Autowired
    private InstanceRegistry registry;

    @Autowired
    private AppService appService;


    /**
     * 增加一个应用
     * @param appModel
     * @return
     */
    @PostMapping("/add")
    public ResultData addApp(@RequestBody AppModel appModel) {
        ResultData result = new ResultData();
        appModel.setIsDeleted(Constants.IS_DELETE_FALSE);
        appService.addApp(appModel);
        return result;
    }

    @GetMapping("/{id}")
    public ResultData getAppById(@PathVariable Long id) {
        AppModel appModel=appService.findAppById(id);
        return ResultData.builder().data(appModel).build();
    }


    /**
     * 更新一个应用
     * @param appModel
     * @return
     */
    @PostMapping("/update")
    public ResultData updateApp(@RequestBody AppModel appModel) {
        appService.update(appModel);
        return ResultData.builder().build();
    }

    /**
     * 接入应用管理
     * @param appPageRequest
     * @return
     */
    @PostMapping("/searchByPage")
    ResultData searchAppByPage(@RequestBody AppPageRequest appPageRequest) {
        PageResult<AppModel> pageResult= appService.findByPageVague(appPageRequest);
        return ResultData.builder().data(pageResult).build();
    }

    /**
     * 接入管理搜索下拉列表
     * @return
     */
    @GetMapping("/queryCriteria")
    public ResultData getSearchToobar() {
        return ResultData.builder().data(Arrays.asList(createProjectDOMSelect(),createStatueDOMSelect())).build();
    }



    /**
     * 根据应用名从skywalking中查询应用的调用拓扑信息
     * @param name
     * @return
     */
    @GetMapping("/trace/{name}")
    public ResultData appDepsTopo(@PathVariable("name") String name) {
        if(StringUtils.isEmpty(name)){
            return ResultData.builder().code(200).msgCode("400").msgContent("appName is null").build();
        }
        ResultData resultData = null;
        try {
            resultData = appService.getTraceTopology(name);
        } catch (Exception e) {
            return ResultData.builder().code(200).msgCode("500").msgContent("service exception,e:"+e.getMessage()).build();
        }
        return resultData;
    }

    private DOMSelectModel createProjectDOMSelect(){
        DOMSelectModel ret=new DOMSelectModel("projectName","归属项目",new ArrayList<>());
        ret.getOptions().add(new DOMSelectOptionsModel("","全部"));
        HashSet<String> projectNameSet=new HashSet<>();
        appService.findAllByParamter("","","").forEach(appModel -> {
            if(!projectNameSet.contains(appModel.getProjectName())){
                projectNameSet.add(appModel.getProjectName());
                ret.getOptions().add(new DOMSelectOptionsModel(appModel.getProjectName(),appModel.getProjectKey()));
            }
        });
        return ret;
    }
    private DOMSelectModel createStatueDOMSelect(){
        DOMSelectModel ret=new DOMSelectModel("status","状态",new ArrayList<>());
        ret.getOptions().add(new DOMSelectOptionsModel("","全部"));
        AppStatusEnum.status.forEach((k, v)->{
            ret.getOptions().add(new DOMSelectOptionsModel(k,v));
        });
        return ret;
    }

}
