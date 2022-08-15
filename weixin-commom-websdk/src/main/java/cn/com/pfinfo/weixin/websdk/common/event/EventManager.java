package cn.com.pfinfo.weixin.websdk.common.event;

import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ClassUtil;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class EventManager extends ConcurrentHashMap<Class<?>, EventChain<?>> {
    private static final EventManager CHANNEL = new EventManager();

    private static final ExecutorService SERVICE = ThreadUtil.newFixedExecutor(5, "WxEventThreads", true);

    static {
        Set<Class<?>> listeners = ClassUtil.scanPackageBySuper(CharSequenceUtil.EMPTY, WxSdkEventListener.class);
        for (Class<?> clazz : listeners) {
            if (clazz.isInterface()) {
                continue;
            }
            WxSdkEventListener<?> listener = (WxSdkEventListener<?>) Singleton.get(clazz);
            addListener(listener);
        }
    }
    private EventManager() {
        super();
    }

    public static void publish() {
        EventManager.publish(null);
    }

    public static <T> void publish(T source) {
        WxSdkEvent<T> event = source instanceof WxSdkEvent ? (WxSdkEvent<T>) source : WxSdkEvent.of(source);
        EventChain<T> chain = CHANNEL.getChain(source == null ? Object.class : source.getClass());
        chain.add(event);
        String appId = App.appId();
        SERVICE.execute(() -> {
            App.updateAppId(appId);
            chain.run();
            App.clearCurrentApp();
        });
    }

    public static <T> void addListener(WxSdkEventListener<T> listener) {
        ((EventChain<T>) CHANNEL.getChain(listener.type())).addListener(listener);
    }

    public <T> EventChain<T> getChain(Class<?> clazz) {
        return (EventChain<T>) computeIfAbsent(clazz, key -> new EventChain<>(clazz));
    }

}
