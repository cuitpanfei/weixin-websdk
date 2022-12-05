package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.syncdata;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.BaseRequest;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.com.pfinfo.weixin.websdk.wechat4j.util.WechatUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.net.RFC3986;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import org.stringtemplate.v4.ST;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public class WxWechatWebSyncDataServiceImpl implements WxWechatWebSyncDataService {
    private static final Log log = LogFactory.get(WxWechatWebSyncDataServiceImpl.class);
    protected Setting syncDataSetting = WechatApp.appSetting.getSetting("sync_data");
    @Override
    public JSONObject webWeixinInit() {
        WechatContext context = WechatApp.getContext();
        String url = new ST(syncDataSetting.getStr("webwxinit_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add(WxConsts.ReqFiled.PASS_TICKET, context.encodePassTicket())
                .add("r", System.currentTimeMillis() / 1252L)
                .render();
        String body = JSONUtil.createObj().putOnce(WxConsts.ReqFiled.BASE_REQ_UP, new BaseRequest(context)).toString();
        String result = HttpUtil.post(url, body);
        return JSONUtil.parseObj(result);
    }

    @Override
    public JSONObject statusNotify(String loginUserName) {
        WechatContext context = WechatApp.getContext();
        String url = new ST(syncDataSetting.getStr("statusnotify_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add(WxConsts.ReqFiled.PASS_TICKET, context.encodePassTicket())
                .render();
        String body = JSONUtil.createObj()
                .putOnce(WxConsts.ReqFiled.BASE_REQ_UP, new BaseRequest(context))
                .putOnce("ClientMsgId", System.currentTimeMillis())
                .putOnce("Code", 3)
                .putOnce("FromUserName", loginUserName)
                .putOnce("ToUserName", loginUserName)
                .toString();
        String result = HttpUtil.post(url, body);
        return JSONUtil.parseObj(result);
    }

    @Override
    public JSONObject syncCheck(JSONArray syncKeyList) {
        WechatContext context = WechatApp.getContext();
        BaseRequest baseRequest = new BaseRequest(context);
        long millis = System.currentTimeMillis();
        String url = new ST(syncDataSetting.getStr("synccheck_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add("r", millis)
                .add(WxConsts.ReqFiled.SKEY, RFC3986.FRAGMENT.encode(baseRequest.getSkey(), StandardCharsets.UTF_8))
                .add(WxConsts.ReqFiled.SID, baseRequest.getSid())
                .add(WxConsts.ReqFiled.UIN, baseRequest.getUin())
                .add(WxConsts.ReqFiled.DEVICEID, baseRequest.getDeviceId())
                .add(WxConsts.ReqFiled.SYNC_KEY_LOW, RFC3986.FRAGMENT.encode(WechatUtil.syncKeyListToString(syncKeyList), StandardCharsets.UTF_8))
                .add("_", millis)
                .render();
        String res = HttpUtil.createGet(url)
                .keepAlive(true)
                .execute().body();
        String regExp = "window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"}";
        Matcher matcher = Pattern.compile(regExp).matcher(res);
        if (!matcher.find()) {
            throw ExceptionUtil.wrapRuntime("返回数据错误");
        }
        return new JSONObject().putOnce("retcode", matcher.group(1))
            .putOnce("selector", matcher.group(2));
    }
}
