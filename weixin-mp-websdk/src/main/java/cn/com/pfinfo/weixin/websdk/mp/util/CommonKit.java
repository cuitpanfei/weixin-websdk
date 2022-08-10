package cn.com.pfinfo.weixin.websdk.mp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
public class CommonKit {

    private static final GsonBuilder INSTANCE = new GsonBuilder();
    public static final Gson GSON_INSTANCE = INSTANCE.create();

    public static String handlerNickname(String str, boolean escape) {
        String[] ar = {"&", "&amp;", "<", "&lt;", ">", "&gt;", " ", "&nbsp;", "\"", "&quot;", "'", "&#39;"};
        String[] arReverse = {"&#39;", "'", "&quot;", "\"", "&nbsp;", " ", "&gt;", ">", "&lt;", "<", "&amp;", "&"};
        String[] target;
        if (escape) {
            target = ar;
        } else {
            target = arReverse;
        }
        String r = str;
        for (int i = 0; i < target.length; i += 2) {
            r = r.replace(target[i], target[1 + i]);
        }
        return r;
    }
}
