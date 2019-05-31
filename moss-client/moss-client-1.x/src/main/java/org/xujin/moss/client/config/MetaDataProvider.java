package org.xujin.moss.client.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.xujin.moss.client.endpoint.dependency.util.ProcessIdUtil;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.util.Set;

public class MetaDataProvider implements ApplicationContextAware {
    @Autowired
    ServerProperties serverProperties;
    @Autowired
    ManagementServerProperties managementServerProperties;

    private Boolean isEmbedded;
    private Integer tomcatPort;
    private String pid;

    public int getManagementPort() {
        Integer port = managementServerProperties.getPort();
        return  isEmbedded
                ? port == null || port <= 0 || port > 65534 ? getServerPort(): port
                : getServerPort();
    }
    public int getServerPort() {
        return isEmbedded
                ? serverProperties.getPort()
                : tomcatPort;
    }
    public synchronized String getProcessId() {
        if(StringUtils.isEmpty(pid)) {
            pid = ProcessIdUtil.getProcessId();
        }
        return pid;
    }
    private void initServerPort() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames;
        try {
            objectNames = mBeanServer.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        } catch (MalformedObjectNameException e) {
            return;
        }
        tomcatPort = Integer.valueOf(objectNames.iterator().next().getKeyProperty("port"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        isEmbedded = AdminEndpointApplicationRunListener.isEmbeddedServletServer(applicationContext.getEnvironment());
        if(!isEmbedded) {
            initServerPort();
        }
    }
}
