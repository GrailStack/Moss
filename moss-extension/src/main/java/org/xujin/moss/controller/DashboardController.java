package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.util.ReactorUtils;
import org.xujin.moss.model.AppModel;
import org.xujin.moss.vo.BasicDashboardVO;
import org.xujin.moss.service.AppService;
import org.xujin.moss.service.CommonService;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class DashboardController  extends BaseController{

    private static final Log log = LogFactory.getLog(DashboardController.class);

    @Autowired
    private InstanceRegistry registry;

    @Autowired
    private CommonService commonService;

    @Autowired
    private AppService appService;


    @GetMapping("/dashboard/basic")
    public ResultData basicDashboardModel(){
        String userName = this.getUserNameByToken();
        int myAppNum= 0;
        try {
            myAppNum = 0;
            List<AppModel> appModels= appService.findAllByParamter("","",userName);
            for (AppModel appModel:appModels) {
                if(StringUtils.isEmpty(appModel.getSpringApplicationName())){
                    continue;
                }
                Flux<Instance> instanceFlux=registry.getInstances(appModel.getSpringApplicationName());
                List<Instance> instances = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().filter(Instance::isRegistered).collect(Collectors.toList());
                if(null!=instances&&instances.size()>0){
                    myAppNum=myAppNum+instances.size();
                }
            }
        } catch (Exception e) {
            log.error("get My instanceNum is error "+e.getMessage());
        }
        BasicDashboardVO basicDashboardVO = commonService.initBasicDashboard(userName);

        //获取所有已经注册的实例
        Flux<Instance> instanceFlux=registry.getInstances();

        String registerSource = this.getRegisterSource();
        if(StringUtils.isNotEmpty(registerSource)){
            instanceFlux=instanceFlux.filter(instance->registerSource.equalsIgnoreCase(instance.getRegistration().getSource()));
        }
        List<Instance> instances = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().filter(Instance::isRegistered).collect(Collectors.toList());
        basicDashboardVO.setInstanceNum(instances.size());

        //获取所有已经DOWN的实例
        List<Instance> downInstances = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().filter(
                instance -> StatusInfo.STATUS_DOWN.equals(instance.getStatusInfo().getStatus())).collect(Collectors.toList());
        basicDashboardVO.setDownNum(downInstances.size());

        basicDashboardVO.setMyInstanNum(myAppNum);
        return ResultData.ok(basicDashboardVO).build();
    }

    @GetMapping("/dashboard/report")
    public ResultData report(){
        return ResultData.ok(appService.getUseReport()).build();
    }




}
