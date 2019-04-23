package org.xujin.moss.request;

import lombok.Data;

@Data
public class ProjectByPageRequest {
    private int pageNo=1;
    private int pageSize=10;
    private String name;

}
