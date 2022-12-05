package cn.com.pfinfo.weixin.websdk.wechat4j.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 要发送的消息
 *
 * @auther cuitpanfei
 */
@Getter
@Setter
public class WxMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ClientMsgId;
    private String Content;
    private String FromUserName;
    private String LocalID;
    private String ToUserName;
    private Integer Type;
}
