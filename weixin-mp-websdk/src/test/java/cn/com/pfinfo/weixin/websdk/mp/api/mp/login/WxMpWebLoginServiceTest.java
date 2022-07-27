package cn.com.pfinfo.weixin.websdk.mp.api.mp.login;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;
import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.event.QrCodeScanEventDetail;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * created by cuitpanfei on 2022/07/26
 *
 * @author cuitpanfei
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WxMpWebLoginServiceTest {
    WxMpWebLoginService service = Singleton.get(WxMpWebLoginServiceImpl.class);

    @BeforeAll
    void start() {
        WxWebHttpUtil.getNewWxUin();
        EventManager.addListener(Singleton.get(QrCodeScanEventDetail.class));
    }

    @org.junit.jupiter.api.Test
    void getLoginQRCodeDecodeLinkForce() {
        String loginQRCodeDecodeLink = service.getLoginQRCodeDecodeLinkForce();
        System.out.println(loginQRCodeDecodeLink);
        System.out.println(MpApp.appId());
        Assertions.assertNotNull(loginQRCodeDecodeLink);
    }

    @org.junit.jupiter.api.Test
    void getLoginQRCodeDecodeLink() {
        String loginQRCodeDecodeLink = service.getLoginQRCodeDecodeLink();
        System.out.printf("appId=[%s] \t qrTicketUrl=[%s]\n", MpApp.appId(), loginQRCodeDecodeLink);
        Assertions.assertNotNull(loginQRCodeDecodeLink);
    }

    @org.junit.jupiter.api.Test
    void scanLoginQrCodeAck() {
        QrCodeScanState state = service.scanLoginQrCodeAck();
        Assertions.assertEquals(QrCodeScanState.UN_KNOW, state);
        getLoginQRCodeDecodeLink();
        state = service.scanLoginQrCodeAck();
        Assertions.assertEquals(QrCodeScanState.WAIT_SCAN, state);
    }

    @org.junit.jupiter.api.Test
    void bizLogin() {
        CountDownLatch latch = new CountDownLatch(1);
        getLoginQRCodeDecodeLink();
        ThreadUtil.execute(() -> {
            int i = 100;
            while (i-- >= 0 && MpApp.token() == null) {
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

        System.out.println(MpApp.token());

    }
}