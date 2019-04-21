package org.xujin.moss.client.endpoint.dependency.nexus;

public class ResolveData {
    private String groupId;
    private String artifactId;
    private String version;
    private String baseVersion;

    public String getArtifactId() {
        return artifactId;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
