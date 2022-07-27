package cn.com.pfinfo.weixin.websdk.mp.api.mp.login.event;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;
import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEventListener;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginServiceImpl;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/26
 *
 * @author cuitpanfei
 */
public class QrCodeScanEventDetail implements WxSdkEventListener<QrCodeScanEvent> {
    private static final Log log = Log.get(QrCodeScanEventDetail.class);

    private WxMpWebLoginService service = Singleton.get(WxMpWebLoginServiceImpl.class);

    @Override
    public void onEvent(WxSdkEvent<QrCodeScanEvent> eventSource) {
        Optional<QrCodeScanEvent> source = eventSource.getSource();
        source.ifPresent(event -> {
            String appId = MpApp.appId();
            switch (event.getState()) {
                case WAIT_SCAN:
                    // 网页二维码，还没有被扫描。
                    log.info("[{}]的网页二维码，还没有被扫描", appId);
                case SCAN:
                    // 网页二维码，已被扫描，等待用户确认，
                    // 休息一下，继续检测。
                    ThreadUtil.sleep(2000);
                    QrCodeScanState qrCodeScanState = service.scanLoginQrCodeAck();
                    if (!qrCodeScanState.equals(QrCodeScanState.WAIT_SCAN)) {
                        log.info("[{}]的网页二维码，状态检测结果:[{}]", appId, qrCodeScanState);
                        QrCodeScanState.clearQrcodeInfoOf(appId);
                    }
                    event.setState(qrCodeScanState);
                    EventManager.publish(event);
                    break;
                case EXPIRE:
                    log.info("[{}]的网页二维码，已过期", appId);
                    break;
                case DONE:
                    log.info("[{}]的网页二维码，已被扫描，用户已确认。准备获取token", appId);
                    service.bizLogin();
                    break;
                default:
                    QrCodeScanState.clearQrcodeInfoOf(appId);
                    log.info("[{}]的网页二维码，检测到未知扫描状态，中止后续操作。", appId);
            }
        });
    }

}
