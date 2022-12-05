package cn.com.pfinfo.weixin.websdk.common.event;

/**
 * created by cuitpanfei on 2022/12/05
 *
 * @author cuitpanfei
 */
public class CookieChangeEvent {
    public final String appId;
    public final String oldCookies;
    public final String newCookies;

    public CookieChangeEvent(String appId, String oldCookies, String newCookies) {
        this.appId = appId;
        this.oldCookies = oldCookies;
        this.newCookies = newCookies;
    }

    public static CookieChangeEvent of(String appId, String oldCookies, String newCookies) {
        return new CookieChangeEvent(appId, oldCookies, newCookies);
    }
}
