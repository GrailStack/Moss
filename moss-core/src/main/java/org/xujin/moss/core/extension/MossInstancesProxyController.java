package org.xujin.moss.core.extension;

import de.codecentric.boot.admin.server.services.InstanceRegistry;
import de.codecentric.boot.admin.server.web.AdminController;
import de.codecentric.boot.admin.server.web.client.InstanceWebClient;
import de.codecentric.boot.admin.server.web.servlet.InstancesProxyController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 *
 * 扩展定义的实例代理Controller
 * Http Handler for proxied requests
 * @author xujin
 */
@AdminController
public class MossInstancesProxyController extends InstancesProxyController {
    private static final String ADMIN_CONTEXT_PATH = "/admin";
    protected static final String HALO_REQUEST_MAPPING_PATH = ADMIN_CONTEXT_PATH + "/instances/{instanceId}/actuator/**";

    public MossInstancesProxyController(String adminContextPath,
                                        Set<String> ignoredHeaders,
                                        InstanceRegistry registry,
                                        InstanceWebClient instanceWebClient) {
        super(ADMIN_CONTEXT_PATH, ignoredHeaders, registry, instanceWebClient);
    }

    /**
     * 所以端点的请求代理入口:/admin/instances/{instanceId}/actuator/**
     * @author xujin
     * @param instanceId
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(path = HALO_REQUEST_MAPPING_PATH, method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public Mono<Void> endpointProxy(@PathVariable("instanceId") String instanceId,
                                    HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse) throws IOException {
       return super.endpointProxy(instanceId, servletRequest, servletResponse);
    }
}
