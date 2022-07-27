package cn.com.pfinfo.weixin.websdk.common.api;

import cn.com.pfinfo.weixin.websdk.common.stage.App;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface WxWebAppService {


    /**
     * app 类型
     *
     * @return app 类型
     */
    String appType();

    /**
     * 服务是否是有状态的，既是：是否需要登录后才可以访问。
     *
     * @return 服务是否需要登录后才可以访问， 默认为是
     */
    default boolean needCookie() {
        return true;
    }

    /**
     * 检查服务的cookie是否过期，过期则返回true，否则返回false。
     *
     * @return 服务的cookie的过期状态。过期则返回true，否则返回false。
     */
    default boolean cookieIsExpired() {
        return false;
    }

    /**
     * @return 服务是否过期，默认没有过期。
     */
    default boolean serverIsExpired() {
        return needCookie() && cookieIsExpired();
    }

    /**
     * 切换当前应用appId
     *
     * @param appId 应用appId
     * @return 切换当前应用appId后的服务自身，方便后续调用
     */
    default WxWebAppService switchoverTo(String appId) {
        App.updateAppId(appId);
        return this;
    }

    /**
     * 结束服务，清理当前应用appId
     */
    default void finish() {
        App.clearCurrentApp();
    }
}
