package cn.com.pfinfo.weixin.websdk.wechat4j.model;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * AppInfo
 *
 * @auther cuitpanfei
 */
@Getter
@Setter
public final class AppInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer type;
    private String appID;
    private AppInfo() {
    }

    public static AppInfo valueOf(JSONObject info) {
        if (info == null) {
            return null;
        }
        info.getConfig().setIgnoreCase(true);
        return info.toBean(AppInfo.class);
    }

    public static List<AppInfo> valueOf(JSONArray infos) {
        if (infos == null) {
            return Collections.emptyList();
        }
        infos.getConfig().setIgnoreCase(true);
        return infos.toList(AppInfo.class);
    }
}
