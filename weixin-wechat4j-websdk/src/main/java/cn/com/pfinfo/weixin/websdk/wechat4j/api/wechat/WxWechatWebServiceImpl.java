package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat;

import cn.com.pfinfo.weixin.websdk.common.event.EventManager;
import cn.com.pfinfo.weixin.websdk.common.exception.RRException;
import cn.com.pfinfo.weixin.websdk.common.http.R;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.event.SyncMonitorEvent;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login.WxWechatWebLoginService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login.WxWechatWebLoginServiceImpl;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login.entity.RedirectInfo;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.msg.WxWechatWebMsgService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.msg.WxWechatWebMsgServiceImpl;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.syncdata.WxWechatWebSyncDataService;
import cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.syncdata.WxWechatWebSyncDataServiceImpl;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.LoginTip;
import cn.com.pfinfo.weixin.websdk.wechat4j.enums.MsgType;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.UserInfo;
import cn.com.pfinfo.weixin.websdk.wechat4j.model.WxMessage;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatContext;
import cn.com.pfinfo.weixin.websdk.wechat4j.util.QRCodeUtil;
import cn.com.pfinfo.weixin.websdk.wechat4j.util.WechatUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.setting.Setting;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by cuitpanfei on 2022/11/30
 *
 * @author cuitpanfei
 */
public class WxWechatWebServiceImpl implements WxWechatWebService {
    private static final Log log = Log.get(WxWechatWebServiceImpl.class);
    private final Lock isOnlineLock = new ReentrantLock();
    private final Lock loginUserLock = new ReentrantLock();
    protected Setting wechatSetting = WechatApp.appSetting;
    protected WxWechatWebLoginService loginService = Singleton.get(WxWechatWebLoginServiceImpl.class);
    protected WxWechatWebSyncDataService syncDataService = Singleton.get(WxWechatWebSyncDataServiceImpl.class);
    protected WxWechatWebMsgService msgService = Singleton.get(WxWechatWebMsgServiceImpl.class);

    {
        String appId = WechatApp.appSetting.get("webwx", "appid");
        if (!App.getAppId().isPresent()) {
            App.updateAppId(appId);
        }
    }

    @Override
    public boolean isOnline() {
        return WechatApp.getContext().isOnline;
    }

    @Override
    public boolean cookieIsExpired() {
        return false;
    }

    @Override
    public boolean autoLogin() {
        return autoLogin(false);
    }


    /**
     * 自动登录
     */
    @Override
    public boolean autoLogin(boolean tryPushLogin) {
        // 1、判断是否已经登录
        initData();
        if (isOnline()) {
            log.info("当前已是登录状态，无需登录");
            return true;
        }

        int time = wechatSetting.getInt("wechat4j.retry.time", 3);

        // 2、登录
        // 2.1、获取uuid
        String uuid = Optional.ofNullable(WechatApp.getContext().wxuin)
                .filter(s -> tryPushLogin)
                .map(uin -> getWxUuid())
                .orElseGet(() -> {
                    clearCurrentLoginInfo();
                    return getWxUuid(time);
                });

        return Optional.ofNullable(uuid)
                .map(s -> getAndShowQRCode(s, time)).filter(r -> r)
                .map(r -> waitForConfirm(uuid))
                .map(r -> {
                    WechatApp.getContext().urlVersion = r.getUrlVersion();
                    return r.getRedirectUri();
                })
                .map(this::getLoginCode).filter(r -> r)
                .map(r -> initData(time))
                .orElse(false);
    }

    /**
     * 发送文本消息
     *
     * @param userName 用户名（加密的）
     * @param content  文本消息内容
     * @return 返回数据
     */
    @Override
    public JSONObject sendTextToUserName(String userName, String content) {
        String msgId = WechatUtil.createMsgId();
        String loginUserName = getLoginUserName(false);
        WxMessage message = new WxMessage();
        message.setClientMsgId(msgId);
        message.setContent(content);
        message.setFromUserName(loginUserName);
        message.setLocalID(msgId);
        if (CharSequenceUtil.isEmpty(userName)) {
            message.setToUserName(loginUserName);
        } else {
            message.setToUserName(userName);
        }
        message.setType(MsgType.TEXT_MSG.getCode());

        return msgService.sendMsg(message);
    }


