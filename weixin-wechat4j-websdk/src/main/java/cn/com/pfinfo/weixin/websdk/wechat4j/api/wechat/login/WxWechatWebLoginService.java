package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login;

import cn.com.pfinfo.weixin.websdk.common.http.R;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login.entity.RedirectInfo;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.LoginTip;
import cn.hutool.json.JSONObject;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public interface WxWechatWebLoginService {

    JSONObject wxUuid();

    byte[] qrCode(String uuid);

    R<RedirectInfo> redirect(String uuid, LoginTip tip);

    JSONObject newlogin(String redirectUri);

    void logout();

    JSONObject pushLogin();
}
