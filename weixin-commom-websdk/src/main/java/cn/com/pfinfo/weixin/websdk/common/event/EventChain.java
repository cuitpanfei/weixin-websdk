package cn.com.pfinfo.weixin.websdk.common.event;

import cn.com.pfinfo.weixin.websdk.common.exception.RRException;
import cn.hutool.log.Log;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class EventChain<T> extends LinkedBlockingDeque<WxSdkEvent<T>> implements Runnable {
    private static final Log log = Log.get(EventChain.class);
    private static final int ERROR_MAX_FLAG = 10;

    private final Class<T> clazz;


    AtomicInteger takeErrCount = new AtomicInteger();
    private List<WxSdkEventListener<T>> listeners = new ArrayList<>();

    public EventChain(Class<T> clazz) {
        super(1000);
        this.clazz = clazz;
    }

    public void addListener(WxSdkEventListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public boolean add(WxSdkEvent<T> event) {
        if (takeErrCount.intValue() > ERROR_MAX_FLAG) {
            log.warn("Queue[{}]'s Progressive Failures more than 10. Reject add event.", clazz.getName());
            return false;
        }
        return super.add(event);
    }

    @SneakyThrows
    @Override
    public void run() {
        WxSdkEvent<T> event = take();
        try {
            listeners.forEach(l -> l.onEvent(event));
            takeErrCount.set(0);
        } catch (RRException ex) {
            log.warn(ex, "Queue[{}] fail.", clazz.getName());
            takeErrCount.incrementAndGet();
        }

    }
}
