package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.syncdata;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public interface WxWechatWebSyncDataService {
    JSONObject webWeixinInit();
    JSONObject statusNotify(String userName);
    JSONObject syncCheck(JSONArray syncKeyList);
}
