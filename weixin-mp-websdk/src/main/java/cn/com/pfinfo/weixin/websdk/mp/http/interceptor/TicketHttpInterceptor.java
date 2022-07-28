package cn.com.pfinfo.weixin.websdk.mp.http.interceptor;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpInterceptor;
import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.com.pfinfo.weixin.websdk.mp.stage.MpApp;
import cn.com.pfinfo.weixin.websdk.mp.stage.WxCommonData;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
public class TicketHttpInterceptor implements WxWebHttpInterceptor<HttpResponse> {
    /**
     * 处理请求
     *
     * @param httpObj       请求或响应对象
     * @param modelOptional 会话信息
     */
    @Override
    public void process(HttpResponse httpObj, Optional<CookieModel> modelOptional) {
        if (!httpObj.isOk() || !modelOptional.isPresent()) {
            return;
        }
        String header = httpObj.header(Header.CONTENT_TYPE);
        Optional.ofNullable(header)
                .filter(type -> type.toLowerCase().contains(ContentType.TEXT_HTML.toString()))
                .ifPresent(type -> updateCommonData(httpObj.body()));
    }

    private void updateCommonData(String body) {
        WxWebHttpUtil.getCommonData(body)
                .map(entry -> entry.toBean(WxCommonData.class))
                .ifPresent(MpApp::updateCommonData);
    }
}
