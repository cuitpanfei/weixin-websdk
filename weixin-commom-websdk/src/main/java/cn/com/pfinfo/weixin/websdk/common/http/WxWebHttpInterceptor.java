package cn.com.pfinfo.weixin.websdk.common.http;

import cn.com.pfinfo.weixin.websdk.common.model.CookieModel;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpInterceptor;

import java.util.Optional;

/**
 * 实现本接口后会被自动装载到全局拦截器中相应的请求拦截器队列或响应拦截器队列中。
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 * @see WxWebHttpUtil#addHttpInterceptor()
 */
@FunctionalInterface
public interface WxWebHttpInterceptor<T extends HttpBase<T>> extends HttpInterceptor<T> {
    /**
     * 处理请求
     *
     * @param httpObj 请求或响应对象
     */
    void process(T httpObj, Optional<CookieModel> modelOptional);

    /**
     * 处理请求
     *
     * @param httpObj 请求或响应对象
     */
    @Override
    default void process(T httpObj) {
        process(httpObj, App.loadCookie());
    }

}
