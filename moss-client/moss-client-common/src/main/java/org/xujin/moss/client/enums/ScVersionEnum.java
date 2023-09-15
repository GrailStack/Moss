package org.xujin.moss.client.enums;

import java.util.ArrayList;
import java.util.List;

public enum ScVersionEnum {

    SC_EDGWARE_SR3("1.3.3.RELEASE", "Edgware.SR3","Spring Cloud Edgware.SR3"),
    SC_Finchley_RELEASE("2.0.0.RELEASE","Finchley.RELEASE","Spring Cloud Finchley.RELEASE"),
    SC_FINCHLEY_SR2("2.0.2.RELEASE","Finchley.SR2","Spring Cloud Finchley.SR2"),
    SC_FINCHLEY_SR3("2.0.3.RELEASE","Finchley.SR2","Spring Cloud Finchley.SR3"),
    SC_Greenwich_RELEASE("2.1.0.RELEASE","Finchley.SR2","Spring Cloud Finchley.SR3"),
    SC_Dalston_SR5("1.2.5.RELEASE","Dalston.SR5","Spring Cloud Dalston.SR5"),
    SC_Dalston_SR4("1.2.4.RELEASE","Dalston.SR4","Spring Cloud Dalston.SR4"),
    SC_Dalston("1.2.3.RELEASE","Dalston","Spring Cloud Dalston.SR2"),
    SC_Dalston_SR1("1.2.2.RELEASE","Dalston.SR1","Spring Cloud Dalston.SR1"),
    SC_Edgware_SR6("1.3.6.RELEASE","Edgware.SR6","Spring Cloud Edgware.SR6"),
    SC_Edgware_SR5("1.3.5.RELEASE","Edgware.SR5","Spring Cloud Edgware.SR5"),
    SC_Edgware_SR4("1.3.4.RELEASE","Edgware.SR4","Spring Cloud Edgware.SR4"),
    SC_Edgware_SR2("1.3.2.RELEASE","Edgware.SR2","Spring Cloud Edgware.SR2"),
    SC_Edgware_SR1("1.3.1.RELEASE","Edgware.SR1","Spring Cloud Edgware.SR1"),
    SC_Edgware_RELEASE("1.3.0.RELEASE","Edgware.RELEASE","Spring Cloud Edgware.RELEASE"),
    SC_Edgware_RC1("1.3.0.RC1","Edgware.RC1","Spring Cloud Edgware.RC1"),
    SC_Edgware_M1("1.3.0.M1","Edgware.M1","Spring Cloud Edgware.M1"),
    SC_Dalston_RELEASE("1.2.0.RELEASE","Dalston.RELEASE","Spring Cloud Dalston.RELEASE"),
    SC_Finchley_SR4("2.0.4.RELEASE","Finchley.SR4","Spring Cloud Finchley.SR4"),
    SC_Finchley_SR1("2.0.1.RELEASE","Finchley.SR1","Spring Cloud Finchley.SR1"),
    SC_Finchley_RC2("2.0.0.RC2","Finchley.RC2","Spring Cloud Finchley.RC2"),
    SC_Finchley_RC1("2.0.0.RC1","Finchley.RC1","Spring Cloud Finchley.RC1"),
    SC_Finchley_M9("2.0.0.M9","Finchley.M9","Spring Cloud Finchley.M9"),
    SC_Finchley_M8("2.0.0.M8","Finchley.M8","Spring Cloud Finchley.M8"),
    SC_Finchley_M7("2.0.0.M7","Finchley.M7","Spring Cloud Finchley.M7"),
    SC_Finchley_M6("2.0.0.M6","Finchley.M6","Spring Cloud Finchley.M6"),
    SC_Finchley_M5("2.0.0.M5","Finchley.M5","Spring Cloud Finchley.M5"),
    SC_Finchley_M4("2.0.0.M4","Finchley.M4","Spring Cloud Finchley.M4"),
    SC_Finchley_M3("2.0.0.M3","Finchley.M3","Spring Cloud Finchley.M3"),
    SC_Greenwich_SR6("2.1.6.RELEASE","Greenwich.SR6","Spring Cloud Greenwich.SR6"),
    SC_Greenwich_SR5("2.1.5.RELEASE","Greenwich.SR5","Spring Cloud Greenwich.SR5"),
    SC_Greenwich_SR4("2.1.4.RELEASE","Greenwich.SR4","Spring Cloud Greenwich.SR4"),
    SC_Greenwich_SR3("2.1.3.RELEASE","Greenwich.SR3","Spring Cloud Greenwich.SR3"),
    SC_Greenwich_SR2("2.1.2.RELEASE","Greenwich.SR2","Spring Cloud Greenwich.SR2"),
    SC_Greenwich_SR1("2.1.1.RELEASE","Greenwich.SR1","Spring Cloud Greenwich.SR1"),
    SC_Greenwich_RC2("2.1.0.RC2","Greenwich.RC2","Spring Cloud Greenwich.RC2"),
    SC_Greenwich_RC1("2.1.0.RC2","Greenwich.RC1","Spring Cloud Greenwich.RC1"),
    SC_Greenwich_M3("2.1.0.M2","Greenwich.M3","Spring Cloud Greenwich.M3"),
    SC_Greenwich_M2("2.1.0.M2","Greenwich.M2","Spring Cloud Greenwich.M2"),
    SC_Hoxton_SR12("2.2.9.RELEASE","Hoxton.SR12","Spring Cloud Hoxton.SR12"),
    SC_Hoxton_SR11("2.2.8.RELEASE","Hoxton.SR11","Spring Cloud Hoxton.SR11"),
    SC_Hoxton_SR10("2.2.7.RELEASE","Hoxton.SR10","Spring Cloud Hoxton.SR10"),
    SC_Hoxton_SR9("2.2.6.RELEASE","Hoxton.SR9","Spring Cloud Hoxton.SR9"),
    SC_Hoxton_SR8("2.2.5.RELEASE","Hoxton.SR8","Spring Cloud Hoxton.SR8"),
    SC_Hoxton_SR7("2.2.4.RELEASE","Hoxton.SR7","Spring Cloud Hoxton.SR7"),
    SC_Hoxton_SR6("2.2.3.RELEASE","Hoxton.SR6","Spring Cloud Hoxton.SR6"),
    SC_Hoxton_SR5("2.2.3.RELEASE","Hoxton.SR5","Spring Cloud Hoxton.SR5"),
    SC_Hoxton("2.2.2.RELEASE","Hoxton","Spring Cloud Hoxton.SR4 OR Hoxton.SR3"),
    SC_Hoxton_SR1("2.2.1.RELEASE","Hoxton.SR1","Spring Cloud Hoxton.SR1"),
    SC_Hoxton_RELEASE("2.2.0.RELEASE","Hoxton.RELEASE","Spring Cloud Hoxton.RELEASE"),
    SC_Hoxton_RC2("2.2.0.RC2","Hoxton.RC2","Spring Cloud Hoxton.RC2"),
    SC_Hoxton_RC1("2.2.0.RC1","Hoxton.RC1","Spring Cloud Hoxton.RC1"),
    SC_Hoxton_M3("2.2.0.M3","Hoxton.M3","Spring Cloud Hoxton.M3"),
    SC_Hoxton_M2("2.2.0.M2","Hoxton.M2","Spring Cloud Hoxton.M2"),
    SC_Hoxton_M1("2.2.0.M1","Hoxton.M1","Spring Cloud Hoxton.M1"),
    SC_2020_0_6("3.0.6","2020.0.6","Spring Cloud 2020.0.6"),
    SC_2020_0_5("3.0.5","2020.0.5","Spring Cloud 2020.0.5"),
    SC_2020_0_4("3.0.4","2020.0.4","Spring Cloud 2020.0.4"),
    SC_2020_0_3("3.0.3","2020.0.3","Spring Cloud 2020.0.3"),
    SC_2020_0_2("3.0.2","2020.0.2","Spring Cloud 2020.0.2"),
    SC_2020_0_1("3.0.1","2020.0.1","Spring Cloud 2020.0.1"),
    SC_2020_0_0("3.0.0","2020.0.0","Spring Cloud 2020.0.0"),
    SC_2020_0_0_RC1("3.0.0-RC1","2020.0.0-RC1","Spring Cloud 2020.0.0-RC1"),
    SC_2020_0_0_M6("3.0.0-M6","2020.0.0-M6","Spring Cloud 2020.0.0-M6"),
    SC_2020_0_0_M5("3.0.0-M5","2020.0.0-M5","Spring Cloud 2020.0.0-M5"),
    SC_2020_0_0_M4("3.0.0-M4","2020.0.0-M4","Spring Cloud 2020.0.0-M4"),
    SC_2020_0_0_M3("3.0.0-M3","2020.0.0-M3","Spring Cloud 2020.0.0-M3"),
    SC_2020_0_0_M2("3.0.0-M2","2020.0.0-M2","Spring Cloud 2020.0.0-M2"),
    SC_2020_0_0_M1("3.0.0.M1","2020.0.0-M1","Spring Cloud 2020.0.0-M1"),
    SC_2021_0_8("3.1.7","2021.0.8","Spring Cloud 2021.0.8"),
    SC_2021_0_7("3.1.6","2021.0.7","Spring Cloud 2021.0.7"),
    SC_2021_0_5("3.1.5","2021.0.5","Spring Cloud 2021.0.5"),
    SC_2021_0_4("3.1.4","2021.0.4","Spring Cloud 2021.0.4"),
    SC_2021_0_3("3.1.3","2021.0.3","Spring Cloud 2021.0.3"),
    SC_2021_0_2("3.1.2","2021.0.2","Spring Cloud 2021.0.2"),
    SC_2021_0_1("3.1.1","2021.0.1","Spring Cloud 2021.0.1"),
    SC_2021_0_0("3.1.0","2021.0.0","Spring Cloud 2021.0.0"),
    SC_2021_0_0_RC1("3.1.0-RC1","2021.0.0-RC1","Spring Cloud 2021.0.0-RC1"),
    SC_2021_0_0_M3("3.1.0-M3","2021.0.0-M3","Spring Cloud 2021.0.0-M3"),
    SC_2021_0_0_M2("3.1.0-M2","2021.0.0-M2","Spring Cloud 2021.0.0-M2"),
    SC_2022_0_3("4.0.3","2022.0.3","Spring Cloud 2022.0.3"),
    SC_2022_0_2("4.0.2","2022.0.2","Spring Cloud 2022.0.2"),
    SC_2022_0_1("4.0.1","2022.0.1","Spring Cloud 2022.0.1"),
    SC_2022_0_0("4.0.0","2022.0.0","Spring Cloud 2022.0.0"),
    SC_2022_0_0_RC3("4.0.0-RC3","2022.0.0-RC3","Spring Cloud 2022.0.0-RC3"),
    SC_2022_0_0_RC2("4.0.0-RC2","2022.0.0-RC2","Spring Cloud 2022.0.0-RC2"),
    SC_2022_0_0_RC1("4.0.0-RC1","2022.0.0-RC1","Spring Cloud 2022.0.0-RC1"),
    SC_2022_0_0_M5("4.0.0-M5","2022.0.0-M5","Spring Cloud 2022.0.0-M5"),
    SC_2022_0_0_M4("4.0.0-M4","2022.0.0-M4","Spring Cloud 2022.0.0-M4"),
    SC_2022_0_0_M3("4.0.0-M3","2022.0.0-M3","Spring Cloud 2022.0.0-M3"),
    SC_2022_0_0_M2("4.0.0-M2","2022.0.0-M2","Spring Cloud 2022.0.0-M2"),
    SC_2022_0_0_M1("4.0.0-M1","2022.0.0-M1","Spring Cloud 2022.0.0-M1"),
    SC_2023_0_0_M1("4.1.0-M1","2023.0.0-M1","Spring Cloud 2023.0.0-M1");

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
