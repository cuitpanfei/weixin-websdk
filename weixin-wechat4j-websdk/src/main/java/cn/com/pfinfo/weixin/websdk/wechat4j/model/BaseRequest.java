package cn.com.pfinfo.weixin.websdk.wechat4j.model;

import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.com.pfinfo.weixin.websdk.wechat4j.util.WechatUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基本请求模型
 *
 * @auther cuitpanfei
 */
@Getter
@Setter
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deviceId;
    private String sid;
    private String skey;
    private String uin;

    public BaseRequest() {

    }
    public BaseRequest(String deviceId, String sid, String skey, String uin) {
        this.deviceId = deviceId;
        this.sid = sid;
        this.skey = skey;
        this.uin = uin;
    }
    public BaseRequest(WechatContext context) {
        this(context.wxsid, context.skey, context.wxuin);
    }
    public BaseRequest(String sid, String skey, String uin) {
        this(WechatUtil.createDeviceID(), sid, skey, uin);
    }
}
