package cn.com.pfinfo.weixin.websdk.mp.api.mp.login;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;
import cn.com.pfinfo.weixin.websdk.common.stage.App;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface WxMpWebLoginService {

    /**
     * <p>强制获取登录的二维码，以link形式返回，通过外部转为二维码后，需要微信App端摄像头扫描二维码，选择并点击要授权的公众号
     * <p>此公众号将会被本系统管理，知道授权过期。 过期时间由微信公众号平台决定。
     *
     * @return 根据用于登录的二维码解析出来的link
     */
    String getLoginQRCodeDecodeLinkForce();

    /**
     * <p>获取登录的二维码，以link形式返回，通过外部转为二维码后，需要微信App端摄像头扫描二维码，选择并点击要授权的公众号
     * <p>此公众号将会被本系统管理，知道授权过期。 过期时间由微信公众号平台决定。
     * <p>如果系统中存在对应{@linkplain App#appId()}对应的未扫描的二维码link，直接返回
     *
     * @return 根据用于登录的二维码解析出来的link
     */
    String getLoginQRCodeDecodeLink();

    /**
     * 扫描二维码的状态
     *
     * @return 获取登录二维码的状态
     */
    QrCodeScanState scanLoginQrCodeAck();

    void bizLogin();
}
