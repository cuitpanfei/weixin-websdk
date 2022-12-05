package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEventListener;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.WxWechatWebService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.WxWechatWebServiceImpl;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.syncdata.WxWechatWebSyncDataService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.syncdata.WxWechatWebSyncDataServiceImpl;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.ExitType;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.Retcode;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.Selector;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public class SyncMonitorEventDetail implements WxSdkEventListener<SyncMonitorEvent> {
    private static final Log log = Log.get(SyncMonitorEventDetail.class);

    private WxWechatWebService wechatWebService = Singleton.get(WxWechatWebServiceImpl.class);
    private WxWechatWebSyncDataService syncDataService = Singleton.get(WxWechatWebSyncDataServiceImpl.class);

    @Override
    public void onEvent(WxSdkEvent<SyncMonitorEvent> event) {
        Optional<SyncMonitorEvent> source = event.getSource();
        source.map(s -> WechatApp.getContext())
                .filter(context -> context.isOnline && !JSONUtil.isNull(context.syncKey))
                .ifPresent(this::syncMonitor);
    }

    private void syncMonitor(WechatContext context) {
        log.info("on syncMonitorEvent");
        int time = WechatApp.appSetting.getInt("wechat4j.syncCheck.retry.time", 5);
        int index = 0;
        long start = System.currentTimeMillis();
        while (context.isOnline) {
            try {
                JSONObject result = syncDataService.syncCheck(getSyncKeyList(context));
                log.info("微信同步监听心跳返回数据：{}", result);

                //人为退出
                int retcode = result.getInt("retcode");
                if (retcode != Retcode.RECODE_0.getCode()) {
                    log.info("微信退出或从其它设备登录");
                    wechatWebService.logout();
                    processExitEvent(ExitType.REMOTE_EXIT, null);
                    return;
                }
                int selector = result.getInt("selector");
                processSelector(selector);
                ThreadUtil.sleep(2000);
                EventManager.publish(new SyncMonitorEvent());
                break;
            } catch (Throwable e) {
                log.error("同步监听心跳异常", e);
                if (index == 0) {
                    log.info("同步监听请求失败，正在重试...");
                } else if (index > 0) {
                    log.info("第{}次重试失败" + index);
                }
                if (index >= time) {
                    log.info("重复{}次仍然失败，退出微信", index);
                    wechatWebService.logout();
                    processExitEvent(ExitType.ERROR_EXIT, e);
                    return;
                }
                index++;
            }
            //如果时间太短则阻塞2秒
            long end = System.currentTimeMillis();
            if (end - start < 2000) {
                ThreadUtil.sleep(2000);
            }
        }
        processExitEvent(ExitType.LOCAL_EXIT, null);

    }

    private void processSelector(int selector) {
        Selector e = Selector.valueOf(selector);
        if (e == null) {
            log.warn("Cannot process unknow selector {}", selector);
            return;
        }
        EventManager.publish(new SelectorEvent(e));
    }

    private void processExitEvent(ExitType type, Throwable t) {
        EventManager.publish(new ExitEvent(type, t));
    }


    /**
     * 获取SyncKey的List
     *
     * @return 返回数据
     */
    private JSONArray getSyncKeyList(WechatContext context) {
        return context.syncKey.getJSONArray("List");
    }

}
