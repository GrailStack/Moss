package org.xujin.moss.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.xujin.moss.config.AdminEndpointApplicationRunListener.SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE;

@ConfigurationProperties(prefix = "endpoints.errorLogFile")
public class ErrorLogFileEndPoint extends AbstractNamedMvcEndpoint {

    public static  final String basicPath="/opt/app/logs";

    public static  final String logFile_type_info="info";

    public static  final String logFile_type_error="error";


    private static final Log logger = LogFactory.getLog(ErrorLogFileEndPoint.class);

    public ErrorLogFileEndPoint() {
        super("errorLogFile", SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE+"/errorLogFile", true);
    }

    @RequestMapping(method = { RequestMethod.GET, RequestMethod.HEAD })
    @ResponseBody
    public void invoke(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isEnabled()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        Resource resource = getLogFileResource(logFile_type_error);
        if (resource != null && !resource.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Log file '" + resource + "' does not exist");
            }
            resource = null;
        }
        Handler handler = new Handler(resource, request.getServletContext());
        handler.handleRequest(request, response);
    }

    private Resource getLogFileResource(String logType) {
        String LogFileName= getByCustomLogType(getEnvironment(),basicPath,logType);
        return new FileSystemResource(LogFileName);
    }

    public static String getByCustomLogType(PropertyResolver propertyResolver, String path, String type) {
        String fileName = propertyResolver.getProperty("spring.application.name");
        if(type.equals(logFile_type_info)){
            fileName=fileName+"_info.log";
        }else if(type.equals(logFile_type_error)){
            fileName=fileName+"_error.log";
        }
        return path+"/"+fileName;
    }

    /**
     * {@link ResourceHttpRequestHandler} to send the log file.
     */
    private static class Handler extends ResourceHttpRequestHandler {

        private final Resource resource;

        Handler(Resource resource, ServletContext servletContext) {
            this.resource = resource;
            getLocations().add(resource);
            try {
                setServletContext(servletContext);
                afterPropertiesSet();
            }
            catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        protected void initAllowedLocations() {
            this.getLocations().clear();
        }

        @Override
        protected Resource getResource(HttpServletRequest request) throws IOException {
            return this.resource;
        }

        @Override
        protected MediaType getMediaType(Resource resource) {
            return MediaType.TEXT_PLAIN;
        }

    }

}




