package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.wechat4j.enums.ExitType;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class ExitEvent {
    public final ExitType type;
    public final Throwable t;

    public ExitEvent(ExitType type, Throwable t) {
        this.type = type;
        this.t = t;
    }
}
