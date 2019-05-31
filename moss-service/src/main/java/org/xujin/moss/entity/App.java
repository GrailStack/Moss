package org.xujin.moss.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_app")
public class App extends BaseEntity{

    /**
     * 应用名称
     */
    @TableField("name")
    private String name;

    /**
     * 应用标志
     */
    @TableField("app_id")
    private String appId;

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
    @TableField("project_name")
    private String projectName;

    /**
     * 所属项目Id
     */
    @TableField("project_key")
    private String projectKey;

    /**
     * 应用描述
     */
    @TableField("description")
    private String description;


    /**
     * 应用状态
     * 0-创建 1-开发中 2-运行中 3-已下线
     */
    @TableField("status")
    private int status;

    /**
     * 是否被接管
     */
    @TableField("take_over")
    private int takeOver;

    /**
     * 运维OwnerName
     */
    @TableField("ops_owner_name")
    private String opsOwnerName;

    /**
     * 运维OwnerId
     */
    @TableField("ops_owner_id")
    private String opsOwnerId;

    /**
     * 应用对应的仓库地址
     */
    @TableField("repo_url")
    private String repoUrl;

    /**
     * buName
     */
    @TableField("bu_name")
    private String buName;

    /**
     * springApplicationName
     */
    @TableField("spring_application_name")
    private String springApplicationName;

    /**
     * Spring Boot的Version
     */
    @TableField("spring_boot_version")
    private String springBootVersion;

    /**
     *  Spring Cloud的Version
     */
    @TableField("spring_cloud_version")
    private String springCloudVersion;



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
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getTakeOver() {
        return takeOver;
    }

    public void setTakeOver(int takeOver) {
        this.takeOver = takeOver;
    }

    public String getOpsOwnerName() {
        return opsOwnerName;
    }

    public void setOpsOwnerName(String opsOwnerName) {
        this.opsOwnerName = opsOwnerName;
    }

    public String getOpsOwnerId() {
        return opsOwnerId;
    }

    public void setOpsOwnerId(String opsOwnerId) {
        this.opsOwnerId = opsOwnerId;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getSpringApplicationName() {
        return springApplicationName;
    }

    public void setSpringApplicationName(String springApplicationName) {
        this.springApplicationName = springApplicationName;
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

}
