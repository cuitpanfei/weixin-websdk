package cn.com.pfinfo.weixin.websdk.mp.api;

import cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.WxMpWebServiceImpl;
import cn.hutool.core.lang.Singleton;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
@FunctionalInterface
public interface WxWebService {

    /**
     * 服务功能的描述
     *
     * @return
     */
    String desc();

    /**
     * 微信公众号网页版接口服务
     *
     * @return weixin mp web app Service.
     */
    default WxMpWebService mpWebService() {
        return Singleton.get(WxMpWebServiceImpl.class);
    }



    static WxWebService me() {
        return DefaultWxMpWebServiceImpl.INSTANCE;
    }

    class DefaultWxMpWebServiceImpl implements WxWebService {

        public static final WxWebService INSTANCE = new DefaultWxMpWebServiceImpl();

        @Override
        public String desc() {
            return "默认的微信平台网页接口服务";
        }
    }

}