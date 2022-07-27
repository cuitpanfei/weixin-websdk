package cn.com.pfinfo.weixin.websdk.mp.stage;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.stage.App;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public class MpApp extends App {
    private static final Map<String, String> TOKEN = new ConcurrentHashMap<>();

    static {
        setType(WxConsts.AppType.MP_TYPE);
    }

    public static void setToken(String token) {
        MpApp.TOKEN.put(App.appId(), token);
    }

    public static String token() {
        return MpApp.TOKEN.get(App.appId());
    }
}
