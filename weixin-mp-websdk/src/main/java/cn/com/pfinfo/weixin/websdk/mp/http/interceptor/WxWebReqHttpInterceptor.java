package cn.com.pfinfo.weixin.websdk.mp.http.interceptor;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpInterceptor;
import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.hutool.http.HttpRequest;
import cn.hutool.log.Log;

import java.util.Optional;

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
        managerOptional.ifPresent(model -> req.setFollowRedirects(true).cookie(model.cookie()));
        StringBuilder sb = new StringBuilder();
        Optional.ofNullable(req.form())
                .ifPresent(map -> map.forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n")));
        log.debug("url:[{}], method:[{}]", req.getUrl(), req.getMethod());
        WxWebRspHttpInterceptor.REQ_INFO.set(String.format("request: %s,%n form:%n%s", req, sb));
    }
}
