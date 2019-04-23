package org.xujin.moss.request;

import lombok.Data;

@Data
public class AppPageRequest {
    private int pageNo=1;
    private int pageSize=10;
    private String name;
    private String projectName;
    private String status;
    private String takeOver;
}
