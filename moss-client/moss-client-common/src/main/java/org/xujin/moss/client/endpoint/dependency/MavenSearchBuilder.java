package org.xujin.moss.client.endpoint.dependency;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.xujin.moss.client.endpoint.dependency.util.PomUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface MavenSearchBuilder {
    Logger logger = LoggerFactory.getLogger(MavenSearchBuilder.class);

    default InputStream getContentByName(String[] av, String fileName) {
        InputStream inputStream = null;
        String endsWith = ".pom";

        if ((av == null || av.length == 0) && StringUtils.isNotEmpty(fileName)) {
            endsWith = "-sources.jar";
            av = PomUtil.getArtifactIdAndVersion(fileName);
        }

        if (av == null || av.length == 0) {
            return null;
        }

        String searchUrl = getSearchUrl(av);
        logger.debug("search-url: " + searchUrl);

        try (InputStream is = httpCall(searchUrl)) {
            String pomUrl = getPomUrl(is, endsWith);
            logger.debug(pomUrl);
            if (pomUrl.startsWith("https://")) {
                HttpsURLConnection pomConn = (HttpsURLConnection)new URL(pomUrl).openConnection();
                pomConn.setSSLSocketFactory(MySSLSocketFactory.getSSLSocketFactory());
                pomConn.setRequestMethod("GET");
                inputStream = pomConn.getInputStream();
            } else if (pomUrl.startsWith("http://")) {
                HttpURLConnection pomConn = (HttpURLConnection)new URL(pomUrl).openConnection();
                pomConn.setRequestMethod("GET");
                inputStream = pomConn.getInputStream();
            }
        } catch (Exception e) {
            logger.error("get pom info by jar name[" + av[0] + " " + av[1] + "] failed", e);
        }
        return inputStream;
    }

    String getPomUrl(InputStream is, String endsWith) throws Exception;

    String getSearchUrl(String[] gav);

    default InputStream httpCall(String httpUrl) {
        logger.debug(httpUrl);
        InputStream inputStream = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            inputStream = conn.getInputStream();
        } catch (Exception e) {
            logger.error("http call has exception: ", e);
        }
        return inputStream;
    }
}
