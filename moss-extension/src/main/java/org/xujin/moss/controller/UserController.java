package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.common.domain.PageResult;
import org.xujin.moss.entity.User;
import org.xujin.moss.vo.SubMenuVO;
import org.xujin.moss.model.UserModel;
import org.xujin.moss.request.UserPageListRequest;
import org.xujin.moss.service.MenuService;
import org.xujin.moss.service.UserAppService;
import org.xujin.moss.service.UserService;
import org.xujin.moss.utils.BeanMapper;
import org.xujin.moss.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserController
 * @author xujin
 */
@RestController
@RequestMapping("/admin/user")
public class UserController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserAppService userAppService;

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录的信息
     * @return
     */
    @GetMapping("/currentLogin")
    public ResultData currentLogin(){
        Map<String,Object> ret=new HashMap<>();
        User user=new User();
        if(null!=user){
            ret.put("mailNickname",this.getUserNameByToken());
        }else {
            ret.put("mailNickname","");
        }
        return ResultData.builder().data(ret).build();
    }


    /**
     * 根据当前登录信息或者环境获取对应的菜单
     * @return
     */
    @GetMapping("/menu")
    public ResultData menu(){
        List<SubMenuVO> list=menuService.getMenuByUserRole(this.getUserNameByToken());
        return ResultData.builder().data(list).build();
    }

    /**
     * 是否收藏服务或实例
     * @param mailNickname
     * @param appId
     * @param isCollect
     * @return
     */
    @GetMapping("/isNeedCollect")
    public ResultData collected(@RequestParam(value = "mailNickname") String  mailNickname,
                                @RequestParam(value = "appId")  String appId,
                                @RequestParam(value = "isCollect") boolean isCollect ){
        if(isCollect){
            userAppService.collecteApp(mailNickname,appId.toLowerCase());
        }else {
            userAppService.cancleCollecteApp(mailNickname,appId.toLowerCase());
        }
        return ResultData.builder().build();
    }

    @GetMapping("/list")
    public ResultData getUserList(){
        List<UserVO> userList= BeanMapper.mapList(userService.getUserList(), UserModel.class,UserVO.class);
        return ResultData.builder().data(userList).build();
    }


    /**
     * 查询用户列表并分页
     * @param userPageListRequest
     * @return
     */
    @PostMapping("/pageList")
    ResultData searchUserByPage(@RequestBody UserPageListRequest userPageListRequest) {
        PageResult<UserVO> pageResult= userService.findPageByParam(userPageListRequest);
        return ResultData.builder().data(pageResult).build();
    }


    /**
     * 增加用户
     * @param userModel
     * @return
     */
    @PostMapping("/add")
    public ResultData addUser(@RequestBody UserModel userModel) {
        return userService.addUser(userModel);
    }

    /**
     * 更新一个User
     * @param userModel
     * @return
     */
    @PostMapping("/update")
    public ResultData updateUser(@RequestBody UserModel userModel) {
        userService.update(userModel);
        return ResultData.builder().build();
    }

    /**
     * 通过Id删除用户
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResultData deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResultData.builder().build();
    }

}
