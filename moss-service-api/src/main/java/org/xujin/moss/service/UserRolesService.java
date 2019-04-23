package org.xujin.moss.service;


import org.xujin.moss.model.UserRoleModel;

import java.util.List;

/**
 * 菜单角色绑定关系服务
 * @author xujin
 */
public interface UserRolesService {
    UserRoleModel create(UserRoleModel userRoleModel);
    int updateById(UserRoleModel userRoleModel);
    UserRoleModel findById(long id);
    int deleteById(long id);
    List<UserRoleModel> getAll();
    List<UserRoleModel> getByUsername(String username);
}
