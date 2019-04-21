package org.xujin.moss.service;

import org.xujin.moss.model.MenuModel;
import org.xujin.moss.vo.SubMenuVO;

import java.util.List;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/15 15:01
 **/
public interface MenuService {

  List<MenuModel> getMenuByRole(String role);
  List<MenuModel> getAllMenu();

  MenuModel updateAll(MenuModel menuModel);

  void createNewMenuIfNotExist(MenuModel menuModel);

  void deleteMenu(Long id);

  List<SubMenuVO> getMenuByUserRole(String userName);


  List<MenuModel> getMenuByKey(String key);



}
