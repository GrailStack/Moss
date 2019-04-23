package org.xujin.moss.utils;

import org.xujin.moss.model.MenuModel;
import org.xujin.moss.vo.SubMenuVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuUtil {

    public static List<SubMenuVO> initMenu(HashMap<Long, MenuModel> uniqueMenu, long parent){
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
}
