package cn.com.pfinfo.weixin.websdk.mp.api.mp.login;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;
import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.event.QrCodeScanEventDetail;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        FileOutputStream fos = null;
        try {
            File tmp = File.createTempFile("qrTicketUrl", ".jpg");
            QrCodeUtil.generate(loginQRCodeDecodeLink,300,300,tmp);
            Runtime.getRuntime().exec("cmd /c start " + tmp.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        String appId = MpApp.appId();
        ThreadUtil.execute(() -> {
            MpApp.updateAppId(appId);
            int i = 5;
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