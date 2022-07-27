package cn.com.pfinfo.weixin.websdk.mp.api.mp;

import cn.com.pfinfo.weixin.websdk.common.api.WxWebAppService;
import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.material.WxMpWebMaterialService;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface WxMpWebService extends WxWebAppService {

    String MP_WEIXIN_QQ_COM = WxConsts.AppSite.MP_WEIXIN_QQ_COM;
    String BASE_URL = MP_WEIXIN_QQ_COM + "/cgi-bin";

    /**
     * 返回微信公众号平台登陆服务，提供公众号平台登录接口
     *
     * @return 微信公众号平台登陆相关接口方法的实现类对象
     */
    WxMpWebLoginService loginService();

    /**
     * 返回微信公众号平台素材相关接口方法的实现类对象，以方便调用其各个接口.
     *
     * @return 微信公众号平台素材相关接口方法的实现类对象
     */
    WxMpWebMaterialService materialService();

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
