package cn.com.pfinfo.weixin.websdk.wechat4j.stage;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.hutool.core.codec.Base58;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public class WechatApp extends App {

    public static final Setting appSetting = SettingUtil.get("wechat");
    private static final Map<String, WechatContext> CONTEXT = new ConcurrentHashMap<>();
    private static final String STORAGE_GROUP_NAME = "storage";

    static {
        WxWebHttpUtil.init();
        App.setType(WxConsts.AppType.WECHAT_WEB_TYPE);
        App.updateAppId(WechatApp.appSetting.get("webwx", "appid"));
        Optional.ofNullable(appSetting.get(STORAGE_GROUP_NAME, "context"))
                .map(Base58::decode)
                .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                .map(JSONUtil::parseArray)
                .map(json -> json.toList(WechatContext.class))
                .map(list -> list.stream().collect(Collectors.toMap(c -> c.appId, c -> c)))
                .ifPresent(CONTEXT::putAll);
        Optional.ofNullable(appSetting.get(STORAGE_GROUP_NAME, "cookie"))
                .ifPresent(App::updateCookie);
    }

    public static WechatContext getContext() {
        return CONTEXT.computeIfAbsent(App.appId(), WechatContext::new);
    }

    public static void clearAllContext() {
        CONTEXT.values().forEach(WechatContext::clear);
        CONTEXT.clear();
    }

    public static void clearCurrentContext() {
        WechatContext context = CONTEXT.remove(App.appId());
        context.clear();
    }

    public static void storageContext() {
        String str = JSONUtil.toJsonStr(CONTEXT.values());
        String info = Base58.encode(str.getBytes(StandardCharsets.UTF_8));
        appSetting.setByGroup("context", STORAGE_GROUP_NAME, info);
        appSetting.store();
    }

    public static void storageCookie() {
        App.cookie().ifPresent(cookie -> appSetting.setByGroup("cookie", STORAGE_GROUP_NAME, cookie));
        appSetting.store();
    }

    public static void storage() {
        storageContext();
        storageCookie();
    }

    /**
     * 获取Cookie值
     *
     * @param name key
     * @return Cookie值
     */
    public static String getCookieValue(String name) {
        return App.cookie()
                .flatMap(s -> Arrays.stream(s.split(";")).map(String::trim).filter(kv -> kv.startsWith(name + "=")).findFirst())
                .map(s -> s.split("=")[1])
                .orElse(null);
    }
}
