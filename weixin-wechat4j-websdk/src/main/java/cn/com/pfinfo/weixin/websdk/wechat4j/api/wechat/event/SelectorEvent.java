package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.wechat4j.enums.Selector;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class SelectorEvent {
    public final Selector e;

    public SelectorEvent(Selector e) {
        this.e = e;
    }
}
