package org.xujin.moss.client.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerEndpoint(id = "errorLogFile")
public class ErrorLogFileEndPoint {

    private static class Handler extends ResourceHttpRequestHandler {

        private final Resource resource;

        Handler(Resource resource, ServletContext servletContext) {
            this.resource = resource;
            getLocations().add(resource);
            try {
                setServletContext(servletContext);
                afterPropertiesSet();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        protected MediaType getMediaType(HttpServletRequest request, Resource resource) {
            return MediaType.TEXT_PLAIN;
        }

        @Override
        protected Resource getResource(HttpServletRequest request) throws IOException {
            return this.resource;
        }

        @Override
        protected void initAllowedLocations() {
            this.getLocations().clear();
        }
    }

    public static final String basicPath = "/opt/app/logs";

    public static final String logFile_type_info = "info";

    public static final String logFile_type_error = "error";

    private static final Log logger = LogFactory.getLog(ErrorLogFileEndPoint.class);

    public static String getByCustomLogType(PropertyResolver propertyResolver, String path, String type) {
        String fileName = propertyResolver.getProperty("spring.application.name");
        if (type.equals(logFile_type_info)) {
            fileName = fileName + "_info.log";
        } else if (type.equals(logFile_type_error)) {
            fileName = fileName + "_error.log";
        }
        return path + "/" + fileName;
    }

    @Autowired
    private Environment environment;

    public ErrorLogFileEndPoint() {
    }

    private Resource getLogFileResource(String logType) {
        String LogFileName = getByCustomLogType(environment, basicPath, logType);
        return new FileSystemResource(LogFileName);
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
    public void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Resource resource = getLogFileResource(logFile_type_error);
        if (resource != null && !resource.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Log file '" + resource + "' does not exist");
            }
            resource = null;
        }
        ErrorLogFileEndPoint.Handler handler = new ErrorLogFileEndPoint.Handler(resource, request.getServletContext());
        handler.handleRequest(request, response);
    }

}
