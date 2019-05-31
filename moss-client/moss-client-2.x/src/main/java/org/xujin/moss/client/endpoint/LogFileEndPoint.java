package org.xujin.moss.client.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@WebEndpoint(id = "logfile")
public class LogFileEndPoint {

    private LogFileRegistry registry;

    public LogFileEndPoint(Environment environment, LogFileRegistry registry) {
        this.registry = registry;
    }
    @ReadOperation
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

    @ReadOperation
    @ResponseBody
    public Resource retriveLogfile(@Selector String requiredLogFileName) throws FileNotFoundException {

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
