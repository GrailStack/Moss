package org.xujin.moss.core.extension;

import de.codecentric.boot.admin.server.services.InstanceRegistry;
import de.codecentric.boot.admin.server.web.AbstractInstancesProxyController;
import de.codecentric.boot.admin.server.web.AdminController;
import de.codecentric.boot.admin.server.web.client.InstanceWebClient;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

/**
 *
 * 扩展定义的实例代理Controller
 * Http Handler for proxied requests
 * @author xujin
 */
@AdminController
public class MossInstancesProxyController extends AbstractInstancesProxyController {

    protected static final String HALO_REQUEST_MAPPING_PATH = "/admin/instances/{instanceId}/actuator/**";

    private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();

    public MossInstancesProxyController(String adminContextPath,
                                        Set<String> ignoredHeaders,
                                        InstanceRegistry registry,
                                        InstanceWebClient instanceWebClient) {
        super(adminContextPath, ignoredHeaders, registry, instanceWebClient,HALO_REQUEST_MAPPING_PATH);
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
        ServerHttpRequest request = new ServletServerHttpRequest(servletRequest);

        String pathWithinApplication = UriComponentsBuilder.fromPath(servletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString()).toUriString();
        String endpointLocalPath = getEndpointLocalPath(pathWithinApplication);

        URI uri = UriComponentsBuilder.fromPath(endpointLocalPath)
                .query(request.getURI().getRawQuery())
                .build(true)
                .toUri();
        ClientResponse clientResponse = super.forward(instanceId,
                uri,
                request.getMethod(),
                request.getHeaders(),
                () -> BodyInserters.fromDataBuffers(DataBufferUtils.readInputStream(request::getBody,
                        this.bufferFactory,
                        4096
                ))
        ).block();

        ServerHttpResponse response = new ServletServerHttpResponse(servletResponse);
        response.setStatusCode(clientResponse.statusCode());
        response.getHeaders().addAll(filterHeaders(clientResponse.headers().asHttpHeaders()));
        OutputStream responseBody = response.getBody();
        response.flush();

        return clientResponse.body(BodyExtractors.toDataBuffers())
                .window(1)
                .concatMap(body -> writeAndFlush(body, responseBody))
                .then();
    }

    private Mono<Void> writeAndFlush(Flux<DataBuffer> body, OutputStream responseBody) {
        return DataBufferUtils.write(body, responseBody).map(DataBufferUtils::release).then(Mono.create(sink -> {
            try {
                responseBody.flush();
                sink.success();
            } catch (IOException ex) {
                sink.error(ex);
            }
        }));
    }
}
