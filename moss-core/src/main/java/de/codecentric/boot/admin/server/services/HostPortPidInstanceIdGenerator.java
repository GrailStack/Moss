package de.codecentric.boot.admin.server.services;

import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.domain.values.Registration;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
@Slf4j
public class HostPortPidInstanceIdGenerator implements InstanceIdGenerator {
    private static final char _ = '_';
    private static final char point = '.';
    private HashingInstanceUrlIdGenerator hashingInstanceUrlIdGenerator
            = new HashingInstanceUrlIdGenerator();
    @Override
    public InstanceId generateId(Registration registration) {
        URL serviceUrl;
        try {
            serviceUrl = new URL(registration.getServiceUrl());
        } catch (MalformedURLException e) {
            log.error("Malformed service url {}, fallback to hashing", registration.getServiceUrl());
            return hashingInstanceUrlIdGenerator.generateId(registration);
        }
        StringBuilder stringBuilder =
                new StringBuilder(registration.getName())
                .append(_).append(serviceUrl.getProtocol())
                .append(_).append(serviceUrl.getHost().replace(point, _))
                .append(_).append(serviceUrl.getPort())

                ;
        String encode;
        String id = stringBuilder.toString();
        try {
            encode = URLEncoder.encode(id, "utf-8");
            return InstanceId.of(encode);
        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode id of {}, fallback to hashing", id);
            return hashingInstanceUrlIdGenerator.generateId(registration);
        }

    }
}
