package org.xujin.moss.client.endpoint.dependency.nexus;

import java.io.InputStream;

import org.xujin.moss.client.endpoint.dependency.MavenSearch;
import org.xujin.moss.client.endpoint.dependency.MavenSearchBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NexusMavenSearch implements MavenSearch {
    private MavenSearchBuilder mavenSearchBuilder = new NexusMavenSearchBuilder();

    @Override
    public InputStream getPomInfoByFileName(String[] av, String fileName) {
        return mavenSearchBuilder.getContentByName(av, fileName);
    }

    @Override
    public InputStream getSourceJarByFileName(String fileName) {
        return mavenSearchBuilder.getContentByName(null, fileName);
    }
}
