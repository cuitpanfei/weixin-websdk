package cn.com.pfinfo.weixin.websdk.wechat4j.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * RecommendInfo
 *
 * @auther cuitpanfei
 */
@Getter
@Setter
public final class RecommendInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ticket;
    private String userName;
    private Integer sex;
    private Integer attrStatus;
    private String city;
    private String nickName;
    private Integer scene;
    private String province;
    private String content;
    private String alias;
    private String signature;
    private Integer opCode;
    private Long qqNum;
    private Integer verifyFlag;
    private RecommendInfo() {
    }

    public static RecommendInfo valueOf(JSONObject info) {
        if (info == null) {
            return null;
        }
        info.getConfig().setIgnoreCase(true);
        return info.toBean(RecommendInfo.class);
    }

    public static List<RecommendInfo> valueOf(JSONArray infos) {
        if (infos == null) {
            return Collections.emptyList();
        }
        infos.getConfig().setIgnoreCase(true);
        return infos.toList(RecommendInfo.class);
    }
}
