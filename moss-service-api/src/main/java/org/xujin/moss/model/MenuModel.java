package org.xujin.moss.model;

import org.xujin.moss.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author xujin
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class MenuModel extends BaseModel {
    private Long    parentId;
    private String  name;
    private String  parentIds;
    private String  url;
    private String  roles;
    private Integer sort;
    private String  icon;
    private String  key;

}
