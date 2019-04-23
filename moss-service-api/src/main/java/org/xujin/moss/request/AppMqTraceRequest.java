package org.xujin.moss.request;

import lombok.Data;

@Data
public class AppMqTraceRequest {
    private int pageNo=1;
    private int pageSize=10;
    private String appName;
    private String type;
    private String messageId;

}
