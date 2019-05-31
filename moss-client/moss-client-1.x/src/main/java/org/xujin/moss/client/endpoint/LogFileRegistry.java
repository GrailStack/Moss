package org.xujin.moss.client.endpoint;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@ConfigurationProperties("logging.registry")
public class LogFileRegistry implements InitializingBean {
    private List<LogFile> files = new ArrayList<>();
    private HashMap<String/* name */, String/* path */> fileMap;

    public List<LogFile> getFiles() {
        return files;
    }

    public void setFiles(List<LogFile> files) {
        this.files = files;
    }

    public Resource getFile(String name) throws FileNotFoundException {
        String path = fileMap.get(name);
        if (StringUtils.isEmpty(path)) {
            throw new FileNotFoundException(name);
        }
        return new FileSystemResource(path);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileMap = new HashMap<>();
        for (LogFile file : files) {
            fileMap.put(file.name, file.path);
        }
    }

    @Data
    public static class LogFile {
        private String name;
        private String description;
        private String path;
    }
}
