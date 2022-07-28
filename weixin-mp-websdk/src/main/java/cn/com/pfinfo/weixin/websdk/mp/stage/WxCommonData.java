package cn.com.pfinfo.weixin.websdk.mp.stage;


import lombok.Getter;
import lombok.Setter;

/**
 * created by cuitpanfei on 2022/07/28
 *
 * @author cuitpanfei
 */
@Getter
@Setter
public class WxCommonData {
    private String ticket;
    private String lang;
    private String param;
    private String uin;
    private String uinBase;
    private String userName;
    private String nickName;
    private String nickNameDecode;
}