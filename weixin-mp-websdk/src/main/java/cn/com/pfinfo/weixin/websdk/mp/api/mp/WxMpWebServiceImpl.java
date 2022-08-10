package cn.com.pfinfo.weixin.websdk.mp.api.mp;

import cn.com.pfinfo.weixin.websdk.common.http.WxWebHttpUtil;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.WxMpWebDraftService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.draft.WxMpWebDraftServiceImpl;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.login.WxMpWebLoginServiceImpl;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.material.WxMpWebMaterialService;
import cn.com.pfinfo.weixin.websdk.mp.api.mp.material.WxMpWebMaterialServiceImpl;
import cn.hutool.core.lang.Singleton;
import cn.hutool.log.Log;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxMpWebServiceImpl implements WxMpWebService {
    private static final Log log = Log.get(WxMpWebServiceImpl.class);

    @Override
    public WxMpWebLoginService loginService() {
        return Singleton.get(WxMpWebLoginServiceImpl.class);
    }

    @Override
    public WxMpWebMaterialService materialService() {
        return Singleton.get(WxMpWebMaterialServiceImpl.class);
    }

    @Override
    public WxMpWebDraftService draftService() {
        return Singleton.get(WxMpWebDraftServiceImpl.class);
    }

    @Override
    public boolean cookieIsExpired() {
        try {
            String body = WxWebHttpUtil.get(MP_WEIXIN_QQ_COM);
            return !body.contains("近期编辑");
        } catch (Exception e) {
            log.error(e);
            return true;
        }
    }
}
