package cn.com.pfinfo.weixin.websdk.mp.http.interceptor;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.exception.RRException;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpInterceptor;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;
import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.hutool.http.Header.SET_COOKIE;
import static java.lang.String.join;

/**
 * 响应拦截器，实现{@linkplain WxWebHttpInterceptor}接口后，会自动装载到全局响应拦截器队列
 * <p>
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxWebRspHttpInterceptor implements WxWebHttpInterceptor<HttpResponse> {

    private static final Log log = Log.get(WxWebReqHttpInterceptor.class);

    public static final ThreadLocal<String> REQ_INFO = new ThreadLocal<>();

    /**
     * 处理请求
     *
     * @param response 响应对象
     */
    @Override
    public void process(HttpResponse response, Optional<CookieModel> modelOptional) {
        String body = response.body();
        if (!response.isOk()) {
            log.error("请求的状态不正确，status=[{}], body: {}", response.getStatus(), body);
        }
        Optional.ofNullable(response.header(Header.CONTENT_TYPE))
                .ifPresent(contentType -> {
                    // result is json
                    if (contentType.contains(ContentType.JSON.getValue())) {
                        checkRet(body);
                    }
                });
        modelOptional.ifPresent(model -> model.updateCookie(getCookie(response, model.cookie())));
        REQ_INFO.remove();
    }

    public static String getCookie(HttpResponse response, String oldCookies) {
        Set<String> cookieSet = oldCookies == null ? new HashSet<>() : Arrays.stream(oldCookies.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Optional.ofNullable(response.headerList(SET_COOKIE.getValue()))
                .orElse(Collections.emptyList())
                .stream()
                .filter(s -> !s.contains("EXPIRED"))
                .map(s -> s.contains(";") ? s.split(";")[0].trim() : s.trim())
                .forEach(cookieSet::add);
        return join("; ", cookieSet);
    }

    void checkRet(String body) {
        JSONObject object = JSONUtil.parseObj(body);
        Integer ret = null;
        if (object.containsKey(WxConsts.RespFiled.RET)) {
            ret = object.getInt(WxConsts.RespFiled.RET);
        } else if (object.containsKey(WxConsts.RespFiled.BASE_RESP)) {
            ret = object.getByPath(WxConsts.RespFiled.BASE_RESP_DOT_RET, Integer.class);
        }
        if (ret != null && ret != 0) {
            if (ret == -101) {
                return;
            }
            String msg = handerRet(ret);
            String exMsg = String.format("%s,%n resp: ret:%d, msg:%s, body:%s", REQ_INFO.get(), ret, msg, body);
            throw new RRException(exMsg, ret);
        }
    }

    String handerRet(int ret) {
        List<ErrCodeParser> parsers = WxWebHttpUtil.ERR_CODE_PARSER.getOrDefault(MpApp.type(), Collections.emptyList());
        for (ErrCodeParser parser : parsers) {
            String s = parser.msg4ErrCode(ret);
            if (s != null) {
                return s;
            }
        }
        return null;
    }
}
