package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.msg;

import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.MediaType;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.MsgType;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.BaseRequest;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.MediaMessage;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.WxMessage;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Optional;

/**
 * created by cuitpanfei on 2022/12/02
 *
 * @author cuitpanfei
 */
public interface WxWechatWebMsgService {
    JSONObject webWxSync();

    default JSONObject sendMsg(String url, WxMessage message) {
        WechatContext context = WechatApp.getContext();
        BaseRequest baseRequest = new BaseRequest(context);
        String body = new JSONObject()
                .putOnce(WxConsts.ReqFiled.BASE_REQ_UP, baseRequest)
                .putOnce(WxConsts.ReqFiled.MSG, message)
                .putOnce(WxConsts.ReqFiled.SCENE, 0).toString();
        String rsp = HttpUtil.post(url, body);
        return JSONUtil.parseObj(rsp);
    }

    /**
     * 发送消息
     *
     * @param message 消息
     * @return 服务器的响应结果
     */
    default JSONObject sendMsg(WxMessage message) {
        String url = null;
        if (message.getType() == MsgType.TEXT_MSG.getCode()) {
            url=textMsgUrl();
        } else if (message.getType() == MsgType.IMAGE_MSG.getCode()) {
            url=imageMsgUrl();
        } else if (message.getType() == MsgType.VIDEO_MSG.getCode()) {
            url=videoMsgUrl();
        }
        return Optional.ofNullable(url)
                .map(s -> sendMsg(s, message))
                .orElseGet(JSONUtil::createObj);
    }

    String videoMsgUrl();

    String imageMsgUrl();

    String textMsgUrl();


    /**
     * 上传媒体信息
     *
     * @param fromUserName 媒体上传方
     * @param userName     媒体上传后将要被发送给谁
     * @param mediaData    媒体数据
     * @param mediaName    媒体文件名
     * @param mediaType    媒体类型
     * @return 服务器的响应结果
     */
    JSONObject uploadMedia(String fromUserName, String userName, byte[] mediaData, String mediaName,
                           MediaType mediaType);
}
