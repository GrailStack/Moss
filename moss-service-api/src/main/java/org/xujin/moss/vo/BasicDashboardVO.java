package org.xujin.moss.vo;

import lombok.Data;

@Data
public class BasicDashboardVO {

    private int appNum;
    private int projectNum;
    private int instanceNum;
    private int myAppNum;
    private int myInstanNum;
    private int downNum;
    private int MyProjectNum;

    /**
     * 角色
     */
    private String role;

    /**
     * 部门
     */
    private String department;

}
