package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event;

import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEvent;
import cn.com.pfinfo.weixin.websdk.common.event.WxSdkEventListener;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.msg.WxWechatWebMsgService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.msg.WxWechatWebMsgServiceImpl;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.ReceivedMsg;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.hutool.core.lang.Singleton;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.List;


/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class SelectorEventDetail implements WxSdkEventListener<SelectorEvent> {

    private static final Log log = LogFactory.get(SelectorEventDetail.class);
    private WxWechatWebMsgService syncDataService = Singleton.get(WxWechatWebMsgServiceImpl.class);

    @Override
    public void onEvent(WxSdkEvent<SelectorEvent> eventSource) {
        eventSource.getSource().ifPresent(this::processSelector);
    }

    void processSelector(SelectorEvent event) {
        switch (event.e) {
            case SELECTOR_0:
                break;
            case SELECTOR_2:
                webWxSync();
                break;
            case SELECTOR_4:
                break;
            case SELECTOR_6:
                break;
            case SELECTOR_7:
                break;
            default:
                break;
        }
    }


    /**
     * 同步数据
     */
    private void webWxSync() {
        try {
            JSONObject result = syncDataService.webWxSync();
            if (result == null) {
                log.error("从服务端同步新数据异常");
                return;
            }
            //更新SyncKey
            WechatApp.getContext().syncKey = result.getJSONObject("SyncKey");
            //新消息处理
            JSONArray addMsgList = result.getJSONArray("AddMsgList");
            processNewMsg(addMsgList);

        } catch (Exception e) {
            log.error("Execute webWxSync error.", e);
        }
    }

    private void processNewMsg(JSONArray addMsgList) {
        List<ReceivedMsg> receivedMsgList = ReceivedMsg.valueOf(addMsgList);
        if (receivedMsgList.isEmpty()) {
            return;
        }
        int len = receivedMsgList.size();
        log.debug("收到{}条新消息", len);
    }
}
