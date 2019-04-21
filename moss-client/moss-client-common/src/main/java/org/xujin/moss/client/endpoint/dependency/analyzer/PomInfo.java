package org.xujin.moss.client.endpoint.dependency.analyzer;

import java.io.Serializable;
import java.util.List;

public class PomInfo implements Serializable {
    public String groupId;
    public String artifactId;
    public String version;
    public String location;
    public long size;
    public List<PomDependency> dependencies;

    public String getArtifactId() {
        return artifactId;
    }

    public List<PomDependency> getDependencies() {
        return dependencies;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getLocation() {
        return location;
    }

    public long getSize() {
        return size;
    }

    public String getVersion() {
        return version;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setDependencies(List<PomDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
