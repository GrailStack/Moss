package org.xujin.moss.controller.extension;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.common.util.PagingUtils;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.controller.BaseController;
import org.xujin.moss.model.*;
import org.xujin.moss.service.AppService;
import org.xujin.moss.service.CommonService;
import org.xujin.moss.service.DictService;
import org.xujin.moss.service.UserAppService;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.BuildVersion;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xujin.moss.model.AppTakeoverModel;
import org.xujin.moss.model.MossApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static de.codecentric.boot.admin.server.domain.values.StatusInfo.STATUS_UNKNOWN;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;


/**
 * 应用Controller
 * @author xujin
 * @date 2019-01-08
 */
@RestController
@RequestMapping("/admin")
public class ApplicationController extends BaseController {


    @Autowired
    private DictService dictService;


    @Autowired
    private InstanceRegistry registry;


    @Autowired
    private CommonService commonService;

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private AppService appService;


    /**
     * 服务列表分页
     * @param appName app名称
     * @param pageSize 每页大小
     * @param pageNum  当前页
     * @param findType 是否我收藏的应用
     * @return
     */
    @GetMapping("/applications")
    public ResultData search(@RequestParam(value = "appName", required = false) String appName,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "8") int pageSize,
                             @RequestParam(value = "findType", required = false) String findType){

        String userName = this.getUserNameByToken();
        Flux<MossApplication> applicationFlux=applicationFlux(appName);
        List<MossApplication> applications =applicationFlux.collectList().block();
        /**
         * 查询我收藏的应用
         */
        List<MossApplication> myCollectList=new ArrayList<MossApplication>();
        if(Constants.APP_FIND_TYPE_COLLECT.equals(findType)){
            List<UserAppModel> userAppModels=userAppService.findCollectedByMailNickName(userName,"");
            /**
             * 如果查询不到我的收藏
             */
            if(null!=userAppModels&&userAppModels.size()>0){
                for (UserAppModel userAppModel:userAppModels) {
                    for (MossApplication mossApplication :applications) {
                        if(mossApplication.getName().equalsIgnoreCase(userAppModel.getAppId())){
                            mossApplication.setAttachType(Constants.ATTACH_TYPE_COLLECT);
                            myCollectList.add(mossApplication);
                        }
                    }

                }
            }
            applications=myCollectList;
        }

        if(Constants.APP_FIND_TYPE_MY.equals(findType)){
            /**
             * 查询当前登录人我的应用
             */
            List<MossApplication> myAppList=new ArrayList<MossApplication>();
            if(StringUtils.isNotEmpty(userName)){
                List<AppModel> ownerAppList= appService.findAllByParamter("","",userName);
                if(null!=ownerAppList&&ownerAppList.size()>0){
                    for (AppModel appModel:ownerAppList) {
                        for (MossApplication mossApplication :applications) {
                            if(mossApplication.getName().equalsIgnoreCase(appModel.getAppId())){
                                mossApplication.setAttachType(Constants.ATTACH_TYPE_ME);
                                myAppList.add(mossApplication);

                            }
                        }

                    }
                }
                applications=myAppList;
            }
        }


        PageResult<MossApplication> pageResult = getHaloApplicationPageResult(pageSize, pageNum, applications,findType, userName);
        return ResultData.ok(pageResult).build();
    }

    private PageResult<MossApplication> getHaloApplicationPageResult(int pageSize, int pageNum, List<MossApplication> applications, String findType, String userName) {
        PageResult<MossApplication> pageResult= PagingUtils.pageBuider(applications,pageNum,pageSize).build();
        for (MossApplication mossApplication : pageResult.getList()) {
            AppModel appModel= commonService.findAppExtendInfo(mossApplication.getName().toLowerCase());
            if(null!=appModel){
                mossApplication.setProjectKey(appModel.getProjectKey());
                mossApplication.setProjectName(appModel.getProjectName());
                mossApplication.setOwnerName(appModel.getOwnerName());
                if(Constants.TAKE_OVER_TRUE==appModel.getTakeOver()){
                    mossApplication.setTakeOver(true);
                }
            }
            if(StringUtils.isEmpty(findType)){
                /**
                 * 当findType为null标识查询所有的,并对是否收藏过打标
                 */
                List<UserAppModel> userAppModels=userAppService.findCollectedByMailNickName(userName, mossApplication.getName().toLowerCase());
                if(null!=userAppModels&&userAppModels.size()>0){
                    mossApplication.setAttachType(Constants.ATTACH_TYPE_COLLECT);
                }else{
                    mossApplication.setAttachType(Constants.ATTACH_TYPE_OTHER);
                }
            }

            List<MetaDataModel> appFlickerRule=dictService.findDictDataByDictCodeAndStatus(Constants.DICT_DATA_STATUS_TRUE,Constants.APP_FLICKER_RULE);
            if(null!=appFlickerRule&&appFlickerRule.size()>0){
                MetaDataModel metaDataModel=appFlickerRule.get(0);
                //实例数>配置实例数闪烁告警
               if(mossApplication.getInstanceNum()<=Integer.parseInt(metaDataModel.getValue())) {
                   mossApplication.setTwinkle(true);
               }
            }
            List<MetaDataModel> scoringRules=dictService.findDictDataByDictCodeAndStatus(Constants.DICT_DATA_STATUS_TRUE,Constants.SCORING_RULES);
            if(null!=scoringRules&&scoringRules.size()>0){
                MetaDataModel metaDataModel=appFlickerRule.get(0);
                if(mossApplication.getInstanceNum()>Integer.parseInt(metaDataModel.getValue())) {
                    //星数等于实例数-规则数
                    mossApplication.setStarsNum(mossApplication.getInstanceNum()-Integer.parseInt(metaDataModel.getValue()));
                }
            }
        }
        return pageResult;
    }

    /**
     * 应用详情
     * @param name 查询的名字，支持模糊
     * @return
     */
    @GetMapping("/applications/{name}")
    public ResultData application(@PathVariable("name")String name){
        Mono<ResultData> mono = this.toApplication(name, registry.getInstances(name).filter(Instance::isRegistered))
                .filter(a -> !a.getInstances().isEmpty())
                .map(r-> ResultData.ok(r).build()).defaultIfEmpty(ResultData.error(400).build());
        return mono.block();
    }


    @Nullable
    protected BuildVersion getBuildVersion(List<Instance> instances) {
        List<BuildVersion> versions = instances.stream()
                .map(Instance::getBuildVersion)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(toList());
        if (versions.isEmpty()) {
            return null;
        } else if (versions.size() == 1) {
            return versions.get(0);
        } else {
            return BuildVersion.valueOf(versions.get(0) + " ... " + versions.get(versions.size() - 1));
        }
    }

    protected Tuple2<String, Instant> getStatus(List<Instance> instances) {
        Map<String, Instant> statusWithTime = instances.stream()
                .collect(toMap(instance -> instance.getStatusInfo().getStatus(),
                        Instance::getStatusTimestamp,
                        this::getMax
                ));
        if (statusWithTime.size() == 1) {
            Map.Entry<String, Instant> e = statusWithTime.entrySet().iterator().next();
            return Tuples.of(e.getKey(), e.getValue());
        }

        if (statusWithTime.containsKey(StatusInfo.STATUS_UP)) {
            Instant oldestNonUp = statusWithTime.entrySet()
                    .stream()
                    .filter(e -> !StatusInfo.STATUS_UP.equals(e.getKey()))
                    .map(Map.Entry::getValue)
                    .min(naturalOrder())
                    .orElse(Instant.EPOCH);
            Instant latest = getMax(oldestNonUp, statusWithTime.getOrDefault(StatusInfo.STATUS_UP, Instant.EPOCH));
            return Tuples.of(StatusInfo.STATUS_RESTRICTED, latest);
        }
        return statusWithTime.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey(StatusInfo.severity()))
                .map(e -> Tuples.of(e.getKey(), e.getValue()))
                .orElse(Tuples.of(STATUS_UNKNOWN, Instant.EPOCH));
    }

    private Instant getMax(Instant t1, Instant t2) {
        return t1.compareTo(t2) >= 0 ? t1 : t2;
    }

    /**
     * 得到应用的flux 实例
     * @return
     */
    private Flux<MossApplication> applicationFlux(String appName){
        Flux<Instance> instanceFlux=null;
        /**
         * 当应用名不为空进行过滤分页
         */
        if(StringUtils.isNotEmpty(appName)){
            //因为注册到Eureka上的服务为大写
            instanceFlux=registry.getInstances(appName);
        }else {
            instanceFlux=registry.getInstances();
        }
        String registerSource = this.getRegisterSource();
        if(StringUtils.isNotEmpty(registerSource)){
            instanceFlux=instanceFlux.filter(instance->registerSource.equalsIgnoreCase(instance.getRegistration().getSource()));
        }
        return instanceFlux.filter(Instance::isRegistered)
                .groupBy(instance -> instance.getRegistration().getName())
                .flatMap(grouped -> toApplication(grouped.key(), grouped));
    }

    protected Mono<MossApplication> toApplication(String name, Flux<Instance> instances) {
        return instances.collectList().map(instanceList -> {
            MossApplication group = new MossApplication(name);
            group.setInstances(instanceList);
            group.setRegisterSource(instanceList.get(0).getRegistration().getSource());
            group.setInstanceNum(instanceList.size());
            group.setBuildVersion(getBuildVersion(instanceList));
            Tuple2<String, Instant> status = getStatus(instanceList);
            group.setStatus(status.getT1());
            group.setStatusTimestamp(status.getT2());
            return group;
        });
    }

    /**
     * 自动checkApp进行映射名称
     * @return
     */
    @GetMapping("/checkApp")
    public ResultData search() {
        AppTakeoverModel appTakeoverModel=new AppTakeoverModel();
        Flux<MossApplication> applicationFlux = applicationFlux("");
        List<String> takeoverAppList=new ArrayList<String>();
        List<String> noTakeoverAppList=new ArrayList<String>();
        List<MossApplication> applications =applicationFlux.collectList().block();
        for (MossApplication mossApplication :applications) {
            if(appService.checkAppAndMappingName(mossApplication.getName())) {
                takeoverAppList.add(mossApplication.getName());
            }else{
                noTakeoverAppList.add(mossApplication.getName());
            }
        }
        appTakeoverModel.setNoTakeoverAppList(noTakeoverAppList);
        appTakeoverModel.setTakeoverAppList(takeoverAppList);
        return ResultData.builder().data(appTakeoverModel).build();

    }

}
