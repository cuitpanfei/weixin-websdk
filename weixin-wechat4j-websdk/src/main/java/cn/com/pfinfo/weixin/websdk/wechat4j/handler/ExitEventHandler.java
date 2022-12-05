package cn.com.pfinfo.weixin.websdk.wechat4j.handler;


import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event.ExitEvent;

/**
 * 退出事件处理器，收到事件时，会话信息已经清除。
 *
 * @auther cuitpanfei
 */
public interface ExitEventHandler {
    /**
     * 针对所有类型的退出事件
     *
     * @param exitEvent 退出事件
     */
    default void handleAllType(ExitEvent exitEvent) {
    }

    /**
     * 针对所有类型的退出事件
     *
     * @param exitEvent 退出事件
     */
    default void handleExitEvent(ExitEvent exitEvent) {
        switch (exitEvent.type) {
            case ERROR_EXIT:
                handleErrorExitEvent();
                break;
            case REMOTE_EXIT:
                handleRemoteExitEvent();
                break;
            case LOCAL_EXIT:
                handleLocalExitEvent();
                break;
            default:
                break;
        }
        handleAllType(exitEvent);
    }

    /**
     * 针对错误导致的退出事件
     */
    default void handleErrorExitEvent() {
    }

    /**
     * 针对远程人为导致的退出事件
     */
    default void handleRemoteExitEvent() {
    }

    /**
     * 针对本地任务导致的退出事件
     */
    default void handleLocalExitEvent() {
    }
}
