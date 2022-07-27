package cn.com.pfinfo.weixin.websdk.mp.stage;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.stage.App;

/**
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public class MpApp extends App {
    private static String token;

    static {
        setType(WxConsts.AppType.MP_TYPE);
    }

    public static void setToken(String token) {
        MpApp.token = token;
    }

    public static String token() {
        return MpApp.token;
    }
}
