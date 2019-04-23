package org.xujin.moss.request;

import lombok.Data;

@Data
public class UserPageListRequest {
    private int pageNo=1;
    private int pageSize=10;
    private String username;
    private String name;

}
