package cn.com.pfinfo.weixin.websdk.common.model;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface CookieModel {
    /**
     * 当前应用cookie
     * @return 当前应用cookie。
     */
    String cookie();

    void updateCookie(String cookie);
}
