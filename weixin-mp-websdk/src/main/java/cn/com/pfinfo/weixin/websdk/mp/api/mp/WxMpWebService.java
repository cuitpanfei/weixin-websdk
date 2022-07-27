package cn.com.pfinfo.weixin.websdk.mp.api.mp;

import cn.com.pfinfo.weixin.websdk.common.api.WxWebAppService;
import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginService;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface WxMpWebService extends WxWebAppService {

    String MP_WEIXIN_QQ_COM = WxConsts.AppSite.MP_WEIXIN_QQ_COM;
    String BASE_URL = MP_WEIXIN_QQ_COM + "/cgi-bin";

    WxMpWebLoginService loginService();


    /**
     * app 类型
     *
     * @return app 类型
     */
    @Override
    default String appType() {
        return WxConsts.AppType.MP_TYPE;
    }
}
