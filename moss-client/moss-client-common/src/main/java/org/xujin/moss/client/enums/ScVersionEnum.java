package org.xujin.moss.client.enums;

import java.util.ArrayList;
import java.util.List;

public enum ScVersionEnum {

    SC_EDGWARE_SR3("1.3.3.RELEASE", "Edgware.SR3","Spring Cloud Edgware.SR3"),
    SC_Finchley_RELEASE("2.0.0.RELEASE","Finchley.RELEASE","Spring Cloud Finchley.RELEASE"),
    SC_FINCHLEY_SR2("2.0.2.RELEASE","Finchley.SR2","Spring Cloud Finchley.SR2"),
    SC_FINCHLEY_SR3("2.0.3.RELEASE","Finchley.SR2","Spring Cloud Finchley.SR3"),
    SC_Greenwich_RELEASE("2.1.0.RELEASE","Finchley.SR2","Spring Cloud Finchley.SR3");

    private String springCommonVersion;
    private String springcloudVersion;
    private String desc;

    ScVersionEnum(String springCommonVersion, String springcloudVersion, String desc) {
        this.springCommonVersion = springCommonVersion;
        this.springcloudVersion = springcloudVersion;
        this.desc = desc;
    }
    public  static String getScVersionByCommonVersion(String springCloudommonVersion){
        List list=new ArrayList<>();
        for (ScVersionEnum scVersionEnum : ScVersionEnum.values()) {
            if (scVersionEnum.springCommonVersion.equalsIgnoreCase(springCloudommonVersion)){
               return scVersionEnum.springcloudVersion;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getScVersionByCommonVersion("2.0.2.RELEASE"));
    }

}
