package org.xujin.moss.model;

import org.xujin.moss.base.BaseModel;

public class ProjectModel extends BaseModel {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 负责人名称
     */
    private String ownerName;

    /**
     * 应用负责人Id
     */
    private String ownerId;

    /**
     * 所属项目名
     */
    private String cname;

    /**
     * 所属项目Id
     */
    private String key;

    /**
     * 项目描述
     */
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
