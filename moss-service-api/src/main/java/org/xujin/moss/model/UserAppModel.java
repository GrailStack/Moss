package org.xujin.moss.model;


import org.xujin.moss.base.BaseModel;

/**
 * 用户和app之间的收藏Model
 */
public class UserAppModel  extends BaseModel {

    public String mailNickName;

    public String appId;

    public String getMailNickName() {
        return mailNickName;
    }

    public void setMailNickName(String mailNickName) {
        this.mailNickName = mailNickName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
