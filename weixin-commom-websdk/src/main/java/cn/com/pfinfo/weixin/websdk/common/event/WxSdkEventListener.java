package cn.com.pfinfo.weixin.websdk.common.event;

import cn.hutool.core.util.ClassUtil;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface WxSdkEventListener<T> {
    void onEvent(WxSdkEvent<T> eventSource);

    default Class<T> type() {
        return (Class<T>) ClassUtil.getTypeArgument(getClass());
    }
}
