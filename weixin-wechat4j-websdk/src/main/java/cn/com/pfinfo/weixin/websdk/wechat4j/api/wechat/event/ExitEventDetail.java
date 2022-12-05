package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEventListener;
import cn.com.pfinfo.weixin.websdk.wechat4j.handler.ExitEventHandler;
import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class ExitEventDetail implements WxSdkEventListener<ExitEvent> {
    private static final List<ExitEventHandler> HANDLERS;

    static {
        Set<Class<?>> handlers = ClassScanner.scanAllPackageBySuper(CharSequenceUtil.EMPTY, ExitEventHandler.class);
        HANDLERS = handlers.stream()
                .filter(c -> !c.isInterface())
                .map(clazz -> (ExitEventHandler) Singleton.get(clazz))
                .collect(Collectors.toList());
    }

    @Override
    public void onEvent(WxSdkEvent<ExitEvent> eventSource) {
        eventSource.getSource().ifPresent(this::dispatchEvent);
    }

    void dispatchEvent(ExitEvent event) {
        HANDLERS.forEach(handler -> handler.handleExitEvent(event));
    }
}
