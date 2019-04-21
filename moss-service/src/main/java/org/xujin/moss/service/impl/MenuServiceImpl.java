package org.xujin.moss.service.impl;

import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.Menu;
import org.xujin.moss.mapper.MenuMapper;
import org.xujin.moss.model.MenuModel;
import org.xujin.moss.vo.SubMenuVO;
import org.xujin.moss.model.UserRoleModel;
import org.xujin.moss.service.MenuService;
import org.xujin.moss.service.UserRolesService;
import org.xujin.moss.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/15 15:01
 **/
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private Environment environment;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRolesService userRolesService;


    @Override
    public List<MenuModel> getMenuByRole(String role) {
        List<MenuModel> menus = new ArrayList<>();
        List<Menu> allMenus = menuMapper.getMenuList();
        allMenus.stream().filter(menu -> {
            String roles = menu.getRoles();
            if (StringUtils.isEmpty(roles)) {
                return true;
            } else {
                HashSet<String> roleSet = new HashSet(Arrays.asList(roles.split(",")));
                return roleSet.contains(role);
            }
        }).forEach(menu -> {
            menus.add(BeanMapper.map(menu, MenuModel.class));
        });
        return menus;
    }

    @Override
    public List<MenuModel> getAllMenu() {
        Map<String, Object> param = new HashMap<>();
        param.put("is_deleted", Constants.IS_DELETE_FALSE);
        List<Menu> allMenus = menuMapper.selectByMap(param);
        return BeanMapper.mapList(allMenus, Menu.class, MenuModel.class);
    }

    @Override
    public MenuModel updateAll(MenuModel menuModel) {
        menuMapper.updateById(BeanMapper.map(menuModel, Menu.class));
        return BeanMapper.map(menuMapper.selectById(menuModel.getId()), MenuModel.class);
    }


    @Transactional
    @Override
    public void createNewMenuIfNotExist(MenuModel menuModel) {
        if (null == menuModel) {
            return;
        }
        Menu existMenu = menuMapper.selectById(menuModel.getId());
        if (existMenu == null) {
            Menu addMenu = BeanMapper.map(menuModel, Menu.class);
            addMenu.setGmtCreate(new Timestamp(System.currentTimeMillis()));
            addMenu.setGmtModified(new Timestamp(System.currentTimeMillis()));
            addMenu.setParentIds("0");
            menuMapper.insert(addMenu);
        } else {
            Menu menu = BeanMapper.map(menuModel, Menu.class);
            menu.setGmtModified(new Timestamp(System.currentTimeMillis()));
            menu.setParentIds("0");
            menuMapper.updateById(menu);
        }
    }

    @Transactional
    @Override
    public void deleteMenu(Long id) {
        Menu menu = menuMapper.selectById(id);
        menu.setIsDeleted(Constants.IS_DELETE_TRUE);
        menu.setGmtModified(new Timestamp(System.currentTimeMillis()));
        menuMapper.updateById(menu);
    }


    @Transactional
    public List<SubMenuVO> getMenuByUserRole(String userName) {
        //为登录的用户赋予普通用户角色
        List<UserRoleModel> userRoleModels = userRolesService.getByUsername(userName);
        if (null == userRoleModels || userRoleModels.isEmpty()) {
            UserRoleModel userRoleModel = new UserRoleModel();
            userRoleModel.setRole("USER");
            userRoleModel.setUsername(userName);
            userRolesService.create(userRoleModel);
        }
        List<MenuModel> allMenu = Optional.of(userRolesService.getByUsername(userName)).orElseGet(ArrayList::new).stream()
                .map(u -> getMenuByRole(u.getRole())).reduce(new ArrayList<MenuModel>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });

        return findTree(allMenu);
    }

    /**
     * 通过key查询获取菜单
     * @param key
     * @return
     */
    @Override
    public List<MenuModel> getMenuByKey(String key) {
        Map<String, Object> param = new HashMap<>();
        param.put("`key`", key);
        List<Menu> allMenus = menuMapper.selectByMap(param);
        return BeanMapper.mapList(allMenus, Menu.class, MenuModel.class);
    }

    public List<SubMenuVO> findTree(List<MenuModel> list) {
        try {//查询所有菜单
            List<SubMenuVO> allMenu = BeanMapper.mapList(list,MenuModel.class,SubMenuVO.class);
            //根节点
            List<SubMenuVO> rootMenu = new ArrayList<SubMenuVO>();
            for (SubMenuVO nav : allMenu) {
                nav.setTitle(nav.getName());
                nav.setSrc(nav.getUrl());
                if(String.valueOf(nav.getParentId()).equals("0")){//父节点是0的，为根节点。
                    rootMenu.add(nav);
                }
            }
            /* 根据Menu类的order排序 */
            Collections.sort(rootMenu, order());
            //为根菜单设置子菜单，getClild是递归调用的
            for (SubMenuVO nav : rootMenu) {
                /* 获取根节点下的所有子节点 使用getChild方法*/
                List<SubMenuVO> childList = getChild(nav.getId(), allMenu);
                nav.setSubMenu(childList);//给根节点设置子节点
            }
            /**
             * 输出构建好的菜单数据。
             *
             */
            return rootMenu;
        } catch (Exception e) {
            return null;
        }
    }

    public Comparator<SubMenuVO> order(){
        Comparator<SubMenuVO> comparator = new Comparator<SubMenuVO>() {
            @Override
            public int compare(SubMenuVO o1, SubMenuVO o2) {
                if(o1.getSort() != o2.getSort()){
                    return o2.getSort()-o1.getSort();
                }
                return 0;
            }
        };
        return comparator;
    }

    /**
     * 获取子节点
     * @param id 父节点id
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<SubMenuVO> getChild(Long id, List<SubMenuVO> allMenu){
        //子菜单
        List<SubMenuVO> childList = new ArrayList<SubMenuVO>();
        for (SubMenuVO nav : allMenu) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getParentId().equals(id)){
                childList.add(nav);
            }
        }
        //递归
        for (SubMenuVO nav : childList) {
            nav.setSubMenu(getChild(nav.getId(), allMenu));
        }
        Collections.sort(childList,order());//排序
        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return new ArrayList<SubMenuVO>();
        }
        return childList;
    }



}
