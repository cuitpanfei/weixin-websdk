package cn.com.pfinfo.weixin.websdk.wechat4j.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.util.stream.Collectors;

/**
 * 微信工具类
 *
 * @auther cuitpanfei
 */
public final class WechatUtil {
    private WechatUtil() {
    }

    private static String STRING_CHARS_1 = "123456789";
    private static String STRING_CHARS_2 = "1234567890";

    /**
     * 创建一个设备ID
     *
     * @return 设备ID
     */
    public static String createDeviceID() {
        return "e" + RandomUtil.randomString(STRING_CHARS_1,15);
    }

    /**
     * 创建一个消息ID
     *
     * @return 消息ID
     */
    public static String createMsgId() {
        return System.currentTimeMillis() + RandomUtil.randomString(STRING_CHARS_2,4);
    }

    /**
     * 把SyncKeyList转为字符串格式
     *
     * @param syncKeyList syncKeyList
     * @return 字符串
     */
    public static String syncKeyListToString(JSONArray syncKeyList) {
        if (syncKeyList == null) {
            return null;
        }
        String syncKey = syncKeyList.stream().map(JSONUtil::parseObj)
                .map(json-> json.getStr("Key")+"_"+json.getStr("Val"))
                .collect(Collectors.joining("|"));
        return syncKey;
    }
}
