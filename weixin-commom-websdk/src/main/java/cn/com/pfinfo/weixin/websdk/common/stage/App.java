package cn.com.pfinfo.weixin.websdk.common.stage;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.http.CookieManager;
import cn.com.pfinfo.weixin.websdk.common.model.AuthModel;
import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class App {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();
    private static String appType = WxConsts.AppType.MP_TYPE;


    protected App() {
    }

    public static String type() {
        return appType;
    }

    /**
     * 设置当前应用类别
     *
     * @param type 应用类别
     * @see cn.com.pfinfo.weixin.websdk.common.consts.WxConsts.AppType
     */
    protected static void setType(String type) {
        App.appType = type;
    }

    public static boolean hasCookie() {
        return cookie() != null;
    }

    public static String cookie() {
        return loadCookie().map(CookieModel::cookie).orElse(null);
    }


    public static void updateCookie(String cookie) {
        loadCookie().ifPresent(cookieModel -> cookieModel.updateCookie(cookie));
    }

    public static String appId() {
        return CURRENT.get();
    }

    public static void updateAppId(String appId) {
        CURRENT.set(appId);
    }


    public static Optional<CookieModel> loadCookie() {
        return getAppId().map(appId -> CookieManager.INSTANCE.computeIfAbsent(appId,
                id -> AuthModel.builder().openId(id).build()));
    }

    public static Optional<String> getAppId() {
        return Optional.ofNullable(appId());
    }

    public static void clearCurrentApp() {
        App.CURRENT.remove();
    }
}
