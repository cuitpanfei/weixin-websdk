package cn.com.pfinfo.weixin.websdk.mp.api.mp.login.event;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;

/**
 * created by cuitpanfei on 2022/07/26
 *
 * @author cuitpanfei
 */
public class QrCodeScanEvent {

    private QrCodeScanState state;

    public QrCodeScanEvent(QrCodeScanState state) {
        this.state = state;
    }

    public QrCodeScanState getState() {
        return this.state;
    }

    public void setState(QrCodeScanState qrCodeScanState) {
        this.state = qrCodeScanState;
    }
}
