package cn.com.pfinfo.weixin.websdk.wechat4j.stage;

import cn.com.pfinfo.weixin.websdk.wechat4j.model.UserInfo;
import cn.hutool.core.net.RFC3986;
import cn.hutool.json.JSONObject;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
public class WechatContext implements Serializable {
    public final String appId;
    //认证码
    public volatile String wxsid;
    public volatile String passTicket;
    public volatile String skey;
    public volatile String wxuin;
    //url版本号
    public volatile String urlVersion;
    //用户数据
    public volatile UserInfo loginUser;

    public volatile JSONObject syncKey;

    public volatile List<UserInfo> contactList;

    public volatile boolean isOnline = false;

    public WechatContext(String appId) {
        this.appId = appId;
    }

    public void clear() {
        this.wxsid = null;
        this.passTicket = null;
        this.skey = null;
        this.wxuin = null;
        this.urlVersion = null;
        this.loginUser = null;
        this.syncKey = null;
        if (this.contactList != null) {
            this.contactList.clear();
        }
        this.contactList = null;
        this.isOnline = false;
    }

    public String encodePassTicket() {
        return RFC3986.FRAGMENT.encode(passTicket, StandardCharsets.UTF_8);
    }

    public Optional<String> currentUserName() {
        return Optional.ofNullable(loginUser)
                .map(UserInfo::getUserName);
    }

    public Optional<UserInfo> getContactByName(String name, Function<UserInfo, String> fun) {
        if (name == null) {
            return Optional.empty();
        }
        if (contactList != null) {
            for (UserInfo userInfo : contactList) {
                if (userInfo == null) {
                    continue;
                }
                String targetName = fun.apply(userInfo);
                if (name.equals(targetName)) {
                    return Optional.of(userInfo);
                }
            }
        }
        return Optional.empty();
    }


    public Optional<UserInfo> getContactByNickName(String nickName) {
        return getContactByName(nickName, UserInfo::getNickName);
    }

    public Optional<UserInfo> getContactByRemarkName(String remarkName) {
        return getContactByName(remarkName, UserInfo::getRemarkName);
    }

    public Optional<String> getContactUserNameByNickName(String nickName) {
        return getContactByNickName(nickName)
                .map(UserInfo::getUserName);
    }

    public Optional<String> getContactUserNameByRemarkName(String remarkName) {
        return getContactByRemarkName(remarkName)
                .map(UserInfo::getUserName);
    }
}
