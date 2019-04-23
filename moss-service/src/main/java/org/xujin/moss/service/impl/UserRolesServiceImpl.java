package org.xujin.moss.service.impl;

import org.xujin.moss.entity.UserRoles;
import org.xujin.moss.mapper.UserRolesMapper;
import org.xujin.moss.model.UserRoleModel;
import org.xujin.moss.service.UserRolesService;
import org.xujin.moss.utils.BeanMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/16 17:57
 **/
@Service
public class UserRolesServiceImpl implements UserRolesService {

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    @Transactional
    public UserRoleModel create(UserRoleModel userRoleModel) {
        UserRoles userRole= BeanMapper.map(userRoleModel,UserRoles.class);
        userRole.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        userRole.setGmtModified(new Timestamp(System.currentTimeMillis()));
        long id = userRolesMapper.insert(userRole);
        userRole.setId(id);
        return BeanMapper.map(userRole,UserRoleModel.class);
    }

    @Override
    @Transactional
    public int updateById(UserRoleModel userRoleModel) {
        if(userRoleModel.getId()<=0){
            throw new IllegalArgumentException("Method[updateById] MUST has ID");
        }
        UserRoles userRole= BeanMapper.map(userRoleModel,UserRoles.class);
        return userRolesMapper.updateById(userRole);
    }

    @Override
    public UserRoleModel findById(long id) {
        return BeanMapper.map(userRolesMapper.selectById(id),UserRoleModel.class);
    }


    @Override
    @Transactional
    public int deleteById(long id) {
        return userRolesMapper.deleteById(id);
    }

    @Override
    public List<UserRoleModel> getAll() {
        return BeanMapper.mapList(
                userRolesMapper.selectByMap(null),
                UserRoles.class,
                UserRoleModel.class
        );
    }

    @Override
    public List<UserRoleModel> getByUsername(String username) {
        Map<String, Object> param=new HashMap<>();
        if(StringUtils.isNotEmpty(username)){
            param.put("username",username);
        }
        return  BeanMapper.mapList(userRolesMapper.selectByMap(param), UserRoles.class, UserRoleModel.class);
    }
}
