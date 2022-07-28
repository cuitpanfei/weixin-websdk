package cn.com.pfinfo.weixin.websdk.mp.stage;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.stage.App;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public class MpApp extends App {
    private static final Map<String, String> TOKEN = new ConcurrentHashMap<>();
    private static final Map<String, WxCommonData> COMMON_DATA = new ConcurrentHashMap<>();

    static {
        setType(WxConsts.AppType.MP_TYPE);
    }

    public static void setToken(String token) {
        MpApp.TOKEN.put(App.appId(), token);
    }

    public static String token() {
        return MpApp.TOKEN.get(App.appId());
    }

    public static void updateCommonData(WxCommonData commonData) {
        String appId = App.appId();
        COMMON_DATA.put(appId, commonData);
    }

    public static String ticket() {
        return Optional.ofNullable(COMMON_DATA.get(App.appId()))
                .map(WxCommonData::getTicket)
                .orElse(null);
    }

    public static String ticketId() {
        return Optional.ofNullable(COMMON_DATA.get(App.appId()))
                .map(WxCommonData::getUserName)
                .orElse(null);
    }
}
