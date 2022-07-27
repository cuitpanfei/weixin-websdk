package cn.com.pfinfo.weixin.websdk.mp.api;

import cn.hutool.core.lang.Singleton;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
class WxWebServiceTest {

    @Test
    void me() {
        WxWebService me = WxWebService.me();
        Assertions.assertEquals(WxWebService.DefaultWxMpWebServiceImpl.INSTANCE, me);
    }

    @Test
    void testSingle() {
        WxWebService me = WxWebService.me();
        Assertions.assertEquals(Singleton.get(WxWebService.class), me);
    }
}