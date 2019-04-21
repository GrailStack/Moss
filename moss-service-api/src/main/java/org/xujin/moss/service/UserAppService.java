package org.xujin.moss.service;


import org.xujin.moss.model.UserAppModel;

import java.util.List;

public interface UserAppService {

    List<UserAppModel> findCollectedByMailNickName(String mailNickName, String appId);


    Boolean collecteApp(String mailNickName, String appId);


    Boolean cancleCollecteApp(String mailNickName, String appId);

}
