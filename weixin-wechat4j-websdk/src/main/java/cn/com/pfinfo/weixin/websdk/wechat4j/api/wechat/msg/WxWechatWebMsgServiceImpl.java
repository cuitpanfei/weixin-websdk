package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.msg;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.exception.RRException;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.MediaType;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.BaseRequest;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import org.stringtemplate.v4.ST;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public class WxWechatWebMsgServiceImpl implements WxWechatWebMsgService {
    private static final Log log = LogFactory.get(WxWechatWebMsgServiceImpl.class);
    /**
     * 上传媒体文件分片大小
     */
    private static final int UPLOAD_MEDIA_FILE_CHUNK_SIZE = 524288;
    protected Setting msgSetting = WechatApp.appSetting.getSetting("send_received_msg");

    @Override
    public JSONObject webWxSync() {
        WechatContext context = WechatApp.getContext();
        BaseRequest baseRequest = new BaseRequest(context);
        String url = new ST(msgSetting.getStr("webwxsync_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add(WxConsts.ReqFiled.SKEY, baseRequest.getSkey())
                .add(WxConsts.ReqFiled.SID, baseRequest.getSid())
                .add(WxConsts.ReqFiled.PASS_TICKET, context.encodePassTicket())
                .render();
        String body = new JSONObject()
                .putOnce(WxConsts.ReqFiled.BASE_REQ_UP, baseRequest)
                .putOnce(WxConsts.ReqFiled.SYNC_KEY, context.syncKey).toString();

        String res = HttpUtil.post(url, body);
        return JSONUtil.parseObj(res);
    }

    @Override
    public String textMsgUrl() {
        WechatContext context = WechatApp.getContext();
        return new ST(msgSetting.getStr("webwxsendmsg_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add(WxConsts.ReqFiled.PASS_TICKET, context.encodePassTicket())
                .render();
    }

    @Override
    public String videoMsgUrl() {
        WechatContext context = WechatApp.getContext();
        return new ST(msgSetting.getStr("webwxsendvideomsg_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .render();
    }

    @Override
    public String imageMsgUrl() {
        WechatContext context = WechatApp.getContext();
        return new ST(msgSetting.getStr("webwxsendmsgimg_url"))
                .add(WxConsts.ReqFiled.URL_VERSION, context.urlVersion)
                .add(WxConsts.ReqFiled.PASS_TICKET, context.encodePassTicket())
                .render();
    }

    @Override
    public JSONObject uploadMedia(String fromUserName, String toUserName, byte[] mediaData, String mediaName, MediaType mediaType) {
        WechatContext context = WechatApp.getContext();
        BaseRequest baseRequest = new BaseRequest(context);
        String url = new ST(msgSetting.getStr("uploadmedia_url"))
                .add("urlVersion", context.urlVersion)
                .render();
        long millis = System.currentTimeMillis();
        int mediaLength = mediaData.length;

        HttpRequest request = HttpRequest.post(url).contentType(ContentType.MULTIPART.getValue());
        JSONObject uploadmediarequest = new JSONObject()
                .putOnce(WxConsts.ReqFiled.UPLOAD_TYPE, 2)
                .putOnce(WxConsts.ReqFiled.BASE_REQ_UP, baseRequest)
                .putOnce(WxConsts.ReqFiled.CLIENT_MEDIA_ID, millis)
                .putOnce(WxConsts.ReqFiled.TOTAL_LEN, mediaLength)
                .putOnce(WxConsts.ReqFiled.START_POS, 0)
                .putOnce(WxConsts.ReqFiled.DATA_LEN, mediaLength)
                .putOnce(MediaType.REQUEST_JSON_KEY, mediaType.getCode())
                .putOnce(WxConsts.ReqFiled.FROM_USER_NAME, fromUserName)
                .putOnce(WxConsts.ReqFiled.TO_USER_NAME, toUserName)
                .putOnce(WxConsts.ReqFiled.FILE_MD5, DigestUtil.md5Hex(mediaData));

        // 分片数量
        int chunks = new BigDecimal(mediaLength).divide(new BigDecimal(UPLOAD_MEDIA_FILE_CHUNK_SIZE), 0, BigDecimal.ROUND_UP).intValue();

        JSONObject result = null;
        String passticket = context.passTicket;
        for (int chunk = 0; chunk < chunks; chunk++) {
            int from = chunk * UPLOAD_MEDIA_FILE_CHUNK_SIZE;
            int to = (chunk + 1) * UPLOAD_MEDIA_FILE_CHUNK_SIZE;
            to = Math.min(to, mediaLength);
            byte[] temp = Arrays.copyOfRange(mediaData, from, to);
            request.form(WxConsts.ReqFiled.CHUNKS, String.valueOf(chunks))
                    .form(WxConsts.ReqFiled.CHUNK, String.valueOf(chunk))
                    .form(MediaType.REQUEST_KEY, mediaType.getValue())
                    .form(WxConsts.ReqFiled.UPLOAD_MEDIA_REQUEST, uploadmediarequest.toString())
                    .form(WxConsts.ReqFiled.WEBWX_DATA_TICKET, WechatApp.getCookieValue("webwx_data_ticket"))
                    .form(WxConsts.ReqFiled.PASS_TICKET, passticket)
                    .form(WxConsts.ReqFiled.FILE_NAME, temp, mediaName);
            try {
                String res = request.execute().body();
                result = JSONUtil.parseObj(res);
                if (result.isEmpty()) {
                    break;
                }
            } catch (RRException e) {
                log.error(e);
                break;
            }
        }

        return result;
    }
}
