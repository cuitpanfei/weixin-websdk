package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public final class CookieManager extends ConcurrentHashMap<String, CookieModel> {
    public static final CookieManager INSTANCE = new CookieManager();

    CookieManager() {
    }
}
