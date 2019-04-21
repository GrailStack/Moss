package org.xujin.moss.client.endpoint.dependency.central;

class SearchResponse {
    private PomDoc[] docs;

    public PomDoc[] getDocs() {
        return docs;
    }

    public void setDocs(PomDoc[] docs) {
        this.docs = docs;
    }
}
