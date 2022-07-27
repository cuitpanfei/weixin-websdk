package cn.com.pfinfo.weixin.websdk.common.model;

/**
 * created by cuitpanfei on 2022/07/25
 *
 * @author cuitpanfei
 */
public interface AppModel {

    /**
     * 唯一主键
     *
     * @return 唯一主键， MP为openid
     */
    String id();

    /**
     * 应用名称
     *
     * @return 应用名称，MP为公众号名称
     */
    String name();


}
