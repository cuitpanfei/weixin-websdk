package cn.com.pfinfo.weixin.websdk.mp.http.interceptor;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpInterceptor;
import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.log.Log;

import java.util.Optional;

import static cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst.AJAX;
import static cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst.JSON;
import static cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst.LANG;
import static cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst.LANG_ZH_CN;
import static cn.com.pfinfo.weixin.websdk.mp.consts.MpWebConst.TOKEN;

/**
 * 请求拦截器，实现{@linkplain WxWebHttpInterceptor}接口后，会自动装载到全局请求拦截器队列
 * <p>
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxWebReqHttpInterceptor implements WxWebHttpInterceptor<HttpRequest> {

    private static final Log log = Log.get(WxWebReqHttpInterceptor.class);

    /**
     * 处理请求
     *
     * @param req 请求或响应对象
     */
    @Override
    public void process(HttpRequest req, Optional<CookieModel> managerOptional) {
        managerOptional.ifPresent(model -> {
            if (req.header(Header.REFERER) == null) {
                req.header(Header.REFERER, WxConsts.AppSite.MP_WEIXIN_QQ_COM);
            }
            String token = MpApp.token();
            UrlBuilder builder = UrlBuilder.of(req.getUrl());

            if (!Optional.ofNullable(builder.getQuery().get(TOKEN)).isPresent()) {
                builder.addQuery(TOKEN, token);
            }
            if (!Optional.ofNullable(builder.getQuery().get(LANG)).isPresent()) {
                builder.addQuery(LANG, LANG_ZH_CN);
            }
            if (ContentType.isFormUrlEncode(req.header(Header.CONTENT_TYPE))) {
                req.form(TOKEN, token).form("f", JSON)
                        .form(AJAX, 1).form(LANG, LANG_ZH_CN);
            }
            req.setUrl(builder).setFollowRedirects(true).cookie(model.cookie());
        });
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(req.form())
                .ifPresent(map -> map.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n")));
        log.debug("url:[{}], method:[{}]", req.getUrl(), req.getMethod());
        WxWebRspHttpInterceptor.REQ_INFO.set(String.format("request: %s,%n form:%n%s", req, sb));
    }
}
