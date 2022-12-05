package cn.com.pfinfo.weixin.websdk.wechat4j.model;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 微信用户信息
 *
 * @auther cuitpanfei
 */
@Getter
@Setter
public final class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long uin;
    private String nickName;
    private String headImgUrl;
    private Integer contactFlag;
    private Integer memberCount;
    private List<UserInfo> memberList;
    private String remarkName;
    private Integer hideInputBarFlag;
    private Integer sex;
    private String signature;
    private Integer verifyFlag;
    private Long ownerUin;
    private String pyInitial;
    private String pyQuanPin;
    private String remarkPYInitial;
    private String remarkPYQuanPin;
    private Integer starFriend;
    private Integer appAccountFlag;
    private Integer statues;
    private Integer attrStatus;
    private String province;
    private String city;
    private String alias;
    private Integer snsFlag;
    private Integer uniFriend;
    private String displayName;
    private Long chatRoomId;
    private String keyWord;
    private String encryChatRoomId;
    private Integer isOwner;
    private String userName;
    private UserInfo() {
    }

    public static UserInfo valueOf(JSONObject info) {
        if (info == null) {
            return null;
        }
        info.getConfig().setIgnoreCase(true);
        return info.toBean(UserInfo.class);
    }

    public static List<UserInfo> valueOf(JSONArray infos) {
        if (infos == null) {
            return Collections.emptyList();
        }
        infos.getConfig().setIgnoreCase(true);
        return infos.toList(UserInfo.class);
    }
}
