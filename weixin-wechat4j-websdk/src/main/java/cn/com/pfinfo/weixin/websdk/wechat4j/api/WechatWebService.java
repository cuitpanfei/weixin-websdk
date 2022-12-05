package cn.com.pfinfo.weixin.websdk.wechat4j.api;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.WxWechatWebService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.WxWechatWebServiceImpl;
import cn.hutool.core.lang.Singleton;

/**
 * created by cuitpanfei on 2022/11/30
 *
 * @author cuitpanfei
 */
@FunctionalInterface
public interface WechatWebService {
    String desc();

    default WxWechatWebService wechat() {
        return Singleton.get(WxWechatWebServiceImpl.class);
    }

    static WechatWebService me() {
        return DefaultWxWechatWebServiceImpl.INSTANCE;
    }

    class DefaultWxWechatWebServiceImpl implements WechatWebService {
        static final WechatWebService INSTANCE;

        static {
            INSTANCE = Singleton.get(DefaultWxWechatWebServiceImpl.class);
            Singleton.put(WechatWebService.class.getName(), INSTANCE);
        }

        @Override
        public String desc() {
            return "默认的微信网页版本接口服务";
        }
    }


}
