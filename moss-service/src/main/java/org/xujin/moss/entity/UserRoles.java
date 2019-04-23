package org.xujin.moss.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.NoArgsConstructor;

/**
 * 用户角色表
 */
@TableName("t_user_roles")
@NoArgsConstructor
public class UserRoles extends BaseEntity {


    @TableField("username")
    private String  username;

    @TableField("role")
    private String  role;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
