package org.xujin.moss.service.impl;

import org.xujin.moss.constant.Constants;
import org.xujin.moss.entity.UserApp;
import org.xujin.moss.mapper.UserAppMapper;
import org.xujin.moss.model.UserAppModel;
import org.xujin.moss.service.UserAppService;
import org.xujin.moss.utils.BeanMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAppServiceImpl implements UserAppService {

    @Autowired
    private UserAppMapper userAppMapper;

    @Override
    public List<UserAppModel> findCollectedByMailNickName(String mailNickName, String appId) {
        if(StringUtils.isEmpty(mailNickName)){
            return null;
        }
        Map<String, Object> param=new HashMap<>();
        param.put("mail_nick_name",mailNickName);
        param.put("is_deleted",(byte)0);
        if(StringUtils.isNotEmpty(appId)){
            param.put("app_id",appId);
        }
        List<UserApp> userAppList = userAppMapper.selectByMap(param);
        if(null==userAppList||userAppList.size()==0){
            return null;
        }
        return BeanMapper.mapList(userAppList,UserApp.class,UserAppModel.class);
    }

    @Override
    public Boolean collecteApp(String mailNickName, String appId) {
        Map<String, Object> param=new HashMap<>();
        param.put("mail_nick_name",mailNickName);
        param.put("app_id",appId);
        param.put("is_deleted",(byte)0);
        List<UserApp> userAppList = userAppMapper.selectByMap(param);
        if(null!=userAppList&&userAppList.size()>0){
            return false;
        }
        UserApp userApp=new UserApp();
        userApp.setAppId(appId);
        userApp.setMailNickName(mailNickName);
        userApp.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        userApp.setGmtModified(new Timestamp(System.currentTimeMillis()));
        userAppMapper.insert(userApp);
        return true;
    }

    @Override
    public Boolean cancleCollecteApp(String mailNickName, String appId) {
        if(StringUtils.isEmpty(mailNickName)||StringUtils.isEmpty(appId)){
            return false;
        }
        Map<String, Object> param=new HashMap<>();
        param.put("mail_nick_name",mailNickName);
        param.put("app_id",appId);
        param.put("is_deleted",(byte)0);
        List<UserApp> userAppList = userAppMapper.selectByMap(param);
        if(null==userAppList||userAppList.size()==0){
            return false;
        }
        UserApp userApp=userAppList.get(0);
        userApp.setGmtModified(new Timestamp(System.currentTimeMillis()));
        userApp.setIsDeleted(Constants.IS_DELETE_TRUE);
        userAppMapper.updateById(userApp);
        return true;
    }
}
