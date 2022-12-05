package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEventListener;
import cn.com.pfinfo.weixin.websdk.wechat4j.handler.ReceivedMsgHandler;
import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.log.Log;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class ReceivedMsgEventDetail implements WxSdkEventListener<ReceivedMsgEvent> {
    private static final Log log = Log.get(ReceivedMsgEventDetail.class);
    private static final List<ReceivedMsgHandler> HANDLERS;

    static {
        Set<Class<?>> handlers = ClassScanner.scanAllPackageBySuper(CharSequenceUtil.EMPTY, ReceivedMsgHandler.class);
        HANDLERS = handlers.stream()
                .filter(c -> !c.isInterface())
                .map(clazz -> (ReceivedMsgHandler) Singleton.get(clazz))
                .collect(Collectors.toList());
        log.info("加载了{}个消息处理器", HANDLERS.size());
    }

    @Override
    public void onEvent(WxSdkEvent<ReceivedMsgEvent> eventSource) {
        eventSource.getSource().ifPresent(this::dispatchEvent);
    }

    void dispatchEvent(ReceivedMsgEvent event) {
        HANDLERS.forEach(handler -> event.messages.forEach(handler::handleAllType));
    }
}
