package cn.com.pfinfo.weixin.websdk.wechat4j.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public final class ReceivedMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer subMsgType;
    private Long voiceLength;
    private String fileName;
    private Long imgHeight;
    private String toUserName;
    private Long hasProductId;
    private Integer imgStatus;
    private String url;
    private Integer imgWidth;
    private Integer forwardFlag;
    private Integer status;
    private String ticket;
    private RecommendInfo recommendInfo;
    private Long createTime;
    private Long newMsgId;
    private Integer msgType;
    private String encryFileName;
    private String msgId;
    private Integer statusNotifyCode;
    private AppInfo appInfo;
    private Integer appMsgType;
    private Long playLength;
    private String mediaId;
    private String content;
    private String statusNotifyUserName;
    private String fromUserName;
    private String oriContent;
    private String fileSize;
    private ReceivedMsg() {
    }

    public static ReceivedMsg valueOf(JSONObject msg) {
        if (msg == null) {
            return null;
        }
        msg.getConfig().setIgnoreCase(true);
        return msg.toBean(ReceivedMsg.class);
    }

    public static List<ReceivedMsg> valueOf(JSONArray msgs) {
        if (msgs == null) {
            return Collections.emptyList();
        }
        msgs.getConfig().setIgnoreCase(true);
        return msgs.toList(ReceivedMsg.class);
    }
}
