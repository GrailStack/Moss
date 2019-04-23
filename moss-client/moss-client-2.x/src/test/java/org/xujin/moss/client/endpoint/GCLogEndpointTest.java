package org.xujin.moss.client.endpoint;

import org.xujin.moss.client.BaseTest;
import org.junit.Test;

/**
 * @Program: moss
 * @Description:
 * @Author: xujin
 * @Create: 2019/2/20 10:29
 **/

public class GCLogEndpointTest extends BaseTest {
    @Test
    public void getGClog(){
        System.gc();System.gc();System.gc();
        GCLogEndpoint gcLogEndpoint=new GCLogEndpoint();
        System.out.println(gcLogEndpoint.getGClog(1,100));
    }
}
