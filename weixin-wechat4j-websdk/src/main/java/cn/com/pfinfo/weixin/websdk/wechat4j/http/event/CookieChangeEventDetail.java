package cn.com.pfinfo.weixin.websdk.wechat4j.http.event;

import cn.com.pfinfo.weixin.websdk.common.event.CookieChangeEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEventListener;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by cuitpanfei on 2022/12/05
 *
 * @author cuitpanfei
 */
@Slf4j
public class CookieChangeEventDetail implements WxSdkEventListener<CookieChangeEvent> {
    @Override
    public void onEvent(WxSdkEvent<CookieChangeEvent> eventSource) {
        eventSource.getSource().ifPresent(this::handlerEvent);
    }

    /**
     * 检查新旧cookie，如果cookie内容确实有变化，store cookie到本地；否则直接结束handler。
     *
     * @param event
     */
    void handlerEvent(CookieChangeEvent event) {
        Set<String> old = sortCookies(event.oldCookies);
        Set<String> now = sortCookies(event.newCookies);
        now.removeAll(old);
        if (now.isEmpty()) {
            return;
        }
        WechatApp.storageCookie();
    }

    Set<String> sortCookies(String cookies) {
        return Optional.ofNullable(cookies).map(value -> Arrays.stream(value.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet())).orElseGet(HashSet::new);
    }
}
