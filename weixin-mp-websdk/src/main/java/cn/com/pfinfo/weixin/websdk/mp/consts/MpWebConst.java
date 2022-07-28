package cn.com.pfinfo.weixin.websdk.mp.consts;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
public class MpWebConst {
    public static final String TOKEN = "token";
    public static final String LANG = "lang";
    public static final String AJAX = "ajax";
    public static final String JSON = "json";
    public static final String LANG_ZH_CN = "zh_CN";

    public enum MaterialType {
        /**
         * 音频
         */
        AUDIO(3),
        /**
         * 图片
         */
        IMG(2),
        /**
         * 视频
         */
        VIDEO(15);
        public final int code;

        MaterialType(int code) {
            this.code = code;
        }
    }
}
