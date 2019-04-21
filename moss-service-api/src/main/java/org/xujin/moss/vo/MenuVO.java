package org.xujin.moss.vo;

import org.xujin.moss.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author xujin
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class MenuVO extends BaseModel {
    private Long    parentId;
    private String  name;
    private String  parentIds;
    private String  url;
    private String  roles;
    private Integer sort;
    private String  icon;
    private String  key;

    List<MenuVO> subMenuModels;

}
