package org.xujin.moss.client.endpoint.dependency.nexus;

import org.xujin.moss.client.endpoint.dependency.MavenSearchBuilder;
import org.xujin.moss.client.endpoint.dependency.util.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class NexusMavenSearchBuilder implements MavenSearchBuilder {
    @Override
    public String getPomUrl(InputStream is, String endsWith) throws Exception {
        ObjectMapper objectMapper = JsonMapper.defaultMapper().getMapper();
        SearchResult results = objectMapper.readValue(is, SearchResult.class);

        if (results.getRepoDetails() != null && results.getData() != null //
            && results.getRepoDetails().length > 0 && results.getData().length > 0) {
            NexusPomInfo pomInfo = results.getData()[0];
            String repositoryUrl = null;
            String repositoryId = null;
            if (results.getRepoDetails().length > 1) {
                for (RepoDetail repoDetail : results.getRepoDetails()) {
                    if ("hosted".equalsIgnoreCase(repoDetail.getRepositoryKind())) {
                        repositoryUrl = repoDetail.getRepositoryURL();
                        break;
                    }
                }
            }

            if (repositoryUrl == null) {
                repositoryUrl = results.getRepoDetails()[0].getRepositoryURL();
                repositoryId = results.getRepoDetails()[0].getRepositoryId();
            }

            // repositoryUrl not null
            if (repositoryUrl != null) {
                if (pomInfo.getVersion().contains("-SNAPSHOT")) {
                    String resolveUrl = "http://nexus.xxx.com/nexus/service/local/artifact/maven/resolve?r=" //
                        + repositoryId + "&g=" + pomInfo.getGroupId() + "&a=" + pomInfo.getArtifactId() //
                        + "&v=" + pomInfo.getVersion() + "&isLocal=true";
                    logger.debug(resolveUrl);
                    InputStream inputStream = httpCall(resolveUrl);
                    ResolveResult resolveResult = objectMapper.readValue(inputStream, ResolveResult.class);
                    if (null != resolveResult && resolveResult.getData() != null) {
                        String snapshotVersion = resolveResult.getData().getVersion();
                        String pomUrl = repositoryUrl + "/content/" + pomInfo.getGroupId().replace(".", "/") + "/" //
                            + pomInfo.getArtifactId() + "/" + pomInfo.getVersion() + "/" //
                            + pomInfo.getArtifactId() + "-" + snapshotVersion + endsWith;
                        logger.debug(pomUrl);
                        return pomUrl;
                    }

                } else {
                    String pomUrl = repositoryUrl + "/content/" + pomInfo.getGroupId().replace(".", "/") + "/" //
                        + pomInfo.getArtifactId() + "/" + pomInfo.getVersion() + "/" //
                        + pomInfo.getArtifactId() + "-" + pomInfo.getVersion() + endsWith;
                    logger.debug(pomUrl);

                    return pomUrl;
                }
            }
        }
        return null;
    }

    @Override
    public String getSearchUrl(String[] av) {
        String searchUrl = "http://nexus.xxxx.com/nexus/service/local/lucene/search?a=" + av[0] + "&v=" + av[1];
        if (av.length > 2) {
            searchUrl += "&g=" + av[2];
        }
        return searchUrl;
    }
}
