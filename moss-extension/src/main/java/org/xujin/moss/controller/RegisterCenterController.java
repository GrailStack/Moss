package org.xujin.moss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.model.RegisterCenterModel;
import org.xujin.moss.request.RegisterCenterPageRequest;
import org.xujin.moss.service.RegisterCenterService;

@RestController
@RequestMapping("/admin/registerCenter")
public class RegisterCenterController {


    @Autowired
    private RegisterCenterService registerCenterService;

    /**
     * 查询项目列表并分页
     * @param model
     * @return
     */
    @PostMapping("/list")
    ResultData searchRegisterCenterByPage(@RequestBody RegisterCenterPageRequest model) {
        PageResult<RegisterCenterModel> pageResult= registerCenterService.findPageByParam(model);
        return ResultData.builder().data(pageResult).build();
    }

    /**
     * 增加一个注册中心
     * @param registerCenterModel
     * @return
     */
    @PostMapping("/add")
    public ResultData addRegisterCenter(@RequestBody RegisterCenterModel registerCenterModel) {
        ResultData resultData=registerCenterService.addRegisterCenter(registerCenterModel);
        return resultData ;
    }

    /**
     * 更新一个注册中心
     * @param registerCenterModel
     * @return
     */
    @PostMapping("/update")
    public ResultData updateRegisterCenter(@RequestBody RegisterCenterModel registerCenterModel) {
        registerCenterService.update(registerCenterModel);
        return ResultData.builder().build();
    }

    /**
     * 删除一个注册中心
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResultData deleteRegisterCenter(@PathVariable Long id) {
        registerCenterService.deleteRegisterCenterById(id);
        return ResultData.builder().build();
    }
}

