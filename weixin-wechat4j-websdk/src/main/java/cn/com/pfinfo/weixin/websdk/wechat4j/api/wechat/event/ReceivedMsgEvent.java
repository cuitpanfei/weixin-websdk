package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.wechat4j.model.ReceivedMsg;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class ReceivedMsgEvent {
    public final List<ReceivedMsg> messages;

    public ReceivedMsgEvent(List<ReceivedMsg> messages) {
        this.messages = Optional.ofNullable(messages).orElseGet(Collections::emptyList);
    }
}
