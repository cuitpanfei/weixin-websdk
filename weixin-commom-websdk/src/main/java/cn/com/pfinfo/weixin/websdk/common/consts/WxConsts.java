package cn.com.pfinfo.weixin.websdk.common.consts;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxConsts {

    /**
     * 应用主站网址
     */
    public static class AppSite {
        public static final String MP_WEIXIN_QQ_COM = "https://mp.weixin.qq.com";
        public static final String WECHAT_QQ_COM = "https://wx.qq.com";
    }

    /**
     * app 类型
     *
     * @author cuitpanfei
     */
    public static class AppType {
        /**
         * 公众号app类型
         */
        public static final String WECHAT_WEB_TYPE = "wechat";
        /**
         * 公众号app类型
         */
        public static final String MP_TYPE = "mp";
        /**
         * 小程序app类型
         */
        public static final String MINI_TYPE = "mini";
    }

    public static class ReqFiled {
        public static final String BASE_REQ_UP = "BaseRequest";
        public static final String URL_VERSION = "urlVersion";
        public static final String SKEY = "skey";
        public static final String SID = "sid";
        public static final String PASS_TICKET = "pass_ticket";
        public static final String SYNC_KEY = "SyncKey";
        public static final String SYNC_KEY_LOW = "synckey";
        public static final String SCENE = "Scene";
        public static final String MSG = "Msg";
        public static final String UIN = "uin";
        public static final String DEVICEID = "deviceid";
        public static final String TYPE = "type";
        public static final String REDIRECT_URI = "redirectUri";
        public static final String UUID = "uuid";
        public static final String UPLOAD_TYPE = "UploadType";
        public static final String CLIENT_MEDIA_ID = "ClientMediaId";
        public static final String TOTAL_LEN = "TotalLen";
        public static final String START_POS = "StartPos";
        public static final String DATA_LEN = "DataLen";
        public static final String FROM_USER_NAME = "FromUserName";
        public static final String TO_USER_NAME = "ToUserName";
        public static final String FILE_MD5 = "FileMd5";
        public static final String CHUNK = "chunk";
        public static final String CHUNKS = "chunks";
        public static final String UPLOAD_MEDIA_REQUEST = "uploadmediarequest";
        public static final String WEBWX_DATA_TICKET = "webwx_data_ticket";
        public static final String FILE_NAME = "filename";
    }

    /**
     * 微信Api的json返回结果中的公共字段。
     */
    public static class RespFiled {
        public static final String RET = "ret";
        public static final String RET_UP = "Ret";
        public static final String BASE_RESP = "base_resp";
        public static final String BASE_RESP_UP = "BaseResponse";
        public static final String BASE_RESP_DOT_RET = BASE_RESP + "." + RET;
        public static final String BASE_RESP_DOT_RET_UP = BASE_RESP_UP + "." + RET_UP;
    }

}