    /**
     * 发送文本消息（根据昵称）
     *
     * @param nickName 昵称
     * @param content  文本消息内容
     * @return 返回数据
     */
    @Override
    public JSONObject sendTextToNickName(String nickName, String content) {
        WechatContext context = WechatApp.getContext();
        Optional<String> userName = context.getContactUserNameByNickName(nickName);
        return userName.map(name -> sendTextToUserName(name, content))
                .orElseGet(JSONUtil::createObj);
    }

    /**
     * 发送文本消息（根据备注名）
     *
     * @param remarkName 备注
     * @param content    文本消息内容
     * @return 返回数据
     */
    @Override
    public JSONObject sendTextToRemarkName(String remarkName, String content) {
        WechatContext context = WechatApp.getContext();
        Optional<String> userName = context.getContactUserNameByRemarkName(remarkName);
        return userName.map(name -> sendTextToUserName(name, content))
                .orElseGet(JSONUtil::createObj);
    }

    /**
     * 发送文本消息（根据多种名称）
     *
     * @param userName   用户名（加密的）
     * @param nickName   昵称
     * @param remarkName 备注
     * @param content    文本消息内容
     * @return 返回数据
     */
    @Override
    public JSONObject sendText(String userName, String nickName, String remarkName, String content) {
        if (CharSequenceUtil.isNotEmpty(userName)) {
            return sendTextToUserName(userName, content);
        } else if (CharSequenceUtil.isNotEmpty(nickName)) {
            return sendTextToNickName(nickName, content);
        } else if (CharSequenceUtil.isNotEmpty(remarkName)) {
            return sendTextToRemarkName(remarkName, content);
        } else {
            String loginUserName = getLoginUserName(false);
            return sendTextToUserName(loginUserName, content);
        }
    }

    /**
     * 发送图片消息
     *
     * @param userName    用户名（加密的）
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendImageToUserName(String userName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送图片消息
     *
     * @param userName 用户名（加密的）
     * @param image    图片文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendImageToUserName(String userName, File image) {
        return null;
    }

    /**
     * 发送图片消息（根据昵称）
     *
     * @param nickName    昵称
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendImageToNickName(String nickName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送图片消息（根据昵称）
     *
     * @param nickName 昵称
     * @param image    图片文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendImageToNickName(String nickName, File image) {
        return null;
    }

    /**
     * 发送图片消息（根据备注名）
     *
     * @param remarkName  备注名
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendImageToRemarkName(String remarkName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送图片消息（根据备注名）
     *
     * @param remarkName 备注名
     * @param image      图片文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendImageToRemarkName(String remarkName, File image) {
        return null;
    }

    /**
     * 发送图片消息（根据多种名称）
     *
     * @param userName    用户名（加密的）
     * @param nickName    昵称
     * @param remarkName  备注名
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendImage(String userName, String nickName, String remarkName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送图片消息（根据多种名称）
     *
     * @param userName   用户名（加密的）
     * @param nickName   昵称
     * @param remarkName 备注名
     * @param image      图片文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendImage(String userName, String nickName, String remarkName, File image) {
        return null;
    }

    /**
     * 发送视频消息
     *
     * @param userName    用户名（加密的）
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideoToUserName(String userName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送视频消息
     *
     * @param userName 用户名（加密的）
     * @param video    视频文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideoToUserName(String userName, File video) {
        return null;
    }

    /**
     * 发送视频消息（根据昵称）
     *
     * @param nickName    昵称
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideoToNickName(String nickName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送视频消息（根据昵称）
     *
     * @param nickName 昵称
     * @param video    视频文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideoToNickName(String nickName, File video) {
        return null;
    }

    /**
     * 发送视频消息（根据备注名）
     *
     * @param remarkName  备注名
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideoToRemarkName(String remarkName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送视频消息（根据备注名）
     *
     * @param remarkName 备注名
     * @param video      视频文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideoToRemarkName(String remarkName, File video) {
        return null;
    }

    /**
     * 发送视频消息（根据多种名称）
     *
     * @param userName    用户名（加密的）
     * @param nickName    昵称
     * @param remarkName  备注名
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideo(String userName, String nickName, String remarkName, byte[] mediaData, String mediaName, ContentType contentType) {
        return null;
    }

    /**
     * 发送视频消息（根据多种名称）
     *
     * @param userName   用户名（加密的）
     * @param nickName   昵称
     * @param remarkName 备注名
     * @param video      视频文件
     * @return 返回数据
     */
    @Override
    public JSONObject sendVideo(String userName, String nickName, String remarkName, File video) {
        return null;
    }

