package org.xujin.moss.client.endpoint.dependency.analyzer;

import java.io.Serializable;

public class PomDependency implements Serializable {
    public String groupId;
    public String artifactId;
    public String version;
    public String scope;

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getScope() {
        return scope;
    }

    public String getVersion() {
        return version;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
