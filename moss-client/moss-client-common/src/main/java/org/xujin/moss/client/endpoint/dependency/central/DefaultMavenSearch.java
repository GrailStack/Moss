package org.xujin.moss.client.endpoint.dependency.central;

import java.io.InputStream;

import org.xujin.moss.client.endpoint.dependency.MavenSearch;
import org.xujin.moss.client.endpoint.dependency.MavenSearchBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultMavenSearch implements MavenSearch {

    MavenSearchBuilder mavenSearchBuilder = new DefaultMavenSearchBuilder();

    @Override
    public InputStream getPomInfoByFileName(String[] av, String fileName) {
        return mavenSearchBuilder.getContentByName(av, fileName);
    }

    @Override
    public InputStream getSourceJarByFileName(String fileName) {
        return mavenSearchBuilder.getContentByName(null, fileName);
    }
}
