package org.xujin.moss.client.endpoint.dependency.analyzer;

import java.io.Serializable;
import java.util.List;

public class JarDependencies implements Serializable {
    String summerframeworkVersion;
    String springCloudVersion;
    String springBootVersion;
    List<PomInfo> pomInfos;

    public List<PomInfo> getPomInfos() {
        return pomInfos;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }

    public String getSpringCloudVersion() {
        return springCloudVersion;
    }

    public String getSummerframeworkVersion() {
        return summerframeworkVersion;
    }

    public void setPomInfos(List<PomInfo> pomInfos) {
        this.pomInfos = pomInfos;
    }

    public void setSpringBootVersion(String springBootVersion) {
        this.springBootVersion = springBootVersion;
    }

    public void setSpringCloudVersion(String springCloudVersion) {
        this.springCloudVersion = springCloudVersion;
    }

    public void setSummerframeworkVersion(String summerframeworkVersion) {
        this.summerframeworkVersion = summerframeworkVersion;
    }
}