    private boolean initData() {
        int time = wechatSetting.getInt("wechat4j.retry.time", 3);
        boolean success = initData(time);
        if (!success) {
            clearCurrentLoginInfo();
        }
        return success;
    }

    private boolean initData(int time) {
        if (!wxInitWithRetry(time)) {
            log.info("初始化数据失败，请重新登录");

            return false;
        }
        log.info("微信登录成功，欢迎你：" + getLoginUserNickName(false));


        isOnlineLock.lock();
        try {
            statusNotify(time);
            WechatApp.getContext().isOnline = true;
            EventManager.publish(new SyncMonitorEvent());
        } finally {
            isOnlineLock.unlock();
        }
        return true;
    }


    /**
     * 获取登录用户对象
     *
     * @return 登录用户对象
     */
    public UserInfo getLoginUser(boolean update) {
        WechatContext context = WechatApp.getContext();
        if (context.loginUser == null || update) {
            JSONObject result;
            try {
                result = syncDataService.webWeixinInit();
                if (result.isEmpty()) {
                    return context.loginUser;
                }
            } catch (RRException e) {
                log.error("", e);
                return context.loginUser;
            }

            loginUserLock.lock();
            try {
                if (context.loginUser == null || update) {
                    context.loginUser = UserInfo.valueOf(result.getJSONObject("User"));
                }
            } finally {
                loginUserLock.unlock();
            }

            return context.loginUser;
        }
        return context.loginUser;
    }

    public UserInfo getLoginUser() {
        return getLoginUser(false);
    }

    /**
     * 获取登录用户名
     *
     * @return 登录用户的用户名（加密的）
     */
    public String getLoginUserName(boolean update) {
        UserInfo loginUser = getLoginUser(update);
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUserName();
    }

    public String getLoginUserName() {
        return getLoginUserName(false);
    }

    /**
     * 获取登录用户的昵称
     *
     * @return 登录用户的昵称
     */
    public String getLoginUserNickName(boolean update) {
        UserInfo loginUser = getLoginUser(update);
        if (loginUser == null) {
            return null;
        }
        return loginUser.getNickName();
    }

    public String getLoginUserNickName() {
        return getLoginUserNickName(false);
    }


    /**
     * 获取uuid（登录时）
     *
     * @param time 次数
     * @return
     */
    private String getWxUuid(int time) {
        log.info("尝试正常方式获取uuid...");

        for (int i = 0; i <= time; i++) {
            if (i > 0) {
                log.info("第" + i + "次尝试...");
            }
            JSONObject result = loginService.wxUuid();

            if (result.isEmpty()) {
                log.info("失败：出现异常");
                return null;
            }

            String code = result.getStr("code");
            if (!"200".equals(code)) {
                String msg = result.getStr("msg");
                log.info("失败：" + msg);
                return null;
            }

            String uuid = result.getStr("uuid");
            if (CharSequenceUtil.isEmpty(uuid)) {
                log.info("失败");
                if (i == 0 && time > 0) {
                    log.info("，将重复尝试" + time + "次");
                }
                continue;
            }

            log.info("成功，值为：" + uuid);
            return uuid;
        }
        return null;
    }


    /**
     * push方式获取uuid（登录时）
     *
     * @return
     */
    private String getWxUuid() {
        log.info("尝试push方式获取uuid...");
        JSONObject result = loginService.pushLogin();
        if (result == null) {
            log.info("失败：出现异常");
            return null;
        }

        String ret = result.getStr("ret");
        if (!"0".equals(ret)) {
            log.info("失败：错误的返回码(" + ret + ")");
            return null;
        }
        String uuid = result.getStr("uuid");
        if (CharSequenceUtil.isEmpty(uuid)) {
            log.info("失败：空值");
            return null;
        }
        log.info("成功，值为：" + uuid);
        return uuid;
    }

