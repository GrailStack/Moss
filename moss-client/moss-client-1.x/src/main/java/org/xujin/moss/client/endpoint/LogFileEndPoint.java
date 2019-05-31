package org.xujin.moss.client.endpoint;
import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

public class LogFileEndPoint extends AbstractNamedMvcEndpoint {

    private LogFileRegistry registry;

    public LogFileEndPoint(Environment environment, LogFileRegistry registry) {
        super("logfile", "/logfile", true);
        this.registry = registry;
    }
    @GetMapping
    @ResponseBody
    public ListNamesResponse listNames() {
        return new ListNamesResponse(collectNames(this.registry));
    }
    private Set<String> collectNames(LogFileRegistry registry) {
        return registry.getFiles()
                .stream()
                .map(logFile -> logFile.getName())
                .collect(Collectors.toSet());
    }

    @GetMapping(value = "/{requiredLogFileName:.*}", produces = "text/plain")
    @ResponseBody
    public Resource retriveLogfile(@PathVariable String requiredLogFileName) throws FileNotFoundException {

        return this.registry.getFile(requiredLogFileName);
    }

    public static final class ListNamesResponse {

        private final Set<String> names;

        ListNamesResponse(Set<String> names) {
            this.names = names;
        }

        public Set<String> getNames() {
            return this.names;
        }

    }
}
