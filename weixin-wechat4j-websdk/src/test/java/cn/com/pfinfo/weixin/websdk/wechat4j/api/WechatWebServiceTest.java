package cn.com.pfinfo.weixin.websdk.wechat4j.api;

import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.WxWechatWebService;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
class WechatWebServiceTest {

    static WxWechatWebService wechat;

    @BeforeAll
    static void setUp() {
        System.out.println("before");
        wechat = WechatWebService.me().wechat();
        WechatApp.getContext().wxuin="815143066";
        wechat.autoLogin(true);
    }
    @AfterAll
    static void after() {
        System.out.println("after");
        WechatApp.storage();
        wechat.finish();
    }

    @Test
    @Order(1)
    void me() {
        CountDownLatch latch = new CountDownLatch(1);
        String appId = App.appId();
        ThreadUtil.execute(() -> {
            App.updateAppId(appId);
            int i = 10;
            while (i-- >= 0 && WechatApp.getContext().isOnline) {
                ThreadUtil.sleep(2, TimeUnit.SECONDS);
            }
            latch.countDown();
        });
        try {
            //   当计数为0时结束阻塞
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void testSendText() {
        JSONObject result = wechat.sendTextToUserName(null, "这是消息内容:"+System.currentTimeMillis());
        System.out.println(result);
    }
}