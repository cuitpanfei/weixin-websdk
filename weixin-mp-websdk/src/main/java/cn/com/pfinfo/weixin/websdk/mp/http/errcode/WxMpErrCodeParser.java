package cn.com.pfinfo.weixin.websdk.mp.http.errcode;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.http.errcode.ErrCodeParser;

/**
 * created by cuitpanfei on 2022/07/27
 *
 * @author cuitpanfei
 */
public class WxMpErrCodeParser implements ErrCodeParser<Integer> {
    @Override
    public String appType() {
        return WxConsts.AppType.MP_TYPE;
    }

    @Override
    public String name() {
        return "login_err_code_parser";
    }

    @Override
    public String msg4ErrCode(Integer code) {
        return null;
    }
}
