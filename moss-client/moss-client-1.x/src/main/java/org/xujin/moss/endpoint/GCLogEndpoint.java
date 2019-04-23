package org.xujin.moss.endpoint;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.mvc.AbstractNamedMvcEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.xujin.moss.config.AdminEndpointApplicationRunListener.SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE;

/**
 * @Program: moss
 * @Description:
 * @Author: xujin
 * @Create: 2019/2/19 18:16
 **/
@Slf4j
@ConfigurationProperties(prefix = "endpoints.gc")
public class GCLogEndpoint  extends AbstractNamedMvcEndpoint {

    public GCLogEndpoint() {
        super("gc", SPRINGBOOT_MANAGEMENT_CONTEXT_PATHY_VALUE+"/gc", true);
    }

    @GetMapping
    @ResponseBody
    public GCLog getGClog(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                          @RequestParam(value = "size", required = false, defaultValue = "100") int size){
        Optional<String> gcLogPath = getGCLogPath();
        GCLog.GCLogBuilder builder = GCLog.builder().page(page).size(size);

        if(gcLogPath.isPresent()){
            String path= gcLogPath.get();
            File gcFile=new File(path);
            long length = gcFile.length();
            try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(gcFile))) {
                lineNumberReader.skip(length);
                builder.total(lineNumberReader.getLineNumber());
                builder.log(Files.lines(gcFile.toPath()).skip((page - 1) * size)
                        .limit(size)
                        .collect(Collectors.toList()));
            } catch (FileNotFoundException e) {
                builder.error("Can not found [-Xloggc]");
            } catch (IOException e) {
                builder.error("Read file error : "+e.getMessage());
            }
        }else {
            builder.error("GClog file can not be found");
        }
        return builder.build();
    }

    private Optional<String> getGCLogPath(){
        // -Xlog:gc:./gclogs
        // -Xloggc:./gclogs
        return ManagementFactory.getRuntimeMXBean().getInputArguments()
                .stream()
                .filter(s-> s.startsWith("-Xlog"))
                .map(s-> s.replace("-Xlog:gc:","").replace("-Xloggc:",""))
                .filter(f->new File(f).exists())
                .findFirst();
    }

    @Data
    @Builder
    public static class GCLog {
        int page;
        int size;
        List<String> log;
        int total;
        String error;
    }

}
