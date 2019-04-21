package org.xujin.moss.model;

import org.xujin.moss.base.BaseModel;

/**
 * AppModel
 * @author xujin
 */
public class AppModel extends BaseModel {
    /**
     * 应用名称
     */
    private String name;

    /**
     * AppId
     */
    private String appId;

    /**
     * 负责人名称
     */
    private String ownerName;


    /**
     * 所属项目名
     */
    private String projectName;

    /**
     * 所属项目Id
     */
    private String projectKey;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 应用状态
     */
    private int status;

    private int takeOver;

    private String springApplicationName;

    private String springBootVersion;

    private String springCloudVersion;


    private String ownerId;

    private String repoUrl;



    public String getSpringApplicationName() {
        return springApplicationName;
    }

    public void setSpringApplicationName(String springApplicationName) {
        this.springApplicationName = springApplicationName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTakeOver() {
        return takeOver;
    }

    public void setTakeOver(int takeOver) {
        this.takeOver = takeOver;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    public String getSpringCloudVersion() {
        return springCloudVersion;
    }

    public void setSpringCloudVersion(String springCloudVersion) {
        this.springCloudVersion = springCloudVersion;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
}