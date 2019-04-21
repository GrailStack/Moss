package org.xujin.moss.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_user_app")
public class UserApp extends BaseEntity {

    @TableField("mail_nick_name")
    public String mailNickName;

    @TableField("app_id")
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
