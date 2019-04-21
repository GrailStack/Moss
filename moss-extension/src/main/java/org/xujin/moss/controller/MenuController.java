package org.xujin.moss.controller;

import org.xujin.moss.common.ResultData;
import org.xujin.moss.constant.Constants;
import org.xujin.moss.model.MenuModel;
import org.xujin.moss.vo.SubMenuVO;
import org.xujin.moss.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/15 19:16
 **/
@RestController
@RequestMapping("/admin/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @GetMapping("/all")
    public ResultData getAll(){

        List<MenuModel>allMenu = menuService.getAllMenu();
        HashMap<Long,MenuModel> uniqueMenu=new HashMap<>();
        allMenu.forEach(m->{
            uniqueMenu.put(m.getId(),m);
        });
        return ResultData.builder().data(initMenu(uniqueMenu,0)).build();
    }

    private List<SubMenuVO> initMenu(HashMap<Long,MenuModel> uniqueMenu, long parent){
        List<SubMenuVO> subMenuVOS =new ArrayList<>();
        uniqueMenu.values().forEach(menu->{
            boolean isRootLevel=menu.getParentId()==parent;
            if(isRootLevel){
                subMenuVOS.add(SubMenuVO.builder()
                        .title(menu.getName())
                        .icon(menu.getIcon())
                        .url(menu.getUrl())
                        .subMenu(initMenu(uniqueMenu,menu.getId()))
                        .key(menu.getKey())
                        .id(menu.getId())
                        .parentId(menu.getParentId())
                        .name(menu.getName())
                        .parentIds(menu.getParentIds())
                        .roles(menu.getRoles())
                        .sort(menu.getSort())
                        .gmtCreate(menu.getGmtCreate())
                        .gmtModified(menu.getGmtModified())
                        .build());
            }
        });
        return subMenuVOS;
    }
    @PatchMapping("/{id}")
    public ResultData updatePartially(MenuModel menuModel){
        if(menuModel.getId()==null){
            return ResultData.builder().code(Constants.CODE_ERR).msgCode("ID should not be null").build();
        }
        return ResultData.builder().data(menuService.updateAll(menuModel)).build();
    }

    @PostMapping("/addOrUpdate")
    public ResultData addOrUpdate(@RequestBody  MenuModel menuModel){
        menuService.createNewMenuIfNotExist(menuModel);
        return ResultData.builder().build();
    }

    /**
     * 逻辑删除菜单
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResultData deleteById(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResultData.builder().build();
    }

}
