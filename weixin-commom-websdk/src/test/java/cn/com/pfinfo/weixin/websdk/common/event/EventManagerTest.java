package cn.com.pfinfo.weixin.websdk.common.event;

import cn.com.pfinfo.weixin.websdk.common.enums.QrCodeScanState;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/26
 *
 * @author cuitpanfei
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventManagerTest {

    WxSdkEventListener<QrCodeScanState> listener;

    @BeforeAll
    void start() {
        listener = new WxSdkEventListener<QrCodeScanState>() {
            private final Log log = LogFactory.get("listener[" + type().getSimpleName() + "]");

            @Override
            public void onEvent(WxSdkEvent<QrCodeScanState> eventSource) {
                Optional<QrCodeScanState> source = eventSource.getSource();
                source.ifPresent(src -> log.info("handler source[{}]", src));
                log.info("handler event[{}] down.", eventSource);
            }

        };
        EventManager.addListener(listener);
    }

    void doSomething(Runnable job) {
        for (int i = 0; i < 1000; i++) {
            ThreadUtil.sleep(5);
            job.run();
        }
    }

    @org.junit.jupiter.api.Test
    void publish() {
        doSomething(EventManager::publish);
    }

    @org.junit.jupiter.api.Test
    void testPublish() {
        doSomething(() -> EventManager.publish(QrCodeScanState.WAIT_SCAN));
    }

    @org.junit.jupiter.api.Test
    void addListener() {
        doSomething(() -> EventManager.addListener(listener));
    }
}