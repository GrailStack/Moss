package org.xujin.moss.enums;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @Description:
 * @Author: xujin
 * @Create: 2018/11/22 10:19
 **/
public class AppStatusEnum {
    public static final Map<String,String> status;
    static {
        ConcurrentHashMap<String,String> enums=new ConcurrentHashMap<>();
        enums.put("0","创建");
        enums.put("1","开发中");
        enums.put("2","运行中");
        enums.put("3","已下线");
        status=Collections.unmodifiableMap(enums);
    }
}
