package org.xujin.moss.controller.extension;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.common.util.PagingUtils;
import org.xujin.moss.common.util.ReactorUtils;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.controller.BaseController;
import org.xujin.moss.model.AppModel;
import org.xujin.moss.model.MossInstance;
import org.xujin.moss.service.CommonService;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.eventstore.InstanceEventStore;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * Halo Admin扩展的实例接口
 * @author xujin
 */
@RestController
@RequestMapping("/admin")
public class InstanceController extends BaseController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private  InstanceRegistry registry;


    @Autowired
    private InstanceEventStore eventStore;


    /**
     * 实例列表分页
     * @param appName
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/instances")
    public ResultData search(@RequestParam(value = "appName",required = false, defaultValue = "") String appName,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize){
        Flux<Instance> instanceFlux=null;
        if(StringUtils.isEmpty(appName)){
            instanceFlux=registry.getInstances();
        }else{
            instanceFlux=registry.getInstances(appName.toUpperCase());
        }
        String registerSource = this.getRegisterSource();
        if(StringUtils.isNotEmpty(registerSource)){
            instanceFlux=instanceFlux.filter(instance->registerSource.equalsIgnoreCase(instance.getRegistration().getSource()));
        }
        /**
         * 实例不进行按已注册进行过滤 get().filter(Instance::isRegistered).collect(Collectors.toList())
         */
        List<Instance> instances = ReactorUtils.optional(instanceFlux).map(r -> r.stream()).get().filter(Instance::isRegistered).collect(Collectors.toList());
        PageResult<Instance> pageResult= PagingUtils.pageBuider(instances,pageNum,pageSize).build();
        List<Instance> instanceList=pageResult.getList();
        List<MossInstance> mossInstanceList = new ArrayList<>();
        for (Instance instance: instanceList) {
            MossInstance mossInstance =new MossInstance();
            AppModel appModel= commonService.findAppExtendInfo(instance.getRegistration().getName());
            if(null!=appModel){
                mossInstance.setProjectKey(appModel.getProjectKey());
                mossInstance.setProjectName(appModel.getProjectName());
                mossInstance.setOwnerName(appModel.getOwnerName());
                if(Constants.TAKE_OVER_TRUE==appModel.getTakeOver());{
                    mossInstance.setTakeOver(true);
                }
            }
            mossInstance.setId(instance.getId());
            mossInstance.setVersion(instance.getVersion());
            mossInstance.setRegistration(instance.getRegistration());
            mossInstance.setRegistered(instance.isRegistered());
            mossInstance.setStatusInfo(instance.getStatusInfo());
            mossInstance.setStatusTimestamp(instance.getStatusTimestamp());
            mossInstance.setInfo(instance.getInfo());
            mossInstanceList.add(mossInstance);
        }
        PageResult<MossInstance> pageResul=new PageResult<MossInstance>();
        pageResul.setList(mossInstanceList);
        pageResul.setCurrentPage(pageResult.getCurrentPage());
        pageResul.setTotalCount(pageResult.getTotalCount());
        pageResul.setTotalPage(pageResult.getTotalPage());
        return ResultData.ok(pageResul).build();
    }

    @GetMapping(path = "/instances/{id}")
    public ResultData instance(@PathVariable String id) {
        Instance instance=registry.getInstance(InstanceId.of(id))
                .filter(Instance::isRegistered).block();
        if(null==instance){
            return ResultData.noContent().msgContent("你想操作的实例不是已注册状态").build();
        }
        return  ResultData.ok(instance).build();
    }

    /**
     * 单个实例的Event
     * @param id
     * @return
     */
    @GetMapping(path = "/instances/events/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData eventBInstanceId(@PathVariable String id) {
        List<InstanceEvent> list=ReactorUtils.optional(eventStore.find(InstanceId.of(id))).map(r -> r.stream()).get().collect(Collectors.toList());
        return ResultData.ok(list).build();
    }

    /**
     * 所有实例的Event支持分页
     * @return
     */
    @GetMapping(path = "/instances/events")
    public ResultData events(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize) {
        List<InstanceEvent> list = ReactorUtils.optional(eventStore.findAll()).map(r -> r.stream()).get().collect(Collectors.toList());
        PageResult<InstanceEvent> pageResult= PagingUtils.pageBuider(list,pageNum,pageSize).build();
        return ResultData.ok(pageResult).build();
    }





}
