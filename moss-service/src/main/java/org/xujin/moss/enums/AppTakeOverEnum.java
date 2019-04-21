package org.xujin.moss.enums;

/**
 * 应用是否被纳管
 * @author xujin
 */
public enum AppTakeOverEnum {

    TAKE_OVER_TRUE("takeOver",1,"已接入"),
    TAKE_OVER_FALSE("noTakeOver",0,"未接入");

    AppTakeOverEnum(String name, int value, String desc) {
        this.name = name;
        this.value = value;
        this.desc = desc;
    }
    private String name;

    private int value;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
