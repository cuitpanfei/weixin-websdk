package cn.com.pfinfo.weixin.websdk.common.model;

import cn.com.pfinfo.weixin.websdk.common.event.CookieChangeEvent;
import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import lombok.Builder;

import java.io.Serializable;
import java.util.Optional;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
@Builder
public class AuthModel implements Serializable, AppModel, CookieModel {
    /**
     * 微信号
     */
    protected String openId;
    /**
     * 应用名称
     */
    protected String openName;

    protected String cookie;

    /**
     * 唯一主键
     *
     * @return 唯一主键， MP为openid
     */
    @Override
    public String id() {
        return getOpenId();
    }

    /**
     * 应用名称
     *
     * @return 应用名称，MP为公众号名称
     */
    @Override
    public String name() {
        return getOpenName();
    }


    /**
     * 当前应用cookie
     *
     * @return 当前应用cookie。
     */
    @Override
    public Optional<String> cookie() {
        return Optional.ofNullable(getCookie());
    }

    @Override
    public void updateCookie(String cookie) {
        String oldCookie = getCookie();
        setCookie(cookie);
        EventManager.publish(CookieChangeEvent.of(App.appId(), oldCookie, cookie));
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenName() {
        return openName;
    }

    public void setOpenName(String openName) {
        this.openName = openName;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}
