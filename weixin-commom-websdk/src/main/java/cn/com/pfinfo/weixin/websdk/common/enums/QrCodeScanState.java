package cn.com.pfinfo.weixin.websdk.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 二维码状态
 *
 * @author cuitpanfei
 */
public enum QrCodeScanState {
    /**
     * 未知结果
     */
    UN_KNOW(-1),
    /**
     * 等待扫描
     */
    WAIT_SCAN(0),
    /**
     * 扫描中，等待用户确认
     */
    SCAN(4),
    /**
     * 二维码过期
     */
    EXPIRE(3),
    /**
     * 已确认结果，扫描完成
     */
    DONE(1);

    int status;

    static Map<String, String> QRCODE_INFO = new ConcurrentHashMap<>();

    QrCodeScanState(int status) {
        this.status = status;
    }

    public static QrCodeScanState statusOf(int status) {
        return Arrays.stream(QrCodeScanState.values())
                .filter(state -> state.status == status)
                .findFirst().orElse(UN_KNOW);
    }

    public static void qrcodeInfo(String appid, String qrcodeInfo) {
        QRCODE_INFO.put(appid, qrcodeInfo);
    }

    public static Optional<String> qrcodeInfoOf(String appid) {
        return Optional.ofNullable(appid).map(id -> QRCODE_INFO.get(id));
    }

    public static void clearQrcodeInfoOf(String appid) {
        QRCODE_INFO.remove(appid);
    }
}
