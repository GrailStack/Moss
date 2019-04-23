package org.xujin.moss.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/10/17 16:06
 **/
@Data
@Builder
public class SubMenuVO {
    @Tolerate
    public SubMenuVO(){}
    private String icon;
    private String title;
    private String src;
    private String key;
    private List<SubMenuVO> subMenu;
    private String url;

    private Long    parentId;
    private String  name;
    private String  parentIds;
    private String  roles;
    private Integer sort;

    private Long id;

    private Timestamp gmtCreate;

    private Timestamp gmtModified;

}
