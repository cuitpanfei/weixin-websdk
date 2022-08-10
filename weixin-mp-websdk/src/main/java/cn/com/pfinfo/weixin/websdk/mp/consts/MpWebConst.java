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

    public enum DelPicGroupType {
        /**
         * 仅删除分组信息，组内图片放到非分组
         */
        DEL_GROUP_WITHOUT_PICS("0", "仅删除分组信息，组内图片放到非分组"),
        /**
         * 连同组内图片一起删除
         */
        DEL_WITH_PICS("1", "连同组内图片一起删除");
        public final String code;
        public final String msg;

        DelPicGroupType(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
