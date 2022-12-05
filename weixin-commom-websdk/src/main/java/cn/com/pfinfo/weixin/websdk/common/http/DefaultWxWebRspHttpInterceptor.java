package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.exception.RRException;
import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;
import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class DefaultWxWebRspHttpInterceptor implements WxWebHttpInterceptor<HttpResponse> {

    public static final ThreadLocal<String> REQ_INFO = new ThreadLocal<>();
    private static final Log log = Log.get(DefaultWxWebRspHttpInterceptor.class);

    static String getCookie(HttpResponse response, Optional<String> oldCookies) {
        Map<String, String> cookieSet = oldCookies.map(value -> Arrays.stream(value.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> s.contains("="))
                .collect(Collectors.toMap(s -> s.split("=")[0], s -> s))).orElseGet(HashMap::new);

        Optional.ofNullable(response.headerList(SET_COOKIE.getValue()))
                .orElse(Collections.emptyList())
                .stream()
                .filter(s -> !s.contains("EXPIRED"))
                .map(s -> s.contains(";") ? s.split(";")[0].trim() : s.trim())
                .map(s -> s.split("="))
                .forEach(arr -> cookieSet.put(arr[0], String.join("=", arr)));
        return join("; ", cookieSet.values());
    }

    /**
     * 处理请求
     *
     * @param response 响应对象
     */
    @Override
    public void process(HttpResponse response, Optional<CookieModel> modelOptional) {
        String body = response.body();
        if (response.isOk()) {
            Optional.ofNullable(response.header(Header.CONTENT_TYPE))
                    .ifPresent(contentType -> {
                        // result is json
                        if (contentType.contains(ContentType.JSON.getValue()) || JSONUtil.isTypeJSONObject(body)) {
                            checkRet(body);
                        }
                    });
            modelOptional.map(CookieModel::cookie).ifPresent(cookie -> App.updateCookie(getCookie(response, cookie)));
        }
        REQ_INFO.remove();
        if (!response.isOk()) {
            log.error("请求的状态不正确，status=[{}], body: {}", response.getStatus(), body);
            throw new RRException("请求的状态不正确：" + body, response.getStatus());
        }
    }

    void checkRet(String body) {
        JSONObject object = JSONUtil.parseObj(body);
        object.getConfig().setIgnoreCase(true);
        Integer ret = null;
        if (object.containsKey(WxConsts.RespFiled.RET)) {
            ret = object.getInt(WxConsts.RespFiled.RET);
        } else if (object.containsKey(WxConsts.RespFiled.BASE_RESP)) {
            ret = object.getByPath(WxConsts.RespFiled.BASE_RESP_DOT_RET, Integer.class);
        } else if (object.containsKey(WxConsts.RespFiled.BASE_RESP_UP)) {
            ret = object.getByPath(WxConsts.RespFiled.BASE_RESP_DOT_RET_UP, Integer.class);
        }
        if (ret != null && ret != 0) {
            if (ret == -101) {
                return;
            }
            String msg = handlerRet(ret);
            String exMsg = String.format("%s%n resp: ret:%d, msg:%s, body:%s", REQ_INFO.get(), ret, msg, body);
            throw new RRException(exMsg, ret);
        }
    }

    String handlerRet(int ret) {
        List<ErrCodeParser<?>> parsers = WxWebHttpUtil.ERR_CODE_PARSER.getOrDefault(App.type(),
                Collections.emptyList());
        for (ErrCodeParser parser : parsers) {
            if (TypeUtil.getTypeArgument(parser.getClass()) == Integer.TYPE) {
                String s = parser.msg4ErrCode(ret);
                if (s != null) {
                    return s;
                }
            }
        }
        return null;
    }
}