    /**
     * 获取并显示qrcode（登录时）
     *
     * @return 是否成功
     */
    private boolean getAndShowQRCode(String uuid, int time) {
        log.info("获取二维码...");
        for (int i = 0; i <= time; i++) {
            if (i > 0) {
                log.info("第" + i + "次尝试...");
            }
            byte[] data = loginService.qrCode(uuid);
            if (data == null || data.length <= 0) {
                log.info("失败");
                if (i == 0 && time > 0) {
                    log.info("，将重新获取uuid并重复尝试" + time + "次");
                }
                getWxUuid(0);
                ThreadUtil.sleep(2000);
                continue;
            }
            log.info("成功，请扫描二维码：");
            log.info("\n{}", QRCodeUtil.toCharMatrix(data));
            QRCodeUtil.openQRCodeImage(data);
            return true;
        }
        return false;
    }


    /**
     * 等待手机端确认登录（登录时）
     *
     * @return
     */
    private RedirectInfo waitForConfirm(String uuid) {
        log.info("等待手机端扫码...");
        boolean flag = false;
        while (true) {
            R<RedirectInfo> result = loginService.redirect(uuid, LoginTip.TIP_0);
            if (!result.isSuccess()) {
                log.warn(result.getMsg());
                break;
            }
            int code = result.getCode();
            if (code == 408) {
                System.out.println(".");
            } else if (code == 400) {
                log.info("失败，二维码失效");
                break;
            } else if (code == 201) {
                if (!flag) {
                    log.info("请确认登录...");
                    flag = true;
                }
            } else if (code == 200) {
                log.info("成功，认证完成");
                return result.getPayload();
            } else {
                break;
            }
        }
        return null;
    }

    /**
     * 获取登录认证码（登录时）
     */
    private boolean getLoginCode(String redirectUri) {
        log.info("获取登录认证码...");
        JSONObject result = loginService.newlogin(redirectUri);
        if (result == null) {
            log.info("失败：出现异常");
            return false;
        }
        String ret = result.getStr("ret");
        if (!"0".equals(ret)) {
            log.info("失败：错误的返回码(" + ret + ")");
            return false;
        }
        WechatContext context = WechatApp.getContext();
        context.wxsid = result.getStr("wxsid");
        context.passTicket = result.getStr("pass_ticket");
        context.skey = result.getStr("skey");
        context.wxuin = result.getStr("wxuin");
        WechatApp.storageContext();
        log.info("成功");

        return true;
    }


    /**
     * 微信数据初始化
     *
     * @return
     */
    private boolean wxInit() {
        WechatContext context = WechatApp.getContext();
        JSONObject result;
        try {
            result = syncDataService.webWeixinInit();
            if (result.isEmpty()) {
                return false;
            }
        } catch (RRException e) {
            log.error("", e);
            return false;
        }
        context.loginUser = UserInfo.valueOf(result.getJSONObject("User"));
        context.syncKey = result.getJSONObject("SyncKey");

        return true;
    }

    /**
     * 微信数据初始化（登录时）
     *
     * @return
     */
    private boolean wxInitWithRetry(int time) {
        log.info("正在初始化数据...");

        for (int i = 0; i <= time; i++) {
            if (i > 0) {
                log.info("第" + i + "次尝试...");
            }

            if (!wxInit()) {
                log.info("失败");
                if (i == 0 && time > 0) {
                    log.info("，将重复尝试" + time + "次");
                }
                ThreadUtil.sleep(2000);
                continue;
            }
            log.info("成功");
            return true;
        }
        return false;
    }

    /**
     * 开启状态通知
     *
     * @param time 次数
     * @return
     */
    private boolean statusNotify(int time) {
        log.info("开启状态通知...");
        for (int i = 0; i < time; i++) {
            try {
                JSONObject result = syncDataService.statusNotify(getLoginUserName(false));
                if (result.isEmpty()) {
                    continue;
                }
            } catch (RRException e) {
                log.warn("", e);
                continue;
            }
            log.info("成功");
            return true;
        }

        return false;
    }


    /**
     * 退出登录
     *
     * @param clearLoginInfo 是否清除登录信息
     */
    public void logout(boolean clearLoginInfo) {
        if (isOnline()) {
            isOnlineLock.lock();
            try {
                WechatContext context = WechatApp.getContext();
                if (isOnline()) {
                    loginService.logout();
                    context.isOnline = false;
                    if (clearLoginInfo) {
                        clearCurrentLoginInfo();
                    }
                }
            } finally {
                isOnlineLock.unlock();
            }
        }
    }

    @Override
    public void logout() {
        logout(true);
    }

    /**
     * 清除全部登录信息
     */
    private void clearCurrentLoginInfo() {
        WechatApp.clearCurrentContext();
    }
}
