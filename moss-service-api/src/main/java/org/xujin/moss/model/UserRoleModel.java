package org.xujin.moss.model;

import org.xujin.moss.base.BaseModel;
import lombok.Data;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/15 19:09
 **/
@Data
public class UserRoleModel extends BaseModel {
    private String  username;
    private String  role;
}
