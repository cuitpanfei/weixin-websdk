package cn.com.pfinfo.weixin.websdk.wechat4j.http.errcode;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;

/**
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public class WxWechatErrCodeParser implements ErrCodeParser<Integer> {
    @Override
    public String appType() {
        return WxConsts.AppType.WECHAT_WEB_TYPE;
    }

    @Override
    public String name() {
        return "wechat_login_err_code_parser";
    }

    @Override
    public String msg4ErrCode(Integer code) {
        return null;
    }
}
