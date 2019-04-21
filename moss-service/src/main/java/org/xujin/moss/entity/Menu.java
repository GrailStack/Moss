package org.xujin.moss.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 菜单实体
 * @author xujin
 */
@TableName("t_menu")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = -162779310;

    @TableField("parent_id")
    private Long    parentId;

    @TableField("name")
    private String  name;

    @TableField("parent_ids")
    private String  parentIds;

    @TableField("url")
    private String  url;

    @TableField("roles")
    private String  roles;

    @TableField("sort")
    private Integer sort;

    @TableField("icon")
    private String  icon;

    @TableField("`key`")
    private String key;

    public Menu() {}

    public Menu(Menu value) {
        this.parentId = value.parentId;
        this.name = value.name;
        this.parentIds = value.parentIds;
        this.url = value.url;
        this.roles = value.roles;
        this.sort = value.sort;
        this.icon = value.icon;
        this.key=value.key;
    }

    public Menu(
            Long    id,
            Long    parentId,
            String  name,
            String  parentIds,
            String  url,
            String  roles,
            Integer sort,
            String  icon,
            String key
    ) {
        this.parentId = parentId;
        this.name = name;
        this.parentIds = parentIds;
        this.url = url;
        this.roles = roles;
        this.sort = sort;
        this.icon = icon;
        this.key=key;
    }


    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentIds() {
        return this.parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return this.icon;
    }


    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "parentId=" + parentId +
                ", name='" + name + '\'' +
                ", parentIds='" + parentIds + '\'' +
                ", url='" + url + '\'' +
                ", roles='" + roles + '\'' +
                ", sort=" + sort +
                ", icon='" + icon + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
