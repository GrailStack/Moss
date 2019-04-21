package org.xujin.moss.client.endpoint.dependency.nexus;

class SearchResult {
    private RepoDetail[] repoDetails;
    private NexusPomInfo[] data;

    public NexusPomInfo[] getData() {
        return data;
    }

    public RepoDetail[] getRepoDetails() {
        return repoDetails;
    }

    public void setData(NexusPomInfo[] data) {
        this.data = data;
    }

    public void setRepoDetails(RepoDetail[] repoDetails) {
        this.repoDetails = repoDetails;
    }
}
