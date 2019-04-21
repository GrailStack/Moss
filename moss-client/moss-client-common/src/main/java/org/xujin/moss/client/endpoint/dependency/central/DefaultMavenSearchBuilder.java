package org.xujin.moss.client.endpoint.dependency.central;

import java.io.InputStream;

import org.xujin.moss.client.endpoint.dependency.MavenSearchBuilder;
import org.xujin.moss.client.endpoint.dependency.util.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultMavenSearchBuilder implements MavenSearchBuilder {
    @Override
    public String getPomUrl(InputStream is, String endsWith) throws Exception {
        ObjectMapper objectMapper = JsonMapper.defaultMapper().getMapper();
        SearchResult results = objectMapper.readValue(is, SearchResult.class);
        if (results.getResponse() != null && results.getResponse().getDocs() != null
            && results.getResponse().getDocs().length > 0) {
            PomDoc pomInfo = results.getResponse().getDocs()[0];
            String pomUrl = "https://search.maven.org/remotecontent?filepath=" + //
                pomInfo.getG().replace('.', '/') + "/" + //
                pomInfo.getA() + "/" + pomInfo.getV() + "/" + pomInfo.getA() + "-" + pomInfo.getV() + endsWith;
            log.debug(pomUrl);
            return pomUrl;
        }
        return null;
    }

    @Override
    public String getSearchUrl(String[] av) {
        String searchUrl = "http://search.maven.org/solrsearch/select?q=a:%22";
        if (av.length > 2) {
            searchUrl += av[0] + "%22%20AND%20v:%22" + av[1] + "%22%20AND%20g:%22" + av[2] + "%22&rows=1&wt=json";
        } else {
            searchUrl += av[0] + "%22%20AND%20v:%22" + av[1] + "%22&rows=1&wt=json";
        }
        return searchUrl;
    }
}
