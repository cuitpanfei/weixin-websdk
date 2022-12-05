package cn.com.pfinfo.weixin.websdk.wechat4j.api.wechat.login.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by cuitpanfei on 2022/12/01
 *
 * @author cuitpanfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedirectInfo {
    private String redirectUri;
    private String urlVersion;
}
