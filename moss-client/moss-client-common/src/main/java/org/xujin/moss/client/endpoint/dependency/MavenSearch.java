package org.xujin.moss.client.endpoint.dependency;

import java.io.InputStream;

public interface MavenSearch {

    InputStream getPomInfoByFileName(String[] av, String fileName);

    InputStream getSourceJarByFileName(String fileName);
}
