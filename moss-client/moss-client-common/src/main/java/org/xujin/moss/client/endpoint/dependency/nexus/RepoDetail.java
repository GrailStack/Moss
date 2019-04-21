package org.xujin.moss.client.endpoint.dependency.nexus;

class RepoDetail {
    private String repositoryId;
    private String repositoryURL;
    private String repositoryKind;

    public String getRepositoryId() {
        return repositoryId;
    }

    public String getRepositoryKind() {
        return repositoryKind;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void setRepositoryKind(String repositoryKind) {
        this.repositoryKind = repositoryKind;
    }

    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }
}
