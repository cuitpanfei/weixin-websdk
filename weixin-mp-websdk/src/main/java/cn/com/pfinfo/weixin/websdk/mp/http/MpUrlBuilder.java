package cn.com.pfinfo.weixin.websdk.mp.http;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebService;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.builder.Builder;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;

import java.util.HashMap;
import java.util.Map;

import static cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst.JSON;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
public class MpUrlBuilder implements Builder<String> {
    final UrlBuilder builder;

    MpUrlBuilder(UrlBuilder builder) {
        this.builder = builder;
    }

    MpUrlBuilder(UrlPath path) {
        this(UrlBuilder.of(WxMpWebService.MP_WEIXIN_QQ_COM));
        addQuery("lang", "zh_CN");
        builder.setPath(path);
    }

    public static MpUrlBuilder base(String uri) {
        return new MpUrlBuilder(UrlPath.of(uri, null));
    }

    public static MpUrlBuilder cgi(String uri) {
        return new MpUrlBuilder(UrlPath.of("/cgi-bin", null).add(uri));
    }

    public MpUrlBuilder addQuery(String key, Object value) {
        if (builder.getQuery().getQueryMap().containsKey(key)) {
            deleteQuery(key);
        }
        builder.addQuery(key, value);
        return this;
    }

    public MpUrlBuilder commonQuery() {
        return token().ajax();
    }

    public MpUrlBuilder action(String action) {
        return addQuery("action", action);
    }
    public MpUrlBuilder random() {
        return addQuery("random", WxWebHttpUtil.random());
    }

    public MpUrlBuilder deleteQuery(String query) {
        synchronized (builder) {
            Map<CharSequence, CharSequence> sequenceMap = builder.getQuery().getQueryMap();
            Map<CharSequence, CharSequence> map = new HashMap<>(sequenceMap.size());
            sequenceMap.forEach((k, v) -> {
                if (k != null && k.equals(query)) {
                    return;
                }
                map.put(k, v);
            });
            builder.setQuery(UrlQuery.of(map));
        }
        return this;
    }

    public MpUrlBuilder ajax() {
        return addQuery("f", JSON)
                .addQuery("ajax", "1");
    }

    public MpUrlBuilder token() {
        return addQuery("token", MpApp.token());
    }

    public static MpUrlBuilder parse(String url) {
        return new MpUrlBuilder(UrlBuilder.of(url));
    }

    public HttpRequest post() {
        return request(Method.POST);
    }

    public HttpRequest get() {
        return request(Method.GET);
    }

    public HttpRequest request(Method method) {
        return HttpUtil.createRequest(method, build());
    }

    /**
     * 构建
     *
     * @return 被构建的对象
     */
    @Override
    public String build() {
        return builder.build();
    }

}
