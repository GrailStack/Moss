package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.model.UserRoleModel;
import org.xujin.moss.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserRoleController
 * @author xujin
 */
@RestController
@RequestMapping("/admin/userRole")
public class UserRoleController extends BaseController {

    @Autowired
    private UserRolesService userRolesService;

    /**
     * 增加用户
     * @param userRoleModel
     * @return
     */
    @PostMapping("/add")
    public ResultData addUser(@RequestBody UserRoleModel userRoleModel) {
        userRolesService.create(userRoleModel);
        return ResultData.builder().build();
    }

    @GetMapping("/getUserRoleByUserName")
    public ResultData getUserRoleByUserName(@RequestParam(name="userName",
            required = false,defaultValue = "") String userName) {
        List<UserRoleModel> list=userRolesService.getByUsername(userName);
        return ResultData.builder().data(list).build();
    }

    /**
     * 更新某色的角色
     * @param userRoleModel
     * @return
     */
    @PostMapping("/update")
    public ResultData updateUserRole(@RequestBody UserRoleModel userRoleModel) {
        userRolesService.updateById(userRoleModel);
        return ResultData.builder().build();
    }





}
