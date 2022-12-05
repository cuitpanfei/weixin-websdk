package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat;

import cn.com.pfinfo.weixin.websdk.common.api.WxWebAppService;
import cn.com.pfinfo.weixin.websdk.common.consts.WxConsts;
import cn.com.pfinfo.weixin.websdk.common.stage.App;
import cn.com.pfinfo.weixin.websdk.wechat4j.stage.WechatApp;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;

import java.io.File;

/**
 * created by cuitpanfei on 2022/11/30
 *
 * @author cuitpanfei
 */
public interface WxWechatWebService extends WxWebAppService {

    @Override
    default String appType() {
        return WxConsts.AppType.WECHAT_WEB_TYPE;
    }

    /**
     * 自动登录
     *
     * @return 成功状态
     */
    boolean autoLogin();

    boolean autoLogin(boolean tryPushLogin);

    /**
     * 发送文本消息（根据昵称）
     *
     * @param nickName 昵称
     * @param content  文本消息内容
     * @return 返回数据
     */
    JSONObject sendTextToNickName(String nickName, String content);
    /**
     * 发送文本消息（根据备注名）
     *
     * @param remarkName 备注
     * @param content    文本消息内容
     * @return 返回数据
     */
    JSONObject sendTextToRemarkName(String remarkName, String content);

    /**
     * 发送文本消息
     *
     * @param userName 用户名（加密的）
     * @param content  文本消息内容
     * @return 返回数据
     */
    JSONObject sendTextToUserName(String userName, String content);

    /**
     * 发送文本消息（根据多种名称）
     *
     * @param userName   用户名（加密的）
     * @param nickName   昵称
     * @param remarkName 备注
     * @param content    文本消息内容
     * @return 返回数据
     */
    JSONObject sendText(String userName, String nickName, String remarkName, String content);
//=====================



    /**
     * 发送图片消息
     *
     * @param userName    用户名（加密的）
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    JSONObject sendImageToUserName(String userName, byte[] mediaData, String mediaName, ContentType contentType);
    /**
     * 发送图片消息
     *
     * @param userName 用户名（加密的）
     * @param image    图片文件
     * @return 返回数据
     */
    JSONObject sendImageToUserName(String userName, File image);

    /**
     * 发送图片消息（根据昵称）
     *
     * @param nickName    昵称
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    JSONObject sendImageToNickName(String nickName, byte[] mediaData, String mediaName, ContentType contentType);


    /**
     * 发送图片消息（根据昵称）
     *
     * @param nickName 昵称
     * @param image    图片文件
     * @return 返回数据
     */
    JSONObject sendImageToNickName(String nickName, File image);


    /**
     * 发送图片消息（根据备注名）
     *
     * @param remarkName  备注名
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    JSONObject sendImageToRemarkName(String remarkName, byte[] mediaData, String mediaName,
                                            ContentType contentType);


    /**
     * 发送图片消息（根据备注名）
     *
     * @param remarkName 备注名
     * @param image      图片文件
     * @return 返回数据
     */
    JSONObject sendImageToRemarkName(String remarkName, File image);


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
    JSONObject sendImage(String userName, String nickName, String remarkName, byte[] mediaData,
                         String mediaName, ContentType contentType);

    /**
     * 发送图片消息（根据多种名称）
     *
     * @param userName   用户名（加密的）
     * @param nickName   昵称
     * @param remarkName 备注名
     * @param image      图片文件
     * @return 返回数据
     */
    JSONObject sendImage(String userName, String nickName, String remarkName, File image);


    /**
     * 发送视频消息
     *
     * @param userName    用户名（加密的）
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    JSONObject sendVideoToUserName(String userName, byte[] mediaData, String mediaName, ContentType contentType);


    /**
     * 发送视频消息
     *
     * @param userName 用户名（加密的）
     * @param video    视频文件
     * @return 返回数据
     */
    JSONObject sendVideoToUserName(String userName, File video);


    /**
     * 发送视频消息（根据昵称）
     *
     * @param nickName    昵称
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    JSONObject sendVideoToNickName(String nickName, byte[] mediaData, String mediaName, ContentType contentType);


    /**
     * 发送视频消息（根据昵称）
     *
     * @param nickName 昵称
     * @param video    视频文件
     * @return 返回数据
     */
    JSONObject sendVideoToNickName(String nickName, File video);


    /**
     * 发送视频消息（根据备注名）
     *
     * @param remarkName  备注名
     * @param mediaData   媒体文件数据
     * @param mediaName   媒体文件名
     * @param contentType 媒体文件类型
     * @return 返回数据
     */
    JSONObject sendVideoToRemarkName(String remarkName, byte[] mediaData, String mediaName,
                                     ContentType contentType);


    /**
     * 发送视频消息（根据备注名）
     *
     * @param remarkName 备注名
     * @param video      视频文件
     * @return 返回数据
     */
    JSONObject sendVideoToRemarkName(String remarkName, File video);


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
    JSONObject sendVideo(String userName, String nickName, String remarkName, byte[] mediaData,
                         String mediaName, ContentType contentType);

    /**
     * 发送视频消息（根据多种名称）
     *
     * @param userName   用户名（加密的）
     * @param nickName   昵称
     * @param remarkName 备注名
     * @param video      视频文件
     * @return 返回数据
     */
    JSONObject sendVideo(String userName, String nickName, String remarkName, File video);

    boolean isOnline();

    void logout();

    @Override
    default void finish() {
        WxWebAppService.super.finish();
        WechatApp.clearAllContext();
    }

}
