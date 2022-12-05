package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.hutool.http.HttpRequest;
import cn.hutool.log.Log;

import java.util.Optional;

/**
 * 响应拦截器，实现{@linkplain WxWebHttpInterceptor}接口后，会自动装载到全局响应拦截器队列
 * <p>
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class DefaultWxWebReqHttpInterceptor implements WxWebHttpInterceptor<HttpRequest> {

    private static final Log log = Log.get(DefaultWxWebReqHttpInterceptor.class);


    /**
     * 处理请求
     *
     * @param response 响应对象
     */
    @Override
    public void process(HttpRequest req, Optional<CookieModel> modelOptional) {
        App.cookie().ifPresent(req::cookie);
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(req.form())
                .ifPresent(map -> map.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n")));
        log.debug("url:[{}], method:[{}]", req.getUrl(), req.getMethod());
        DefaultWxWebRspHttpInterceptor.REQ_INFO.set(String.format("request: %s,%n form:%n%s", req, sb));
    }

}
