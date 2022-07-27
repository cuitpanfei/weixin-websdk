package cn.com.pfinfo.weixin.websdk.common.consts;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public class WxConsts {


    /**
     * app 类型
     * @author cuitpanfei
     */
    public static class AppType {
        /**
         * 公众号app类型
         */
        public static final String MP_TYPE = "mp";
        /**
         * 小程序app类型
         */
        public static final String MINI_TYPE = "mini";
    }

    /**
     * 微信Api的json返回结果中的公共字段。
     */
    public static class RespFiled {
        public static final String RET = "ret";
        public static final String BASE_RESP = "base_resp";
        public static final String BASE_RESP_DOT_RET = BASE_RESP+"."+RET;
    }

}
