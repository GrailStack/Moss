package org.xujin.moss.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_project")
public class Project extends  BaseEntity {

    /**
     * 项目名称
     */
    @TableField("name")
    private String name;

    /**
     * 负责人名称
     */
    @TableField("owner_name")
    private String ownerName;

    /**
     * 应用负责人Id
     */
    @TableField("owner_id")
    private String ownerId;

    /**
     * 所属项目名
     */
    @TableField("cname")
    private String cname;

    /**
     * 所属项目Id
     */
    @TableField("`key`")
    private String key;;

    /**
     * 项目描述
     */
    @TableField("description")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
